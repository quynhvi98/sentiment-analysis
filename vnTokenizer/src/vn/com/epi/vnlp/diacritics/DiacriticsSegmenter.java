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
package vn.com.epi.vnlp.diacritics;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Objects;

import vn.com.epi.vnlp.dfa.DFA;
import vn.com.epi.vnlp.tokenizer.dictionary.DictTree;
import vn.hus.nlp.model.BigramResolver;
import vn.hus.nlp.tokenizer.Token;

/**
 * @author Chu Thanh Quang
 *
 */
public class DiacriticsSegmenter {
	private class SortedSyllable implements Comparable<SortedSyllable>{
		private String syllable;
		private double probability;
		
		public double getProb() {
			return probability;
		}
		
		public String getSyllable() {
			return syllable;
		}
		
		public SortedSyllable(String vS, double vP){
			syllable = vS;
			probability = vP;
		}
		
		@Override
		public int compareTo(SortedSyllable a) {
			 if (probability < a.getProb())
				 return 1;
			 if (probability > a.getProb())
				 return -1;
			 return 0;
		}
	}
	private BigramResolver bigram;
	private DictTree syllablesDict, termsDict;
	
	private List<String>[] accentSyls;
	private Map<String, Double>[] bestProb;
	private Map<String, Integer>[] nextIndex;
	private String controlKey = "CONTROL";
	private String[] tokens;
	private Map<Integer, boolean[]> lowered;
	private List<String> segments;
	private double Epsilon = 1E-10;
	public int functionCalls;
	private DecimalFormat df = new DecimalFormat("#.##");
	
	public DiacriticsSegmenter(DFA dfa, BigramResolver resolver){
		this.bigram = resolver;
		this.syllablesDict = new DictTree(dfa, true);
		this.termsDict = new DictTree(dfa, false);
		for (String term:resolver.getUnigramWords()){
			termsDict.addWord(term);
			for (String syllable: term.split(" "))
				syllablesDict.addWord(syllable);
		}
	}
	
	private void getAllDiacritics(Token[] phrase){
		accentSyls = new List[phrase.length];
		lowered = new HashMap<Integer, boolean[]>();
		for (int i = 0; i < phrase.length; i++) {
			accentSyls[i] = new ArrayList<String>();
			String syl = phrase[i].getText();
			List<String> allDictSylls = syllablesDict.getWords(syl, true);
			if (allDictSylls.size() == 0 && !syl.equals(syl.toLowerCase())){
				List<String> lowerDictSylls = syllablesDict.getWords(syl.toLowerCase(), true);
				if (lowerDictSylls.size() > 0){
					allDictSylls.addAll(lowerDictSylls);
					//Mark to Restore the origin form (Lower, Upper) of syllable after searching
					boolean[] loweredChar = new boolean[syl.length()];
					Arrays.fill(loweredChar, false);
					for (int j = 0; j < syl.length(); j++)
						if (Character.isUpperCase(syl.charAt(j)))
							loweredChar[j] = true;
					lowered.put(Integer.valueOf(i), loweredChar);
				}
			}
			if (allDictSylls.size() == 0){//Neu la TU MOI hoan toan khong co trong tu dien va khong co trong ngram
				accentSyls[i].add(syl);
				termsDict.addWord(syl);
			}
			else{
				List<SortedSyllable> syllables = new ArrayList<SortedSyllable>();
				int inDict = 0;
				for (String syllable:allDictSylls){
					syllables.add(new SortedSyllable(syllable, bigram.getUnigramProb(syllable)));
					if (termsDict.accept(syllable) == DictTree.IS_WORD)
						inDict++;
				}
				Collections.sort(syllables);
				if (inDict == 0)
					termsDict.addWord(syllables.get(0).getSyllable());
				for (SortedSyllable syllable:syllables)
					accentSyls[i].add(syllable.getSyllable());
				
			}
			
			/*if (lowered.get(Integer.valueOf(i)) != null)
				System.out.print("LOWERED ");
			System.out.print(syl + ":");
			for (int j = 0; j < accentSyls[i].size(); j++)
				System.out.print(accentSyls[i].get(j) + "-" + (termsDict.accept(accentSyls[i].get(j))== DictTree.IS_WORD) + ", ");
			
			System.out.println();*/
		}
	}
	
