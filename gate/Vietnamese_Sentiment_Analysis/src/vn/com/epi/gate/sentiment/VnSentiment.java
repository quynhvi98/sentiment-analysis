package vn.com.epi.gate.sentiment;

import static gate.Utils.stringFor;
import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.ProcessingResource;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.InvalidOffsetException;
import gate.util.OffsetComparator;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import vn.com.epi.gate.AnnotationOffsetComparator;

/**
 * Posted from May 26, 2018 3:49 PM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public class VnSentiment extends AbstractLanguageAnalyser implements
		ProcessingResource {
	private static final long serialVersionUID = 1L;
	// private static final int weight = 1;
	private static final AnnotationOffsetComparator ANNOTATION_OFFSET_COMPARATOR = new AnnotationOffsetComparator();
	private static final String PERSON_ANNOTATION_TYPE = "Person";
	private static final String PRODUCT_ANNOTATION_TYPE = "Product";
	private static final String TEMP_FEATURE_ANNOTATION_TYPE = "TempFeature";
	private static final String TEMP_OPINION_WORD_ANNOTATION_TYPE = "TempOpinion";
	private static final String TEMP_VALENCE_SHIFTER_ANNOTATION_TYPE = "TempValenceShifter";
	private static final boolean guessMainEntity = true;
	private URL opinionUrl;
	private String inputASName;
	private String outputASName;
	private PolarityGuesser polarityGuesser;
	private Annotation[] sentences;
	private Annotation mainProduct;
	private Annotation[] opinionWords;
	private AnnotationSet inputAS, outputAS;
	private List<Annotation> freedomFeature;
	private FeatureEntityMatcher featureEntityMatcher = new ProtagonistFeatureEntityMatcher();
	private final Comparator<Annotation> comparator = new OffsetComparator();

	// private List<Integer> matchedOfEntity;

	private Annotation feature, entity;

	public Resource init() throws ResourceInstantiationException {
		featureEntityMatcher
				.setEntityAnnotationTypes(Arrays.asList(new String[] {
						PRODUCT_ANNOTATION_TYPE, PERSON_ANNOTATION_TYPE,
						ORGANIZATION_ANNOTATION_TYPE }));
		featureEntityMatcher.init();
		return this;
	}

	public void execute() throws ExecutionException {
		polarityGuesser = new PolarityGuesser();
		freedomFeature = new ArrayList<Annotation>();
		mainProduct = null;
		try {
			try {
				polarityGuesser.loadModel(getOpinionUrl());
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
		} catch (IOException e) {
		}
		preprocess();
		if (sentences == null) {
			return;
		}
		try {
			matchOpinionFeature();
			if (guessMainEntity) {
				updateEntityForFreedomFeature();
			}
		} catch (InvalidOffsetException e) {
			e.printStackTrace();
		}
	}

	// Get the input and output annotation sets
	public void preprocess() {

		// matchedOfEntity = new ArrayList<Integer>();
		if (getInputASName() != null && getInputASName().equals("")) {
			setInputASName(null);
		}
		inputAS = (getInputASName() == null) ? document.getAnnotations()
				: document.getAnnotations(getInputASName());
		if (getOutputASName() != null && getOutputASName().equals("")) {
			setOutputASName(null);
		}
		outputAS = (getOutputASName() == null) ? document.getAnnotations()
				: document.getAnnotations(getOutputASName());

		AnnotationSet sentenceAnnotationSet = inputAS
				.get(SENTENCE_ANNOTATION_TYPE);
		AnnotationSet opinionAnnotationSet = inputAS
				.get(TEMP_OPINION_WORD_ANNOTATION_TYPE);

		if (sentenceAnnotationSet == null || sentenceAnnotationSet.isEmpty()) {
			sentences = null;
		} else {
			sentences = sentenceAnnotationSet
					.toArray(new Annotation[sentenceAnnotationSet.size()]);
			Arrays.sort(sentences, ANNOTATION_OFFSET_COMPARATOR);
		}

		opinionWords = opinionAnnotationSet
				.toArray(new Annotation[opinionAnnotationSet.size()]);
		Arrays.sort(opinionWords, ANNOTATION_OFFSET_COMPARATOR);
	}

	// noi tat ca
	private void matchOpinionFeature() throws InvalidOffsetException {
		for (int i = 0; i < sentences.length; i++) {
			AnnotationSet annotationBetween = document.getAnnotations()
					.getContained(sentences[i].getStartNode().getOffset(),
							sentences[i].getEndNode().getOffset());

			AnnotationSet productBetween = annotationBetween
					.get(PRODUCT_ANNOTATION_TYPE);

			AnnotationSet featureBetween = annotationBetween
					.get(TEMP_FEATURE_ANNOTATION_TYPE);
			AnnotationSet tempOpinionBetween = annotationBetween
					.get(TEMP_OPINION_WORD_ANNOTATION_TYPE);
			AnnotationSet valenshifterBetween = annotationBetween
					.get(TEMP_VALENCE_SHIFTER_ANNOTATION_TYPE);

			List<Annotation> tokens = new ArrayList<Annotation>(
					annotationBetween.get("Token"));
			Collections.sort(tokens, comparator);

			List<Integer> heads = headOfTokens(tokens);

			for (Annotation tempOpinion : tempOpinionBetween) {
				if (annotationMatchWithAnnotation(tempOpinion, featureBetween) == null) {

					int indexOfOpinion = getHeadTokenOfAnnotation(tempOpinion,
							tokens);

					if (indexOfOpinion != -1) {
						int indexOfMainTokenOpinion = indexOfOpinion + 1;

						Annotation mainTokenOfOpinion = tokens
								.get(indexOfOpinion);

						Annotation tempValenceShifter = determineValenceShifterOfTempOpinion(
								tempOpinion, mainTokenOfOpinion,
								indexOfMainTokenOpinion, tokens,
								valenshifterBetween, heads);

						Annotation opinion = determineEntityAndFeatureOfOpinion(
								tempOpinion, indexOfMainTokenOpinion, tokens,
								productBetween, featureBetween);

						if (opinion != null && tempValenceShifter != null) {
							FeatureMap spaceFmap = Factory.newFeatureMap();
							spaceFmap.put("kind", "valenceshifter");
							spaceFmap.put("direction", tempValenceShifter
									.getFeatures().get("direction"));
							spaceFmap.put("shifter-of", opinion.getId());
							spaceFmap.put("position", tempValenceShifter
									.getFeatures().get("position"));
							addOverlappingAnnotation(
									tempValenceShifter.getStartNode()
											.getOffset(),
									tempValenceShifter.getEndNode().getOffset(),
									spaceFmap, "Valence shifter");
						}
						// }
					}
				}
			}
		}
	}

	// lay ra token head của Opinion
	private int getHeadTokenOfAnnotation(Annotation annot,
										 List<Annotation> tokens) {
		int limit = tokens.size();
		List<Annotation> tokenInner = new ArrayList();
		ArrayList<Integer> tokenIndex = new ArrayList();
		for (int i = 0; i < limit; i++) {
			Annotation token = tokens.get(i);
			if (annot.overlaps(token)) {
				tokenInner.add(token);
				tokenIndex.add(i);
			}
		}
		if (tokenInner.size() > 0) {
			List<Integer> headOfTokens = headOfTokens(tokenInner);
			for (int i = 0; i < tokenInner.size(); i++) {
				if (!tokenIndex.contains(headOfTokens.get(i))
						&& headOfTokens.contains(tokenIndex.get(i))) {
					return tokenIndex.get(i);
				}
			}
			for (int i = 0; i < tokenInner.size(); i++) {
				if (headOfTokens.contains(tokenIndex.get(i))) {
					return tokenIndex.get(i);
				}
			}
			for (int i = 0; i < tokenInner.size(); i++) {
				if (!tokenIndex.contains(headOfTokens.get(i))) {
					return tokenIndex.get(i);
				}
			}
			// not jump together
		}
		return tokenIndex.get(0);
	}

	// lay head cua chuoi token
	private List<Integer> headOfTokens(List<Annotation> tokens) {
		List<Integer> heads = new ArrayList<Integer>();
		for (Annotation token : tokens) {
			heads.add(Integer
					.parseInt((String) token.getFeatures().get("head")));
		}
		return heads;
	}

	// tim ra các con trỏ tới head
	private List<Integer> indexOfTokenDependencyHead(List<Integer> heads,
													 int head) {
		List<Integer> indexs = new ArrayList<Integer>();
		int limit = heads.size();
		for (int i = 0; i < limit; i++) {
			if (heads.get(i) == head) {
				indexs.add(i);
			}
		}
		return indexs;
	}

	// bo di opinion long voi feature
	public Integer addOverlappingAnnotation(long start, long end,
											FeatureMap fMap, String type) {
		Integer id = null;
		AnnotationSet oldAnnotationSet = outputAS.get(type, start, end);
		if (oldAnnotationSet.size() == 0) {
			try {
				return outputAS.add(start, end, type, fMap);
			} catch (InvalidOffsetException e) {
				throw new RuntimeException("Impossible exception", e);
			}
		} else {
			id = oldAnnotationSet.iterator().next().getId();
		}
		return id;
	}

	// lay valenceshifter của Opinion
	private Annotation determineValenceShifterOfTempOpinion(
			Annotation tempOpinion, Annotation token, int head,
			List<Annotation> tokens, AnnotationSet valenshifterBetween,
			List<Integer> heads) {

		List<Integer> indexOfHead = indexOfTokenDependencyHead(heads, head);

		for (int index : indexOfHead) {
			if (((String) tokens.get(index).getFeatures().get("category"))
					.charAt(0) == 'R') {

				Annotation tempValenceShifter = annotationMatchWithAnnotation(
						tokens.get(index), valenshifterBetween);
				if (tempValenceShifter != null
						&& !tempOpinion.overlaps(tempValenceShifter)) {
					return tempValenceShifter;
				}
			}
		}

		int tempHead = Integer.parseInt((String) token.getFeatures()
				.get("head"));
		if (tempHead != 0) {
			indexOfHead = indexOfTokenDependencyHead(heads, tempHead);
			for (int index : indexOfHead) {
				if (((String) tokens.get(index).getFeatures().get("category"))
						.charAt(0) == 'R') {
					Annotation tempValenceShifter = annotationMatchWithAnnotation(
							tokens.get(index), valenshifterBetween);
					if (tempValenceShifter != null
							&& !tempOpinion.overlaps(tempValenceShifter)) {
						return tempValenceShifter;
					}
				}
			}
		}
		return null;
	}

	//
	private Annotation determineEntityAndFeatureOfOpinion(
			Annotation tempOpinion, int indexOfMainTokenOpinion,
			List<Annotation> tokens, AnnotationSet productBetween,
			AnnotationSet featureBetween) throws InvalidOffsetException {
		entity = null;
		feature = null;

		// tim feature cho opinion
		List<Annotation> features = new ArrayList<Annotation>(featureBetween);
		int featureMatchedIndex = -1;
		int shortestPath = tokens.size();

		for (int i = 0; i < features.size(); i++) {
			int pathLeng = getShortestPath(
					getHeadTokenOfAnnotation(features.get(i), tokens),
					indexOfMainTokenOpinion, tokens);
			if (pathLeng < shortestPath) {
				shortestPath = pathLeng;
				featureMatchedIndex = i;
			}
		}

		if (featureMatchedIndex != -1) {
			feature = features.get(featureMatchedIndex);
			// tim entity cho feature
			List<Annotation> products = new ArrayList<Annotation>(
					productBetween);
			int featureIndex = getHeadTokenOfAnnotation(feature, tokens);
			int entityMatchedIndex = -1;
			shortestPath = tokens.size();
			for (int i = 0; i < products.size(); i++) {
				int pathLeng = getShortestPath(
						getHeadTokenOfAnnotation(products.get(i), tokens),
						featureIndex, tokens);
				if (pathLeng < shortestPath) {
					shortestPath = pathLeng;
					entityMatchedIndex = i;
				}
			}

			if (entityMatchedIndex != -1) {
				entity = products.get(entityMatchedIndex);
				return addOpinionFeatureAndEntity(tempOpinion);
			} else {
				return addOpinionFeatureAndEntity(tempOpinion);
			}
		} else {
			// tim entity cho opinion
			List<Annotation> products = new ArrayList<Annotation>(
					productBetween);
			int entityMatchedIndex = -1;
			shortestPath = tokens.size();
			for (int i = 0; i < products.size(); i++) {
				int pathLeng = getShortestPath(
						getHeadTokenOfAnnotation(products.get(i), tokens),
						indexOfMainTokenOpinion, tokens);
				if (pathLeng < shortestPath) {
					shortestPath = pathLeng;
					entityMatchedIndex = i;
				}
			}

			if (entityMatchedIndex != -1) {
				entity = products.get(entityMatchedIndex);
				return addOpinionFeatureAndEntity(tempOpinion);
			} else {
				return null;
			}

		}
	}

	private int getShortestPath(int index1, int index2, List<Annotation> tokens) {
		ArrayList<Integer> partToRoot1 = new ArrayList();
		ArrayList<Integer> partToRoot2 = new ArrayList();
		int max = tokens.size();
		int count = 0;
		int currentRoot = index1 + 1;
		while (count++ < max) {
			partToRoot1.add(currentRoot);
			int currentHead = Integer.parseInt((String) tokens
					.get(currentRoot - 1).getFeatures().get("head"));
			if (currentHead == 0) {
				partToRoot1.add(currentHead);
				break;
			}
			currentRoot = currentHead;
		}
		count = 0;
		currentRoot = index2 + 1;
		while (count++ < max) {
			partToRoot2.add(currentRoot);
			int currentHead = Integer.parseInt((String) tokens
					.get(currentRoot - 1).getFeatures().get("head"));
			if (currentHead == 0) {
				partToRoot2.add(currentHead);
				break;
			}
			currentRoot = currentHead;
		}
		int maxSameLeng = 0;
		max = partToRoot1.size() > partToRoot2.size() ? partToRoot2.size()
				: partToRoot1.size();
		for (int i = 0; i < max; i++) {
			if (partToRoot1.get(partToRoot1.size() - i - 1) == partToRoot2
					.get(partToRoot2.size() - i - 1)) {
				maxSameLeng++;
			}
		}

		return ((partToRoot1.size() + partToRoot2.size()) - 2 * maxSameLeng);
	}

	private Annotation addOpinionFeatureAndEntity(Annotation tempOpinion) {

		FeatureMap opinionFmap = Factory.newFeatureMap();
		opinionFmap.put("kind", "Opinion expression");
		int id = addOverlappingAnnotation(tempOpinion.getStartNode()
						.getOffset(), tempOpinion.getEndNode().getOffset(),
				opinionFmap, "Opinion expression");
		Annotation opinion = document.getAnnotations().get(id);

		if (feature != null) {
			FeatureMap featureFmap = Factory.newFeatureMap();
			featureFmap.put("kind", "feature");
			id = addOverlappingAnnotation(feature.getStartNode().getOffset(),
					feature.getEndNode().getOffset(), featureFmap, "Feature");
			Annotation featureOfOpinion = document.getAnnotations().get(id);
			if (entity == null) {
				if (featureOfOpinion.getFeatures().get("feature-of") == null) {
					freedomFeature.add(featureOfOpinion);
				}
			} else {
				// ArrayList<Integer> matches = (ArrayList<Integer>) entity
				// .getFeatures().get("matches");
				// FeatureMap entityFmap = Factory.newFeatureMap();
				// entityFmap.put("kind", "entity");
				// int idEntity = addOverlappingAnnotation(entity.getStartNode()
				// .getOffset(), entity.getEndNode().getOffset(),
				// entityFmap, "Entity");
				//
				// if (matches != null) {
				// int index = matchedOfEntity.indexOf(matches.get(0));
				// if (index == -1) {
				// matchedOfEntity.add(idEntity);
				// matches.set(0, idEntity);
				// }
				// document.getAnnotations().get(idEntity).getFeatures()
				// .put("matches", matches);
				// }
				// Annotation tempProduct;
				// if (matches != null) {
				//
				// tempProduct = document.getAnnotations().get(
				// (Integer) matches.get(0));
				// } else {
				// tempProduct = document.getAnnotations().get(idEntity);
				// }
				Integer numberRepeatOfEntity = (Integer) entity.getFeatures()
						.get("numberRepeat");
				if (numberRepeatOfEntity == null) {
					numberRepeatOfEntity = 0;
				} else {
					numberRepeatOfEntity += 1;
				}
				entity.getFeatures().put("numberRepeat", numberRepeatOfEntity);
				if (mainProduct != entity) {
					if (mainProduct == null) {
						mainProduct = entity;
					} else {
						if ((Integer) mainProduct.getFeatures().get(
								"numberRepeat") < numberRepeatOfEntity) {
							mainProduct = entity;
						}
					}
				}
				featureOfOpinion.getFeatures()
						.put("feature-of", entity.getId());
			}
			opinion.getFeatures().put("opinion-of", featureOfOpinion.getId());
			String polarity = polarityGuesser.guess(
					stringFor(document, opinion),
					stringFor(document, featureOfOpinion));
			if (polarity != null) {
				opinion.getFeatures().put("polarity", polarity);
			}
		} else {
			if (entity != null) {
				// ArrayList<Integer> matches = (ArrayList<Integer>) entity
				// .getFeatures().get("matches");
				//
				// FeatureMap spaceFmap = Factory.newFeatureMap();
				// spaceFmap.put("kind", "entity");
				// int idEntity = addOverlappingAnnotation(entity.getStartNode()
				// .getOffset(), entity.getEndNode().getOffset(),
				// spaceFmap, "Entity");
				//
				// if (matches != null) {
				// int index = matchedOfEntity.indexOf((Integer) matches
				// .get(0));
				// if (index == -1) {
				// matchedOfEntity.add(idEntity);
				// matches.set(0, idEntity);
				// }
				// document.getAnnotations().get(idEntity).getFeatures()
				// .put("matches", matches);
				// }
				//
				// Annotation tempProduct;
				// if (matches != null) {
				//
				// tempProduct = document.getAnnotations().get(
				// (Integer) matches.get(0));
				// } else {
				// tempProduct = document.getAnnotations().get(idEntity);
				// }
				Integer numberRepeatOfEntity = (Integer) entity.getFeatures()
						.get("numberRepeat");
				if (numberRepeatOfEntity == null) {
					numberRepeatOfEntity = 0;
				} else {
					numberRepeatOfEntity++;
				}
				entity.getFeatures().put("numberRepeat", numberRepeatOfEntity);
				if (mainProduct != entity) {
					if (mainProduct == null) {
						mainProduct = entity;
					} else {
						if ((Integer) mainProduct.getFeatures().get(
								"numberRepeat") < numberRepeatOfEntity) {
							mainProduct = entity;
						}
					}
				}
				opinion.getFeatures().put("opinion-of", entity.getId());
				String polarity = polarityGuesser.guess(
						stringFor(document, opinion),
						stringFor(document, entity));
				if (polarity != null) {
					opinion.getFeatures().put("polarity", polarity);
				}
			}
		}
		return opinion;
	}

	private Annotation annotationMatchWithAnnotation(Annotation tempOpinion,
													 AnnotationSet tempFeatureSet) {
		for (Annotation annotation : tempFeatureSet) {
			if (annotation.overlaps(tempOpinion)) {
				return annotation;
			}
		}
		return null;
	}

	// not use
	private Annotation findMainEntity(AnnotationSet annotationSet) {
		ArrayList<Annotation> listOfDiffEntity = new ArrayList<Annotation>();
		ArrayList<Integer> quantityOfDiffEntity = new ArrayList<Integer>();
		ArrayList<String> stringOfDiffEntity = new ArrayList<String>();
		for (Annotation annotation : annotationSet) {
			String entityString = gate.Utils.stringFor(document, annotation);
			int index = stringOfDiffEntity.indexOf(entityString);
			if (index != -1) {
				// annotation was already in listOfDiffEntity
				quantityOfDiffEntity.set(index,
						quantityOfDiffEntity.get(index) + 1);
			} else {
				// annotation not in listOfDiffEntity
				listOfDiffEntity.add(annotation);
				quantityOfDiffEntity.add(1);
				stringOfDiffEntity.add(entityString);
			}
		}
		int indexOfMainEntity = 0;
		int maxOfQuantity = 0;
		for (int i = 0; i < quantityOfDiffEntity.size(); i++) {
			if (quantityOfDiffEntity.get(i) > maxOfQuantity) {
				maxOfQuantity = quantityOfDiffEntity.get(i);
				indexOfMainEntity = i;
			}
		}
		return listOfDiffEntity.get(indexOfMainEntity);
	}

	private void updateEntityForFreedomFeature() {
		if (mainProduct != null) {
			for (Annotation feature : freedomFeature) {
				if (feature.getFeatures().get("feature-of") == null) {
					feature.getFeatures()
							.put("feature-of", mainProduct.getId());
				}
			}
		}
	}

	/**
	 * @return the inputASName
	 */
	public String getInputASName() {
		return inputASName;
	}

	/**
	 * @param inputASName
	 *            the inputASName to set
	 */
	public void setInputASName(String inputASName) {
		this.inputASName = inputASName;
	}

	/**
	 * @return the outputASName
	 */
	public String getOutputASName() {
		return outputASName;
	}

	/**
	 * @param outputASName
	 *            the outputASName to set
	 */
	public void setOutputASName(String outputASName) {
		this.outputASName = outputASName;
	}

	/**
	 * @return the opinionUrl
	 */
	public URL getOpinionUrl() {
		return opinionUrl;
	}

	/**
	 * @param opinionUrl
	 *            the opinionUrl to set
	 */
	public void setOpinionUrl(URL opinionUrl) {
		this.opinionUrl = opinionUrl;
	}
}
