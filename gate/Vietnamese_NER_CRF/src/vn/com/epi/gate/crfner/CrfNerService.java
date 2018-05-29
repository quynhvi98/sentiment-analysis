package vn.com.epi.gate.crfner;

import java.io.IOException;
import java.util.List;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;

/**
 * Posted from May 26, 2018 4:51 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public class CrfNerService {
	private String modelUrl;
	private AbstractSequenceClassifier<CoreLabel> classifier;

	public CrfNerService(String modelUrl) {
		super();
		this.modelUrl = modelUrl;
	}

	public void init() {
		if (classifier == null) {
			classifier = CRFClassifier.getClassifierNoExceptions(modelUrl);
		}
	}

	public String parserToString(String inputString) {
		return classifier.classifyToString(inputString);
	}

	public List<List<CoreLabel>> parserToList(String fileContents) {
		return classifier.classify(fileContents);
	}

	public static void main(String[] args) throws IOException {

		String serializedClassifier = "D:/ner-model.ser.gz";

		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier
				.getClassifierNoExceptions(serializedClassifier);
		System.out.println("get done");
		String s1 = "Với chip xử_lý A7, tốc_độ xử_lý sẽ nhanh hơn, đồ_họa thực_thi cũng mạnh_mẽ hơn so với người tiền nhiệm, hay thậm_chí Samsung_Galaxy_S4, HTC_One? Câu trả lời chính ở những thang điểm benchmark được đo đạc dưới đây, SunSpider Benchmark (số ms càng thấp càng tốt) chẩn đoán hiệu năng xử lý JavaScript, và iPhone 5S đạt được kết quả khá lý tưởng, nó bỏ xa HTC One gấp 4 lần, vượt mặt iPhone 5C gần như gấp đôi. Trong khi đó, Benchmark Geekbench 3 cho thấy hiệu năng một lõi, và đa lõi trên iPhone 5S cũng không hề khuất phục trước các bậc tiền nhiệm.";
		// System.out.println(classifier.classifyToString(s1));
		List<List<CoreLabel>> out = classifier.classify(s1);
		for (List<CoreLabel> sentence : out) {
			for (CoreLabel word : sentence) {
				System.out.print(word.word() + '/'
						+ word.get(CoreAnnotations.AnswerAnnotation.class)
						+ ' ');
			}
			System.out.println();
		}
	}

}