	public Token[] fillDiacritics(Token[] phrase){
			getAllDiacritics(phrase);
			bestProb = new HashMap[phrase.length];
			nextIndex = new HashMap[phrase.length];
			for (int i=0; i < phrase.length; i++){
				bestProb[i] = new HashMap<String,Double>();
				nextIndex[i] = new HashMap<String,Integer>();
			}
			bestProb[0].put(controlKey, -Double.MAX_VALUE);
			nextIndex[0].put(controlKey, 0);
			
			segments = new ArrayList<String>();
			functionCalls = 0;
			search(0, 0, 0.0, new String());
			//System.out.println("Number of function calls:" + functionCalls);
			if (tokens == null || tokens.length == 0 || bestProb[0].get(controlKey) == -Double.MAX_VALUE){
				tokens = new String[phrase.length];
				
				for (int i = 0; i < phrase.length; i ++)
					tokens[i] = phrase[i].getText();
			}
			Token[] result = new Token[tokens.length];
			int i = 0;
			int h = 0;
			for (String term:tokens){
				char[] chars = term.toCharArray();
				String[] syls = term.split(" ");
				int charIndx = 0;
				int sylIndx = h;
				for (String syl:syls){
					boolean[] loweredChar = lowered.get(Integer.valueOf(h++));
					if (loweredChar != null)
						for (int j = 0; j < loweredChar.length; j++)
							if (loweredChar[j])
								chars[charIndx + j] = Character.toUpperCase(syl.charAt(j));
					charIndx += syl.length() + 1;		
				}
				Token tk = new Token(Token.TERM, String.valueOf(chars));
				tk.setStartOffset(phrase[sylIndx].getStartOffset());
				tk.setEndOffset(phrase[h - 1].getEndOffset());
				tk.setCaption(phrase[sylIndx].getType());
				result[i++] = tk;
			}
			
			return result;
	}
	
	private void search (int k, int index, double prob, String word){
		functionCalls++;
		for (int i = index; i < accentSyls[k].size(); i++){
			String lastText = null;
			if (word.isEmpty())
				lastText = new String(accentSyls[k].get(i));
			else 
				lastText = new String(word + " " + accentSyls[k].get(i));
			int state = termsDict.accept(lastText);
			if (state == DictTree.IS_NOT_WORD) 
				continue;
			
			double lastProb = prob;
			if (state == DictTree.IS_WORD){
				int n = segments.size();
				if (n == 0)
					lastProb += bigram.getUnigramProb(lastText);
				else
					lastProb += bigram.getBigramProb(segments.get(n-1), lastText);
				
				if (lastProb > bestProb[0].get(controlKey) && k + 1 == accentSyls.length){
					segments.add(lastText);
					takeResult(lastProb);
					segments.remove(segments.size() - 1);
				}
			}
			if (k + 1 < accentSyls.length){
				
				search(k + 1, 0, prob, lastText);
				
				if (lastProb > bestProb[0].get(controlKey) && state == DictTree.IS_WORD){
					double estimate = lastProb;
					Double nextProb = bestProb[k+1].get(lastText);
					if (nextProb != null)
						estimate += nextProb.doubleValue();
					else {
						nextProb = bestProb[k+1].get(controlKey);
						if (nextProb != null)
							estimate += nextProb.doubleValue();
					}
					
					int nextInd = 0;
					Integer indx = nextIndex[k+1].get(lastText);
					if (indx != null)
						nextInd = indx.intValue();
					
					if (estimate > bestProb[0].get(controlKey)/* - 1.0*/){
						segments.add(lastText);
						//printSegments(lastProb, estimate);
						search(k + 1, nextInd, lastProb, new String());
						segments.remove(segments.size() - 1);
					}
				}
			}
		}
	}
	
	/*private void printSegments(double lastProb, double estimate){
		for (String s:segments)
			System.out.print(s + "\t");
		System.out.println("(" + df.format(lastProb) +"|" + df.format(estimate)+ ")");
	}*/
	
	private void updateNextIndex(String previous, int i, String current){
		int j = 0;
		while (!current.split(" ")[0].equals(accentSyls[i].get(j)))
			j++;
		
		int currentIndex = Objects.firstNonNull(nextIndex[i].get(previous), 0);
		if (currentIndex < j)
			nextIndex[i].put(previous, Integer.valueOf(j));
	}
	
	private void takeResult(double lastProb){
		int i = 0;
		tokens = new String[segments.size()];
		segments.toArray(tokens);
		String previous = controlKey;
        
		for (String current:tokens){
				
			double currentProb = Objects.firstNonNull(bestProb[i].get(previous), -Double.MAX_VALUE);
			if (lastProb > currentProb){
				bestProb[i].put(previous, lastProb);
				updateNextIndex(previous, i, current);
			}
			
			if (i == 0){
				lastProb -= bigram.getUnigramProb(current);
			}
			else {
				lastProb -= bigram.getBigramProb(previous, current);
				if (Math.abs(lastProb) < Epsilon) 
					lastProb = 0.0;
				double controlProb = Objects.firstNonNull(bestProb[i].get(controlKey), -Double.MAX_VALUE);
				if (lastProb > controlProb){
					bestProb[i].put(controlKey, lastProb);
				}
			}
			
			//System.out.print(current + "|\t");
			previous = current;
			i += current.split(" ").length;
		}
		//System.out.println(df.format(bestProb[0].get(controlKey)));
	}
}
