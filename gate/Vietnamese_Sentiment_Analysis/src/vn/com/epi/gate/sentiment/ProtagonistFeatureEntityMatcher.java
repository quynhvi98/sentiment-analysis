package vn.com.epi.gate.sentiment;

import static gate.Utils.end;
import static gate.Utils.start;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Utils;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * A feature-entity matcher that combines closest matching in sentence
 * and protagonist method in other cases.
 * Posted from May 26, 2018 3:49 PM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 *
 */
public class ProtagonistFeatureEntityMatcher implements FeatureEntityMatcher {

	private static final Logger LOGGER = Logger.getLogger(ProtagonistFeatureEntityMatcher.class);
	
	private Document document;
	private String inputASName = null;
	private List<String> entityAnnotationTypes = new ArrayList<String>();
	private Set<String> entityAnnotationTypeSet;
	private Map<String, Annotation> protagonistMap;
	private Annotation[] tokens;
	private Annotation[] sentences;
	private double alpha = 5;
	
	/* (non-Javadoc)
	 * @see vn.com.epi.gate.sentiment.FeatureEntityMatcher#init()
	 */
	@Override
	public void init() throws ResourceInstantiationException {
		entityAnnotationTypeSet = new HashSet<String>(entityAnnotationTypes);
	}
	
	/* (non-Javadoc)
	 * @see vn.com.epi.gate.sentiment.FeatureEntityMatcher#execute()
	 */
	@Override
	public void execute() throws ExecutionException {
		tokens = initAnnotationArray("Token");
		sentences = initAnnotationArray("Sentence");
		findProtagonists();
		matchFeatureToEntity();
	}

	private Annotation[] initAnnotationArray(String type) {
		AnnotationSet tokenSet = document.getAnnotations(inputASName).get(type);
		Annotation[] annArr = tokenSet.toArray(new Annotation[tokenSet.size()]);
		Arrays.sort(annArr, Utils.OFFSET_COMPARATOR);
		return annArr;
	}

