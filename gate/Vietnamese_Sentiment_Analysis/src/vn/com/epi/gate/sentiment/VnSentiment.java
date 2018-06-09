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


/**
 * Posted from May 26, 2018 3:49 PM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public class VnSentiment extends AbstractLanguageAnalyser implements
		ProcessingResource {
	private static final long serialVersionUID = 1L;
	// private static final int weight = 1;
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



	// Get the input and output annotation sets


	// noi tat ca






	//


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
