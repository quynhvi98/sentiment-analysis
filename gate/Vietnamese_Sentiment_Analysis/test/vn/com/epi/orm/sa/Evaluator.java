package vn.com.epi.orm.sa;

/**
 * Posted from May 30, 2018 11:13 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.CorpusController;
import gate.Document;
import gate.Factory;
import gate.Gate;
import gate.Utils;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import vn.com.epi.gate.refdiff.AbstractMeasuresRow;
import vn.com.epi.gate.refdiff.ReferenceAnnotationDiffer;

public class Evaluator {
	private static final String APP_PATH = "dist/gate/SentimentAnalysisPipeline.gapp";
	private static final String DOCUMENT_FOLDER = "G:/TQT_Documents/annotations-final";

	private static CorpusController application;
	private static List<Document> keysDocument;
	private static FileWriter writer;
	private static List<Document> responsesDocument;
	private static int size = 0;
	private static List<String> score;
	private static List<String> nameAnnotationSet;
	private static StopWatch stopWatch = null;
	private static int numberOfCharacter = 0;
	private static long timeLoadModel;
	private static long timeSum;
	private static long timeProcessDocument;

	/**
	 * @param args
	 * @throws GateException
	 * @throws IOException
	 */
	public static void main(String[] args) throws GateException, IOException {

		stopWatch = new StopWatch();
		stopWatch.start();
		Gate.init();
		application = (CorpusController) PersistenceManager
				.loadObjectFromFile(new File(APP_PATH));
		stopWatch.stop();
		timeLoadModel = stopWatch.getTime();
		System.out.println("Time load model: " + timeLoadModel + " (ms)");
		keysDocument = new ArrayList<Document>();
		responsesDocument = new ArrayList<Document>();
		score = new ArrayList<String>();
		nameAnnotationSet = new ArrayList<String>();
		File folder = new File(DOCUMENT_FOLDER);
		File[] filesFolder = folder.listFiles();
		for (int i = 2; i == 2; i--) {
			File[] files = filesFolder[i].listFiles();
			loadDocument(files, i);
		}
		timeSum = stopWatch.getTime();
		timeProcessDocument = (timeSum - timeLoadModel);
		System.out.println("Time process all document: " + timeProcessDocument);
		System.out.println("Time per document: " + timeProcessDocument / size);
		// save result test to file.
		writeResultTest();

		writer = new FileWriter("output.csv");
		writer.append("FileName, Type, Correct, Partially, Missing, Spurious, Recall, Precision, F1\n");
		// normalEvaluate();
		relationEvaluate();
		writer.flush();
		writer.close();

		closeDocuments();
		Factory.deleteResource(application);
		System.out.println("finish");
	}

	private static void closeDocuments() {
		for (Document document : keysDocument) {
			Factory.deleteResource(document);
		}
		for (Document document : responsesDocument) {
			Factory.deleteResource(document);
		}
	}

	private static void normalEvaluate() {
		int correctDoc = 0;
		double P1, P2, R1, R2;
		P1 = P2 = R1 = R2 = 0;
		for (int i = 0; i < size; i++) {
			AnnotationSet keyProducts = keysDocument.get(i)
					.getAnnotations("consensus").get("Product");
			AnnotationSet responseProducts = responsesDocument.get(i)
					.getAnnotations("").get("Product");
			ArrayList<String> productMatchedName = new ArrayList();
			ArrayList<String> productKeyName = getDistinctProductName(
					keysDocument.get(i), keyProducts);
			ArrayList<String> productResName = getDistinctProductName(
					responsesDocument.get(i), responseProducts);
			// int maxSize = productKeyName.size() > productResName.size() ?
			// productKeyName
			// .size() : productResName.size();
			for (int j = 0; j < productKeyName.size(); j++) {
				System.out.println(productKeyName.get(j));
			}
			for (int j = 0; j < productResName.size(); j++) {
				if (productKeyName.contains(productResName.get(j))) {
					productMatchedName.add(productResName.get(j));
				}
			}
			if (productKeyName.size() != 0 && productResName.size() != 0) {
				correctDoc++;
				P1 += (double) productMatchedName.size()
						/ (double) productResName.size();
				R1 += (double) productMatchedName.size()
						/ (double) productKeyName.size();
			}

		}
		System.out.println("Precision 1: " + P1 / correctDoc);
		System.out.println("Recall 1: " + R1 / correctDoc);

	}

	private static boolean isUpperLetter(String input) {
		boolean hasUppercase = !input.equals(input.toLowerCase());
		return hasUppercase;
	}

	private static ArrayList<String> getDistinctProductName(Document doc,
			AnnotationSet products) {

		ArrayList<String> productNames = new ArrayList();
		for (Annotation product : products) {
			String name = Utils.stringFor(doc, product).replace("\n", "")
					.trim();
			if (!productNames.contains(name) && isUpperLetter(name)) {
				// && !name.contains("Máy") && !name.contains("Nó")&&
				// !name.contains("Chiếc")) {
				productNames.add(name);
			}
		}
		return productNames;
	}

	/**
	 * 
	 */
	private static void relationEvaluate() {

		List<String> types = new ArrayList<String>();
		Set<String> features = new HashSet<String>();
		// String[] measures = { "Strict", "Average", "Lenient" };
		types.add("Product");
		// types.add("Valence shifter");
		types.add("Opinion expression");
		// types.add("Feature");
		features.add("polarity");
		// features.add("direction");
		System.out.println("select reference? y/n");
		Scanner scanner = new Scanner(System.in);
		String in = scanner.nextLine();
		scanner.close();
		if (in.equals("y")) {
			features.add("feature-of");
			features.add("opinion-of");
			features.add("shifter-of");
		}

		// for (int j = 0; j < 3; j++) {
		HashMap<String, ReferenceAnnotationDiffer> differsByType = new HashMap<String, ReferenceAnnotationDiffer>();
		Double precision = 0.0;
		Double recall = 0.0;
		Double F1 = 0.0;
		for (int i = 0; i < size; i++) {
			Set<Annotation> featureAnnotation = new HashSet<Annotation>();
			Set<Annotation> keys = new HashSet<Annotation>();
			Set<Annotation> responses = new HashSet<Annotation>();
			Set<Annotation> keysIter = new HashSet<Annotation>();
			Set<Annotation> responseIter = new HashSet<Annotation>();
			keys = keysDocument.get(i).getAnnotations(nameAnnotationSet.get(i));
			responses = responsesDocument.get(i).getAnnotations("");
			ReferenceAnnotationDiffer differ;
			HashMap<String, ReferenceAnnotationDiffer> differsByTypeTemp = new HashMap<String, ReferenceAnnotationDiffer>();
			for (String type : types) {
				responseIter = ((AnnotationSet) responses).get(type);
				keysIter = ((AnnotationSet) keys).get(type);
				if (type.equals("Opinion expression")) {
					keysIter = ((AnnotationSet) keys).get(type);
					for (Annotation opinion : keysIter) {
						try {
							Annotation annotTemp = (Annotation) keysDocument
									.get(i)
									.getAnnotations(nameAnnotationSet.get(i))
									.get((Integer) opinion.getFeatures().get(
											"opinion-of"));
							if (annotTemp.getType().equals("Feature")) {
								featureAnnotation.add(annotTemp);
							}
						} catch (NullPointerException e) {

						}
					}
				} else if (type.equals("Feature")) {
					keysIter = featureAnnotation;
				} else {
					keysIter = ((AnnotationSet) keys).get(type);
				}
				differ = new ReferenceAnnotationDiffer();
				differ.setKeyDocument(keysDocument.get(i));
				differ.setResDocument(responsesDocument.get(i));
				differ.setSignificantFeaturesSet(features);
				differ.setReferenceSet("Feature:feature-of;Opinion expression:opinion-of;Valence shifter:shifter-of");
				System.out.println("i = " + i);
				differ.calculateDiff(keysIter, responseIter);
				differsByTypeTemp.put(type, differ);
				differsByType.put(type + i, differ);
			}
			differ = new ReferenceAnnotationDiffer(differsByTypeTemp.values());
			AbstractMeasuresRow measuresRow = new AbstractMeasuresRow("", 1);
			differ.getMeasuresRow(measuresRow);
			precision += measuresRow.getPrecision();
			recall += measuresRow.getRecall();
			F1 += measuresRow.getF1();
			writeToFile(toString(measuresRow, keysDocument.get(i).getName()));
		}
		ReferenceAnnotationDiffer differ = new ReferenceAnnotationDiffer(
				differsByType.values());
		AbstractMeasuresRow measuresRow = new AbstractMeasuresRow("", 1);
		differ.getMeasuresRow(measuresRow);
		String micro = "Micro," + measuresRow.getCorrect() + ","
				+ measuresRow.getPartially() + "," + measuresRow.getMissing()
				+ "," + measuresRow.getSpurious() + "," + recall / size + ", "
				+ precision / size + "," + F1 / size;
		writeToFile(micro);
		writeToFile(toString(measuresRow, "Macro") + "\n\n");
		// }
	}

	private static void writeToFile(String input) {

		try {
			writer.append(input + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void loadDocument(File[] files, int index)
			throws GateException, IOException {
		boolean test = false;
		String annotationSetName = "";
		if (index == 2) {
			test = true;
			annotationSetName = "consensus";
		}
		URL url;
		int limit = files.length;
		for (int i = 0; i < limit; i++) {
			String nameFile = files[i].getName();
			if (!score.contains(nameFile)) {
				url = files[i].toURI().toURL();
				try {
					stopWatch.start();
					responsesDocument.add(runApplication(url));
					stopWatch.stop();
					getCharacter(gate.Factory.newDocument(url, "utf-8"));
					keysDocument.add(gate.Factory.newDocument(url, "utf-8"));
					nameAnnotationSet.add(annotationSetName);
					if (test == true) {
						score.add(nameFile);
					}
					size++;
				} catch (Exception e) {

				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private static Document runApplication(URL url) throws GateException,
			IOException {
		Corpus corpus = null;
		try {
			Document document = Factory.newDocument(url, "utf-8");
			corpus = Factory.newCorpus("wrapper");
			corpus.add(document);
			application.setCorpus(corpus);
			application.execute();
			return document;
		} finally {
			if (corpus != null) {
				Factory.deleteResource(corpus);
			}
		}
	}

	private static String toString(AbstractMeasuresRow input, String title) {

		String output = title + "," + input.getCorrect() + ","
				+ input.getPartially() + "," + input.getMissing() + ","
				+ input.getSpurious() + "," + input.getRecall() + ","
				+ input.getPrecision() + "," + input.getF1();
		return output;

	}

	private static void getCharacter(Document document) {

		numberOfCharacter += document.getContent().size();
	}

	private static void writeResultTest() {
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter("resultTest.csv", true));
			bw.write("\n" + timeLoadModel + "," + (timeSum - timeLoadModel)
					+ "," + timeSum + "," + numberOfCharacter + ","
					+ (numberOfCharacter / size) + "," + timeProcessDocument);
			bw.flush();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			if (bw != null) {
				try {
					bw.close();
				} catch (IOException ioe2) {
					ioe2.printStackTrace();
				}
			}
		}
	}
}
