package vn.com.epi.gate.crfner.converter;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.util.GateException;
import gate.util.OffsetComparator;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Posted from May 26, 2018 2:40 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public class ConvertGateDoc2ConllNerFormat {
	private static final String INPUT_DOCUMENT_FOLDER_SCORE = "G:/VNNER/score";
	private static final String INPUT_DOCUMENT_FOLDER_GROUP1 = "G:/VNNER/group1";
	private static final String INPUT_DOCUMENT_FOLDER_GROUP2 = "G:/VNNER/group2";
	private static final String OUTPUT_DOCUMENT_FILE = "D:/stanford-ner-2014-01-04/VietnameseNerCorpusFull.tsv";

	private static final String PRODUCT_LABLE = "Product";
	private static final String LOCATION_LABLE = "Location";
	private static final String ORGANIZATION_LABLE = "Organization";
	private static final String PERSON_LABLE = "Person";
	private static final String FATURE_LABLE = "Feature";
	private static final String OPINION_LABLE = "Opinion expression";

	private static final Comparator<Annotation> comparator = new OffsetComparator();
	private static List<Document> keysDocument;

	/**
	 * @param args
	 * @throws GateException
	 * @throws IOException
	 */
	public static void main(String[] args) throws GateException, IOException {

		Gate.init();
		keysDocument = new ArrayList<Document>();
		convertDocument(OUTPUT_DOCUMENT_FILE);

	}

	private static void closeDocuments() {
		for (Document document : keysDocument) {
			Factory.deleteResource(document);
		}
	}

	private static void convertDocument(String outputDocURL)
			throws GateException, IOException {
		ArrayList<String> output = new ArrayList();

		File folder1 = new File(INPUT_DOCUMENT_FOLDER_SCORE);
		File folder2 = new File(INPUT_DOCUMENT_FOLDER_GROUP1);
		File folder3 = new File(INPUT_DOCUMENT_FOLDER_GROUP2);

		File[] files = folder1.listFiles();
		String annotationSetName = "consensus";
		// String annotationSetName = "";
		URL url;
		int limit = files.length;
		for (int j = 0; j < limit; j++) {
			// String nameFile = files[j].getName();
			url = files[j].toURI().toURL();
			Document doc = gate.Factory.newDocument(url, "utf-8");
			AnnotationSet productAnnotationSet = doc.getAnnotations(
					annotationSetName).get(PRODUCT_LABLE);
			AnnotationSet locationAnnotationSet = doc.getAnnotations(
					annotationSetName).get(LOCATION_LABLE);
			AnnotationSet organizationAnnotationSet = doc.getAnnotations(
					annotationSetName).get(ORGANIZATION_LABLE);
			AnnotationSet personAnnotationSet = doc.getAnnotations(
					annotationSetName).get(PERSON_LABLE);
			AnnotationSet featureAnnotationSet = doc.getAnnotations(
					annotationSetName).get(FATURE_LABLE);
			AnnotationSet opinionAnnotationSet = doc.getAnnotations(
					annotationSetName).get(OPINION_LABLE);
			AnnotationSet tokenAnnotationSet = doc.getAnnotations(
					annotationSetName).get("Token");

			List<Annotation> products = new ArrayList<Annotation>(
					productAnnotationSet);
			List<Annotation> locations = new ArrayList<Annotation>(
					locationAnnotationSet);
			List<Annotation> organizations = new ArrayList<Annotation>(
					organizationAnnotationSet);
			List<Annotation> persons = new ArrayList<Annotation>(
					personAnnotationSet);
			List<Annotation> features = new ArrayList<Annotation>(
					featureAnnotationSet);
			List<Annotation> opinions = new ArrayList<Annotation>(
					opinionAnnotationSet);

			List<Annotation> tokens = new ArrayList<Annotation>(
					tokenAnnotationSet);
			Collections.sort(tokens, comparator);

			for (int i = 0; i < tokens.size(); i++) {
				boolean notFinded = true;
				Annotation currentTokenAnotation = tokens.get(i);
				String currentString = gate.Utils.stringFor(doc,
						currentTokenAnotation).replace(" ", "_");
				if (currentString.contains("\n")) {
					System.out.println(currentString);
					System.out.println("----------");
				}
				if (!currentString.trim().equals("")) {
					for (int k = 0; k < products.size(); k++) {
						Annotation currentProductAnotation = products.get(k);
						if (currentProductAnotation
								.overlaps(currentTokenAnotation)) {
							String line = currentString + "\tProduct";
							output.add(line);
							notFinded = false;
							break;
						}
					}

					for (int k = 0; k < features.size(); k++) {
						Annotation currentProductAnotation = features.get(k);
						if (currentProductAnotation
								.overlaps(currentTokenAnotation)) {
							String line = currentString + "\tFeature";
							output.add(line);
							notFinded = false;
							break;
						}
					}

					for (int k = 0; k < opinions.size(); k++) {
						Annotation currentProductAnotation = opinions.get(k);
						if (currentProductAnotation
								.overlaps(currentTokenAnotation)) {
							String line = currentString + "\tOpinion";
							output.add(line);
							notFinded = false;
							break;
						}
					}
					for (int k = 0; k < organizations.size(); k++) {
						Annotation currentProductAnotation = organizations
								.get(k);
						if (currentProductAnotation
								.overlaps(currentTokenAnotation)) {
							String line = currentString + "\tOrganization";
							output.add(line);
							notFinded = false;
							break;
						}
					}
					for (int k = 0; k < locations.size(); k++) {
						Annotation currentProductAnotation = locations.get(k);
						if (currentProductAnotation
								.overlaps(currentTokenAnotation)) {
							String line = currentString + "\tLocation";
							output.add(line);
							notFinded = false;
							break;
						}
					}
					for (int k = 0; k < persons.size(); k++) {
						Annotation currentProductAnotation = persons.get(k);
						if (currentProductAnotation
								.overlaps(currentTokenAnotation)) {
							String line = currentString + "\tPerson";
							output.add(line);
							notFinded = false;
							break;
						}
					}

					if (notFinded) {
						String line = currentString + "\tO";
						output.add(line);
					}
				}
			}
			output.add(".\tO");
		}
		FileProcess outputFile = new FileProcess(OUTPUT_DOCUMENT_FILE);
		outputFile.save(output);
		closeDocuments();
	}

}
