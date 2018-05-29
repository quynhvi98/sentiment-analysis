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
package vn.com.epi.vnlp.tokenizer.benchmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;

import vn.com.epi.vnlp.dfa.DFA;
import vn.com.epi.vnlp.dfa.io.DfaSaxReader;
import vn.com.epi.vnlp.diacritics.AccentRemover;
import vn.com.epi.vnlp.diacritics.DiacriticsRestorer;
import vn.hus.nlp.model.BigramResolver;
import vn.hus.nlp.tokenizer.TokenizerUtils;

/**
 * @author Chu Thanh Quang
 *
 */
public class DiacriticsBenchmark {
	private static final String RESOURCE_DIR = "vnTokenizer/resources";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			File contentFile = new File(RESOURCE_DIR + "/Token.txt");
			ArrayList<String> allSentence = readFile(contentFile);
			BigramResolver bigramResolver = initBigramResolver(RESOURCE_DIR);
			
			DFA dfa = new DfaSaxReader(new FileReader(
					RESOURCE_DIR + "/automata/dfaLexicon.xml")).read();
			
			DiacriticsRestorer filler = new DiacriticsRestorer(dfa, bigramResolver);
			
			int size = allSentence.size();
			int correct = 0, all = 0;
			long begin = System.currentTimeMillis();
			for(int i = 0; i < size; i++){
				String curSentence = TokenizerUtils.trimUnderscore(allSentence.get(i));
				all+= curSentence.split(" ").length;
				//System.out.println(curSentence);
				
				String noAccent = AccentRemover.removeSigns(curSentence.replace("_", " "));
				filler.initTokenize(noAccent);
				
				String newSentence = TokenizerUtils.toString(filler);
				
				//System.out.println(newSentence);
				
				correct += compare(curSentence, newSentence);
			}
			
			System.out.println("Tong so token: " + all);
			long end = System.currentTimeMillis();
			double rate = 100.00*correct/all;
			DecimalFormat df = new DecimalFormat("##.##");
			System.out.println("Tong so token dien dau dung: " + correct + " = " + df.format(rate) + "% \tProcessing time:" + (end - begin));
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private static int compare(String firstSentence, String secondSentence){
		int count = 0;
		String[] firstArray = firstSentence.split(" ");
		String[] secondArray = secondSentence.split(" ");
		int firstTokenIndex = 0, secondTokenIndx = 0, firstSyllableIndex = 0, secondSyllableIndx = 0;
		while (firstTokenIndex < firstArray.length 
				&& secondTokenIndx < secondArray.length){
			if (firstArray[firstTokenIndex].equals(secondArray[secondTokenIndx])){
				count ++;
				firstSyllableIndex += firstArray[firstTokenIndex].split("_").length;
				secondSyllableIndx += secondArray[secondTokenIndx].split("_").length;
			}else{
				if (firstArray[firstTokenIndex].isEmpty()){
					firstTokenIndex++;
					continue;
				}
				if (secondArray[secondTokenIndx].isEmpty()){
					secondTokenIndx++;
					continue;
				}
				if (	firstArray[firstTokenIndex].indexOf("’") == -1 &&
						firstArray[firstTokenIndex].indexOf("'") == -1 &&
						firstArray[firstTokenIndex].indexOf(".") == -1 &&
						firstArray[firstTokenIndex].indexOf("-") == -1 &&
						firstArray[firstTokenIndex].indexOf("0") == -1 &&
						firstArray[firstTokenIndex].indexOf("1") == -1 &&
						firstArray[firstTokenIndex].indexOf("2") == -1 &&
						firstArray[firstTokenIndex].indexOf("3") == -1 &&
						firstArray[firstTokenIndex].indexOf("4") == -1 &&
						firstArray[firstTokenIndex].indexOf("5") == -1 &&
						firstArray[firstTokenIndex].indexOf("6") == -1 &&
						firstArray[firstTokenIndex].indexOf("7") == -1 &&
						firstArray[firstTokenIndex].indexOf("8") == -1 &&
						firstArray[firstTokenIndex].indexOf("9") == -1)
					System.out.println(firstArray[firstTokenIndex] + " != " + secondArray[secondTokenIndx] /*+ " In phrase:" + firstSentence*/);
				firstSyllableIndex += firstArray[firstTokenIndex].split("_").length;
				secondSyllableIndx += secondArray[secondTokenIndx].split("_").length;
				while (firstSyllableIndex != secondSyllableIndx){
					if (firstSyllableIndex < secondSyllableIndx){
						firstTokenIndex ++;
						if (firstTokenIndex == firstArray.length)
							break;
						firstSyllableIndex += firstArray[firstTokenIndex].split("_").length;
					}
					if (firstSyllableIndex > secondSyllableIndx){
						secondTokenIndx ++;
						if (secondTokenIndx == secondArray.length)
							break;
						secondSyllableIndx += secondArray[secondTokenIndx].split("_").length;
					}
				}
			}
			firstTokenIndex ++;
			secondTokenIndx ++;
		}
		return count;
	}

	private static ArrayList<String> readFile(File inputFile) {
		ArrayList<String> output = new ArrayList<String>();
		try {
			String strLine = new String();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(inputFile), "UTF-8"));
			while ((strLine = br.readLine()) != null) {
				output.add(strLine.trim());
			}
			br.close();
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private static BigramResolver initBigramResolver(String resourceDir) throws IOException {
		BigramResolver resolver = new BigramResolver(
				new File(resourceDir, "bigram/bigram.xml"), 
				new File(resourceDir, "bigram/unigram.xml"));
		return resolver;
	}
	
}