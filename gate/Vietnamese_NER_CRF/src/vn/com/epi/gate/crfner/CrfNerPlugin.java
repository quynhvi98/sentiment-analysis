package vn.com.epi.gate.crfner;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleResource;
import gate.util.InvalidOffsetException;
import gate.util.OffsetComparator;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Posted from May 26, 2018 2:00 PM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
@CreoleResource(name = "Crf-Ner PR", comment = "processing resource")
public class CrfNerPlugin extends AbstractLanguageAnalyser {

	private static final long serialVersionUID = 1L;

	private CrfNerService service = null;
	private URL dirOfModel;
	private String nameOfModel;

	private static final String PRODUCT_LABLE = "Product";
	private static final String LOCATION_LABLE = "Location";
	private static final String ORGANIZATION_LABLE = "Organization";
	private static final String PERSON_LABLE = "Person";
	private static final String FEATURE_LABLE = "TempFeature";

	private static final Comparator<Annotation> comparator = new OffsetComparator();

	public Resource init() throws ResourceInstantiationException {
		File modelFile = new File(dirOfModel.getPath().replace("%20", " "));
		service = new CrfNerService(
				(new File(modelFile.toURI()).getAbsolutePath()).replace("\\",
						"/") + "/" + nameOfModel);
		service.init();
		return this;
	}

	public void execute() throws ExecutionException {

		AnnotationSet outputAS = document.getAnnotations("");

		AnnotationSet tokenAnnotationSet = document.getAnnotations("").get(
				"Token");

		List<Annotation> tokens = new ArrayList<Annotation>(tokenAnnotationSet);
		Collections.sort(tokens, comparator);

		String docString = convertToString(tokens);
		String output = service.parserToString(docString);
		String[] postProcessTokens = findMoreEntity(output).split(" ");
		for (int i = 0; i < postProcessTokens.length; i++) {
			int startToken, endToken;
			if (postProcessTokens[i].endsWith(PRODUCT_LABLE)) {
				startToken = i;
				do {
					endToken = i++;
				} while (i < postProcessTokens.length
						&& (postProcessTokens[i].endsWith(PRODUCT_LABLE) || (isContentNumber(postProcessTokens[i]))));
				try {
					FeatureMap fmap = Factory.newFeatureMap();
					fmap.put("kind", PRODUCT_LABLE);
					outputAS.add((long) tokens.get(startToken).getStartNode()
							.getOffset(), (long) tokens.get(endToken)
							.getEndNode().getOffset(), PRODUCT_LABLE, fmap);
				} catch (InvalidOffsetException e) {
					e.printStackTrace();
				}
			} else if (postProcessTokens[i].endsWith(LOCATION_LABLE)) {
				startToken = i;
				do {
					endToken = i++;
				} while (i < postProcessTokens.length
						&& postProcessTokens[i].endsWith(LOCATION_LABLE));
				try {
					FeatureMap fmap = Factory.newFeatureMap();
					fmap.put("kind", LOCATION_LABLE);
					outputAS.add((long) tokens.get(startToken).getStartNode()
							.getOffset(), (long) tokens.get(endToken)
							.getEndNode().getOffset(), LOCATION_LABLE, fmap);
				} catch (InvalidOffsetException e) {
					e.printStackTrace();
				}
			} else if (postProcessTokens[i].endsWith(ORGANIZATION_LABLE)) {
				startToken = i;
				do {
					endToken = i++;
				} while (i < postProcessTokens.length
						&& postProcessTokens[i].endsWith(ORGANIZATION_LABLE));
				try {
					FeatureMap fmap = Factory.newFeatureMap();
					fmap.put("kind", ORGANIZATION_LABLE);
					outputAS.add((long) tokens.get(startToken).getStartNode()
							.getOffset(), (long) tokens.get(endToken)
							.getEndNode().getOffset(), ORGANIZATION_LABLE, fmap);
				} catch (InvalidOffsetException e) {
					e.printStackTrace();
				}
			} else if (postProcessTokens[i].endsWith(PERSON_LABLE)) {
				startToken = i;
				do {
					endToken = i++;
				} while (i < postProcessTokens.length
						&& postProcessTokens[i].endsWith(PERSON_LABLE));
				try {
					FeatureMap fmap = Factory.newFeatureMap();
					fmap.put("kind", PERSON_LABLE);
					outputAS.add((long) tokens.get(startToken).getStartNode()
							.getOffset(), (long) tokens.get(endToken)
							.getEndNode().getOffset(), PERSON_LABLE, fmap);
				} catch (InvalidOffsetException e) {
					e.printStackTrace();
				}
			} else if (postProcessTokens[i].endsWith(FEATURE_LABLE)) {
				startToken = i;
				do {
					endToken = i++;
				} while (i < postProcessTokens.length
						&& (postProcessTokens[i].endsWith(FEATURE_LABLE) || (isContentNumber(postProcessTokens[i]))));
				try {
					FeatureMap fmap = Factory.newFeatureMap();
					fmap.put("kind", FEATURE_LABLE);
					outputAS.add((long) tokens.get(startToken).getStartNode()
							.getOffset(), (long) tokens.get(endToken)
							.getEndNode().getOffset(), FEATURE_LABLE, fmap);
				} catch (InvalidOffsetException e) {
					e.printStackTrace();
				}
			}
		}

	}

