/*******************************************************************************
 * Copyright (c) 2012 ePi Technologies.
 * 
 * This file is part of VNLP: a Natural Language Processing framework 
 * for Vietnamese.
 * 
 * VNLP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * VNLP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with VNLP.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package vn.com.epi.tagger.benchmark;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Nguyen Vi Duong (vi.duong.bk@gmail.com)
 * 
 */
public class BenchMark {

	private static final String URL_OF_JVNTAGGER = "gate/Vietnamese_JTagger/resources/maxent";
	private static final String URL_OF_POSTAGGER = "resources/models/vtb.tagger";
	private static int noOfAllToken = 0;
	private static int noJVNCorrectToken = 0;
	private static int noPosCorrectToken = 0;
	private static int noOfAllSentence = 0;
	private static int noJVNCorrectSentence = 0;
	private static int noPosCorrectSentence = 0;
	private static JVnTagger jVnTagger;
//	private static PosTagger posTagger;
	private static final ArrayList<String> PUNCTUATION = new ArrayList<String>(
			Arrays.asList(",", ".", ":", ";", "(", ")", "{", "}", "[", "]",
					"!", "?", "<", ">", "-", "/", "+", "~"));

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args[0].equals("-i")) {
			try {
				StringBuffer url = new StringBuffer();
				for (int i = 1; i < args.length; i++) {
					url.append(args[i]);
					url.append(" ");
				}
				File folder = new File(url.toString().trim());
				if (folder.isDirectory()) {
					benchMark(folder.getAbsolutePath());
				} else {
					System.out
							.println("Option should starts with <-i> and folder url. Example: -i D:/Kho ngu lieu 10000 cau duoc gan nhan tu loai");
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		} else {
			System.out
					.println("Please insert Url of treebank, option should starts with <-i>. Example: -i D:/Kho ngu lieu 10000 cau duoc gan nhan tu loai");
		}

	}

	private static void benchMark(String urlOfTreeBank) {
		DataProcess dataProcess = new DataProcess();
		try {
			ArrayList<String> allSentence = dataProcess.readData(urlOfTreeBank);
			jVnTagger = new JVnTagger();
			jVnTagger.init(URL_OF_JVNTAGGER);
//			posTagger = new PosTagger();
//			posTagger.init(URL_OF_POSTAGGER);
			noOfAllSentence = allSentence.size();

			for (String sentence : allSentence) {
				List<String> taggedByHuman = dataProcess.getTagger(sentence);
				if (taggedByHuman != null) {
					String sentenceWithoutTag = dataProcess
							.getSentence(sentence);
					List<String> taggedByJVnTagger = dataProcess
							.getTagger(jVnTagger.tag(sentenceWithoutTag));
//					List<String> taggedByPosTagger = dataProcess
//							.getTagger(posTagger.tag(sentenceWithoutTag));
					noOfAllToken += taggedByHuman.size();
					if (checkCorrectSentence(taggedByHuman, taggedByJVnTagger,
							true)) {
						noJVNCorrectSentence++;
					}

//					if (checkCorrectSentence(taggedByHuman, taggedByPosTagger,
//							false)) {
//						noPosCorrectSentence++;
//					}
				}
			}
			System.out.println("Number of correct token by JVNTagger:"
					+ noJVNCorrectToken);
			System.out.println("Number of correct token by PosTagger:"
					+ noPosCorrectToken);
			System.out.println("Number of all token: " + noOfAllToken);
			System.out.println("Number of correct sentences by JVNTagger:"
					+ noJVNCorrectSentence);
			System.out.println("Number of correct sentences by PosTagger:"
					+ noPosCorrectSentence);
			System.out.println("Number of all sentences: " + noOfAllSentence);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static boolean checkCorrectSentence(List<String> taggedByHuman,
			List<String> taggedByTagger, boolean isJvnTag) {
		if (taggedByHuman.size() != taggedByTagger.size()) {
			return false;
		} else {
			boolean out = true;
			for (int i = 0; i < taggedByHuman.size(); i++) {
				if (taggedByHuman.get(i)
						.equalsIgnoreCase(taggedByTagger.get(i))) {
					if (isJvnTag) {
						noJVNCorrectToken++;
					} else {
						noPosCorrectToken++;
					}
				} else {
					if (PUNCTUATION.contains(taggedByHuman.get(i))) {
						System.out.println("PosTag incorrect: "
								+ taggedByHuman.get(i) + "----"
								+ taggedByTagger.get(i));
						noPosCorrectToken++;
					}
					out = false;
				}
			}
			return out;
		}
	}

}
