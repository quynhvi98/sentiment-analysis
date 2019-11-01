package vn.com.epi.gate.crfner.test;

import java.io.IOException;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * This is a demo of calling CRFClassifier programmatically.
 * <p>
 * Posted from May 29, 2018 4:51 PM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */

public class NERDemo {

	public static void main(String[] args) throws IOException {

		String serializedClassifier = "gate/Vietnamese_NER_CRF/resources/ner-model.ser.gz";

		if (args.length > 0) {
			serializedClassifier = args[0];
		}

		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);

		/*
		 * For either a file to annotate or for the hardcoded text example, this
		 * demo file shows two ways to process the output, for teaching
		 * purposes. For the file, it shows both how to run NER on a String and
		 * how to run it on a whole file. For the hard-coded String, it shows
		 * how to run it on a single sentence, and how to do this and produce an
		 * inline XML output format.
		 */
		if (args.length > 1) {
			String fileContents = IOUtils.slurpFile(args[1]);
			List<List<CoreLabel>> out = classifier.classify(fileContents);
			for (List<CoreLabel> sentence : out) {
				for (CoreLabel word : sentence) {
					System.out.print(word.word() + '/'
							+ word.get(CoreAnnotations.AnswerAnnotation.class)
							+ ' ');
				}
				System.out.println();
			}
			out = classifier.classifyFile(args[1]);
			for (List<CoreLabel> sentence : out) {
				for (CoreLabel word : sentence) {
					System.out.print(word.word() + '/'
							+ word.get(CoreAnnotations.AnswerAnnotation.class)
							+ ' ');
				}
				System.out.println();
			}

		} else {
			String productLable = "Product";
			String locationLable = "Location";
			String organizationLable = "Organization";
			String personLable = "Person";
			String s1 = "Với chip xử lý A7, tốc độ xử lý sẽ nhanh hơn, đồ họa thực thi cũng mạnh mẽ hơn so với người tiền nhiệm, hay thậm chí Samsung Galaxy S4, HTC One? Câu trả lời chính ở những thang điểm benchmark được đo đạc dưới đây, SunSpider Benchmark (số ms càng thấp càng tốt) chẩn đoán hiệu năng xử lý JavaScript, và iPhone 5S đạt được kết quả khá lý tưởng, nó bỏ xa HTC One gấp 4 lần, vượt mặt iPhone 5C gần như gấp đôi. Trong khi đó, Benchmark Geekbench 3 cho thấy hiệu năng một lõi, và đa lõi trên iPhone 5S cũng không hề khuất phục trước các bậc tiền nhiệm .";
			// String s2 =
			// "I go to school at Stanford University, which is located in California.";
			System.out.println(classifier.classifyToString(s1));
			// String[] postProcessTokens =
			// classifier.classifyToString(s1).split(
			// " ");
			// for (int i = 0; i < postProcessTokens.length; i++) {
			// int startToken, endToken;
			// if (postProcessTokens[i].endsWith(productLable)) {
			// startToken = endToken = i;
			// int j = 1;
			// while (postProcessTokens[i + j].endsWith(productLable)) {
			// endToken++;
			// j++;
			// i++;
			// }
			// System.out.println(postProcessTokens[startToken]
			// + postProcessTokens[endToken] + ": leng = "
			// + endToken +" "+ startToken);
			//
			// } else if (postProcessTokens[i].endsWith(locationLable)) {
			// startToken = endToken = i;
			// int j = 1;
			// while (postProcessTokens[i + j].endsWith(locationLable)) {
			// endToken++;
			// j++;
			// i++;
			// }
			// System.out.println(postProcessTokens[startToken]
			// + postProcessTokens[endToken] + ": leng = "
			// + endToken +" "+ startToken);
			//
			// } else if (postProcessTokens[i].endsWith(organizationLable)) {
			// startToken = endToken = i;
			// int j = 1;
			// while (postProcessTokens[i + j].endsWith(organizationLable)) {
			// endToken++;
			// j++;
			// i++;
			// }
			// System.out.println(postProcessTokens[startToken]
			// + postProcessTokens[endToken] + ": leng = "
			// + endToken +" "+ startToken);
			//
			// } else if (postProcessTokens[i].endsWith(personLable)) {
			// startToken = endToken = i;
			// int j = 1;
			// while (postProcessTokens[i + j].endsWith(personLable)) {
			// endToken++;
			// j++;
			// i++;
			// }
			// System.out.println(postProcessTokens[startToken]
			// + postProcessTokens[endToken] + ": leng = "
			// + endToken +" "+ startToken);
			//
			// }
			// }
			// System.out.println(classifier.classifyWithInlineXML(s2));
			// System.out.println(classifier.classifyToString(s2, "xml", true));
			// int i=0;
			// for (List<CoreLabel> lcl : classifier.classify(s2)) {
			// for (CoreLabel cl : lcl) {
			// System.out.println(i++ + ":");
			// System.out.println(cl);
			// }
			// }
		}
	}

}