	private String convertToString(List<Annotation> tokens) {
		StringBuilder output = new StringBuilder();
		for (int i = 0; i < tokens.size(); i++) {
			Annotation currentTokenAnotation = tokens.get(i);
			String currentString = gate.Utils.stringFor(document,
					currentTokenAnotation).replace(" ", "_");

			output.append(currentString + " ");
		}
		return output.toString();
	}

	private boolean isContentNumber(String input) {
		if (input.matches(".*\\d.*")) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean isUpperLetter(String input) {
		boolean hasUppercase = !input.equals(input.toLowerCase());
		return hasUppercase;
	}

	private String findMoreEntity(String parsedString) {
		ArrayList<String> entityKnown = new ArrayList();
		String[] postProcessTokens = parsedString.split(" ");
		for (int i = 0; i < postProcessTokens.length; i++) {
			int startToken, endToken;
			StringBuilder currentEntity = new StringBuilder();
			StringBuilder currentEntityNoTag = new StringBuilder();

			if (postProcessTokens[i].endsWith(PRODUCT_LABLE)) {
				startToken = i;
				do {
					endToken = i++;
				} while (i < postProcessTokens.length
						&& postProcessTokens[i].endsWith(PRODUCT_LABLE));
				for (int j = startToken; j <= endToken; j++) {
					currentEntity.append(postProcessTokens[j] + " ");
					currentEntityNoTag
							.append(postProcessTokens[j].split("/")[0] + " ");

				}
				if (isUpperLetter(currentEntityNoTag.toString())
						&& !entityKnown.contains(currentEntity.toString()
								.trim())) {
					entityKnown.add(currentEntity.toString().trim());
				}

			} else if (postProcessTokens[i].endsWith(LOCATION_LABLE)) {
				startToken = i;
				do {
					endToken = i++;
				} while (i < postProcessTokens.length
						&& postProcessTokens[i].endsWith(LOCATION_LABLE));
				for (int j = startToken; j <= endToken; j++) {
					currentEntity.append(postProcessTokens[j] + " ");
					currentEntityNoTag
							.append(postProcessTokens[j].split("/")[0] + " ");

				}
				if (isUpperLetter(currentEntityNoTag.toString())
						&& !entityKnown.contains(currentEntity.toString()
								.trim())) {
					entityKnown.add(currentEntity.toString().trim());
				}
			} else if (postProcessTokens[i].endsWith(ORGANIZATION_LABLE)) {
				startToken = i;
				do {
					endToken = i++;
				} while (i < postProcessTokens.length
						&& postProcessTokens[i].endsWith(ORGANIZATION_LABLE));
				for (int j = startToken; j <= endToken; j++) {
					currentEntity.append(postProcessTokens[j] + " ");
					currentEntityNoTag
							.append(postProcessTokens[j].split("/")[0] + " ");

				}
				if (isUpperLetter(currentEntityNoTag.toString())
						&& !entityKnown.contains(currentEntity.toString()
								.trim())) {
					entityKnown.add(currentEntity.toString().trim());
				}
			} else if (postProcessTokens[i].endsWith(PERSON_LABLE)) {
				startToken = i;
				do {
					endToken = i++;
				} while (i < postProcessTokens.length
						&& postProcessTokens[i].endsWith(PERSON_LABLE));
				for (int j = startToken; j <= endToken; j++) {
					currentEntity.append(postProcessTokens[j] + " ");
					currentEntityNoTag
							.append(postProcessTokens[j].split("/")[0] + " ");

				}
				if (isUpperLetter(currentEntityNoTag.toString())
						&& !entityKnown.contains(currentEntity.toString()
								.trim())) {
					entityKnown.add(currentEntity.toString().trim());
				}
			} else if (postProcessTokens[i].endsWith(FEATURE_LABLE)) {
				startToken = i;
				do {
					endToken = i++;
				} while (i < postProcessTokens.length
						&& postProcessTokens[i].endsWith(FEATURE_LABLE));
				for (int j = startToken; j <= endToken; j++) {
					currentEntity.append(postProcessTokens[j] + " ");
					currentEntityNoTag
							.append(postProcessTokens[j].split("/")[0] + " ");

				}
				if (isUpperLetter(currentEntityNoTag.toString())
						&& !entityKnown.contains(currentEntity.toString()
								.trim())) {
					entityKnown.add(currentEntity.toString().trim());
				}
			}
		}

		for (int i = 0; i < entityKnown.size(); i++) {
			String tag = entityKnown.get(i).split(" ")[0].split("/")[1];
			parsedString = parsedString.replaceAll(entityKnown.get(i)
					.replaceAll("/" + tag, "/O"), entityKnown.get(i));
		}
		return parsedString;
	}

	/**
	 * @return the dirOfModel
	 */
	public URL getDirOfModel() {
		return dirOfModel;
	}

	/**
	 * @param dirOfModel
	 *            the dirOfModel to set
	 */
	public void setDirOfModel(URL dirOfModel) {
		this.dirOfModel = dirOfModel;
	}

	/**
	 * @return the nameOfModel
	 */
	public String getNameOfModel() {
		return nameOfModel;
	}

	/**
	 * @param nameOfModel
	 *            the nameOfModel to set
	 */
	public void setNameOfModel(String nameOfModel) {
		this.nameOfModel = nameOfModel;
	}

	public static void main(String[] args) {
		System.out.println("Duong/O".endsWith("/O"));
	}
}