	private void findProtagonists() {
		protagonistMap = new HashMap<String, Annotation>();
		Map<Annotation, Entity> annMap = new HashMap<Annotation, Entity>();
		// count
		AnnotationSet entityAnns = document.getAnnotations(inputASName).get(entityAnnotationTypeSet);
		for (Annotation ann : entityAnns) {
			Annotation rep = getRepresentative(ann);
			if (!annMap.containsKey(rep)) {
				annMap.put(rep, new Entity(rep));
			}
			Entity entity = annMap.get(rep);
			entity.score += Math.exp(-alpha * relativePosition(ann));
		}
		// compute score and find best protagonist
		Map<String, Double> bestScoreMap = new HashMap<String, Double>();
		bestScoreMap.put("", Double.NEGATIVE_INFINITY);
		for (String type : entityAnnotationTypes) {
			bestScoreMap.put(type, Double.NEGATIVE_INFINITY);
		}
		for (Entry<Annotation, Entity> entry : annMap.entrySet()) {
			Entity entity = entry.getValue();
			String type = entity.getRepresentative().getType();
			if (entity.score > bestScoreMap.get(type)) {
				bestScoreMap.put(type, entity.score);
				protagonistMap.put(type, entity.getRepresentative());
			}
			if (entity.score > bestScoreMap.get("")) {
				bestScoreMap.put("", entity.score);
				protagonistMap.put("", entity.getRepresentative());
			}
		}
		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Protagonists: " + protagonistMap); 
		}
	}

	@SuppressWarnings("unchecked")
	private Annotation getRepresentative(Annotation ann) {
		if (ann.getFeatures().get("matches") != null) {
			Collection<Integer> matches = (Collection<Integer>) ann
					.getFeatures().get("matches");
			return document.getAnnotations(inputASName).get(
					(Integer) matches.iterator().next());
		}
		return ann;
	}
	
	private void matchFeatureToEntity() {
		AnnotationSet featureAnns = document.getAnnotations(inputASName).get("Feature");
		for (Annotation ann : featureAnns) {
			if (isAlreadyMatched(ann)) {
				continue;
			}
			String entityType = "";
			if (ann.getFeatures().containsKey("entityType")) {
				entityType = (String) ann.getFeatures().get("entityType");
			}
			Annotation entity = findClosestInSentence(ann, entityType);
			if (entity == null) {
				entity = getProtagonist(ann, entityType);
			}
			if (entity != null) {
				match(ann, entity);
			}
		}
	}

	private boolean isAlreadyMatched(Annotation ann) {
		return ann.getFeatures().containsKey("feature-of");
	}

	private Annotation findClosestInSentence(Annotation ann, String entityType) {
		int sentenceIndex = indexOf(ann, sentences);
		if (sentenceIndex < 0) {
			LOGGER.warn("Annotation not in any sentence: " + ann);
			return null;
		}
		Annotation sentence = sentences[sentenceIndex];
		Annotation bestAnn = null;
		double bestDistance = Long.MAX_VALUE;
		
		AnnotationSet containedAnnotations = document.getAnnotations(inputASName)
				.get(start(sentence), end(sentence));
		for (Annotation containedAnn : containedAnnotations) {
			if (entityAnnotationTypeSet.contains(containedAnn.getType()) &&
					(entityType.isEmpty() || entityType.equals(containedAnn.getType()))) {
				double distance = getDistance(ann, containedAnn);
				if (distance < bestDistance) {
					bestAnn = containedAnn;
					bestDistance = distance;
				}
			}
		}
		
		return bestAnn;
	}
	
	private double getDistance(Annotation a, Annotation b) {
		long startA = start(a);
		long endA = end(a);
		long startB = start(b);
		long endB = end(b);
		return startA >= endB ? startA - endB
				: endA <= startB ? startB - endA : 
					0.1 * Math.abs(startB - startA);
	}

	/**
	 * Get an appropriate protagonist for this annotation.
	 * @param ann
	 * @param entityType 
	 * @return
	 */
	private Annotation getProtagonist(Annotation ann, String entityType) {
		return protagonistMap.get(entityType);
	}

	private void match(Annotation feature, Annotation entity) {
		feature.getFeatures().put("feature-of", entity.getId());
	}

	private double relativePosition(Annotation ann) {
		return indexOf(ann, tokens) / (double)tokens.length;
	}
	
	private int indexOf(Annotation ann, Annotation[] annArr) {
		int l = 0, r = annArr.length-1;
		while (l <= r) {
			int mid = (l + r) / 2;
			long midStart = start(annArr[mid]);
			if (start(ann) > midStart) {
				if (end(ann) <= end(annArr[mid])) {
					return mid;
				} else {
					l = mid + 1;
				}
			} else if (start(ann) < midStart) {
				r = mid - 1; 
			} else {
				return end(ann) <= end(annArr[mid]) ? mid : -1;
			}
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see vn.com.epi.gate.sentiment.FeatureEntityMatcher#getEntityAnnotationTypes()
	 */
	@Override
	public List<String> getEntityAnnotationTypes() {
		return entityAnnotationTypes;
	}
	
	/* (non-Javadoc)
	 * @see vn.com.epi.gate.sentiment.FeatureEntityMatcher#setEntityAnnotationTypes(java.util.List)
	 */
	@Override
	public void setEntityAnnotationTypes(List<String> entityAnnotationTypes) {
		this.entityAnnotationTypes = entityAnnotationTypes;
	}

	/* (non-Javadoc)
	 * @see vn.com.epi.gate.sentiment.FeatureEntityMatcher#getInputASName()
	 */
	@Override
	public String getInputASName() {
		return inputASName;
	}
	
	/* (non-Javadoc)
	 * @see vn.com.epi.gate.sentiment.FeatureEntityMatcher#setInputASName(java.lang.String)
	 */
	@Override
	public void setInputASName(String name) {
		this.inputASName = name;
	}

	/* (non-Javadoc)
	 * @see vn.com.epi.gate.sentiment.FeatureEntityMatcher#getDocument()
	 */
	@Override
	public Document getDocument() {
		return document;
	}
	
	/* (non-Javadoc)
	 * @see vn.com.epi.gate.sentiment.FeatureEntityMatcher#setDocument(gate.Document)
	 */
	@Override
	public void setDocument(Document document) {
		this.document = document;
	}
	
	private static class Entity {
		private Annotation representative;
		private double score;
		
		public Entity(Annotation representative) {
			this.representative = representative;
		}
		
		public Annotation getRepresentative() {
			return representative;
		}
	}
	
}
