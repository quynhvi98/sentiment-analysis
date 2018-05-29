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
package vn.com.epi.vnlp.tokenizer;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import vn.com.epi.vnlp.dfa.DFA;
import vn.com.epi.vnlp.tokenizer.dictionary.DictTree;
import vn.hus.nlp.model.BigramResolver;
import vn.hus.nlp.tokenizer.Token;
import vn.hus.nlp.tokenizer.VLexer;

import com.google.common.base.Objects;

/**
 * @author Chu Thanh Quang
 *
 */
public class RecursiveSegmenter {
	private DFA dfa;
	private BigramResolver bigram;
	private DictTree termsDict;
	private List<String> tempAdded;
	private String[] syllables;
	private Map<String, Double>[] bestProb;
	private Map<String, Integer>[] minLength;
	private String controlKey = "CONTROL";
	private String[] tokens;
	private List<String> segments;
	private double Epsilon = 1E-10;
	public int functionCalls;
	private DecimalFormat df = new DecimalFormat("#.##");
	private boolean loweredFirst;
	private int firstTokenLength;
	private boolean checkConnected;
	
	public RecursiveSegmenter(DFA dfa, BigramResolver resolver){
		this.bigram = resolver;
		this.dfa = dfa;
		this.termsDict = new DictTree(dfa, false);
		for (String term:resolver.getUnigramWords())
			termsDict.addWord(term);
	}
	
	private void lowerFirstToken(Token[] phrase){
		int firstTokenLen = 0;
		String text = phrase[firstTokenLen].getText();
		int checkWord = termsDict.accept(text);
		int lastCheck = checkWord;
		while (checkWord != DictTree.IS_NOT_WORD){
			lastCheck = checkWord;
			firstTokenLen++;
			if (firstTokenLen == phrase.length) break;
			text = text + " " + phrase[firstTokenLen].getText();
			checkWord = termsDict.accept(text);
		}
		
		if (lastCheck != DictTree.IS_WORD)
			firstTokenLen = 0;
		
		int lowerFirstLen = 0;
		boolean inDict = dfa.accept(phrase[lowerFirstLen].getText().toLowerCase());
		int state = dfa.getLastState();
		boolean lastIsWord = inDict;
		while (state >= 0){
			lastIsWord = inDict;
			lowerFirstLen++;
			if (lowerFirstLen == phrase.length || phrase[lowerFirstLen].getType() != VLexer.WORD_LOWER) 
				break;
			inDict = dfa.accept(" " + phrase[lowerFirstLen].getText(), state);
			state = dfa.getLastState();
		}
		
		if (lowerFirstLen < firstTokenLen)
			return;
		
		if (state < 0){
			if (lastIsWord && lowerFirstLen > 1 ){
				loweredFirst = true;
				firstTokenLength = lowerFirstLen;
			}
		}
		else if (lowerFirstLen == phrase.length){
			if (inDict && lowerFirstLen > 1 ){
				loweredFirst = true;
				firstTokenLength = lowerFirstLen;
			}
		}
		else{//phrase[lowerFirstLen].getType() != VLexer.WORD_LOWER
			if (inDict && lowerFirstLen > 1 ){
				loweredFirst = true;
				firstTokenLength = lowerFirstLen;
			}
			if (inDict && lowerFirstLen == 1){
				int h = 1;
				String shortName = null;
				checkWord = DictTree.IS_WORD_PART;
				while (checkWord != DictTree.IS_WORD){
					if (h == phrase.length || phrase[lowerFirstLen].getType() != VLexer.WORD_UPPER)
						break;
					if (h == 1)
						shortName = phrase[h].getText();
					else
						shortName = shortName + " " + phrase[h].getText();
					checkWord = termsDict.accept(shortName);
					h++;
				}
				String longName = phrase[0].getText();
				if (shortName != null) 
					longName = longName + " " + shortName;
				if (shortName != null && checkWord == DictTree.IS_WORD &&
						termsDict.accept(longName) != DictTree.IS_WORD){
					loweredFirst = true;
					firstTokenLength = lowerFirstLen;
				}
			}
		}
	}
	
	private void connectNames(String part, String name){
		if (name.isEmpty())
			checkConnected = true;
		String[] syllables = name.split(" ");
		String word = null;
		if (part.isEmpty())
			word = new String(syllables[0]);
		else 
			word = new String(part + " " + syllables[0]);
		
		String remaining;
		if (syllables.length == 1)
			remaining = name.substring(syllables[0].length());
		else
			remaining = name.substring(syllables[0].length() + 1);
				
		int state = termsDict.accept(word);
		if (state == DictTree.IS_WORD && remaining.isEmpty())
			checkConnected = true;
		
		if ((state == DictTree.IS_WORD_PART || state == DictTree.IS_WORD)
				&& !remaining.isEmpty())
			connectNames(word, remaining);
	
		if (state == DictTree.IS_WORD && !remaining.isEmpty())
			connectNames(new String(""), remaining);
	}
	
	private void initSegmentation(Token[] phrase){
		syllables = new String[phrase.length];
		int h = 0;
		while ( h < phrase.length){
			if (h == 0 && loweredFirst)
				syllables[h] = phrase[h].getText().toLowerCase();
			else
				syllables[h] = phrase[h].getText();
			
			if (loweredFirst && h < firstTokenLength)
				h++;
			else if (phrase[h].getType() == VLexer.WORD_UPPER
				/*|| phrase[h].getType() == VLexer.WORD_OTHER
				|| phrase[h].getType() == VLexer.WORD_ALL_CAPS*/){
				String name = new String(phrase[h].getText());
				h++;
				while (h < phrase.length 
						&& (phrase[h].getType() == VLexer.WORD_UPPER
							/*|| phrase[h].getType() == VLexer.WORD_OTHER
							|| phrase[h].getType() == VLexer.WORD_ALL_CAPS*/)){
					syllables[h] = phrase[h].getText();
					name = name + " " + phrase[h].getText();
					h++;
				}
				checkConnected = false;
				connectNames(new String(""), name);
				if ((!checkConnected) && termsDict.accept(name) != DictTree.IS_WORD){
					termsDict.addWord(name);
					tempAdded.add(name);
					//System.out.println("add " + name);
				}
			} else{
				if (termsDict.accept(syllables[h]) != DictTree.IS_WORD){
					termsDict.addWord(syllables[h]);
					tempAdded.add(syllables[h]);
					//System.out.println("add " + syllables[h]);
				}
				h++;
			}
		}
	}

	public Token[] segment(Token[] phrase){
		loweredFirst = false;
		firstTokenLength = 0;
		if (phrase[0].getType() == VLexer.WORD_UPPER)
			lowerFirstToken(phrase);
		tempAdded = new ArrayList<String>();
		initSegmentation(phrase);
		bestProb = new HashMap[phrase.length];
		for (int i=0; i < phrase.length; i++)
			bestProb[i] = new HashMap<String,Double>();
		bestProb[0].put(controlKey, -Double.MAX_VALUE);
		minLength = new HashMap[phrase.length];
		for (int i=0; i < phrase.length; i++)
			minLength[i] = new HashMap<String,Integer>();
		minLength[0].put(controlKey, phrase.length);
		segments = new ArrayList<String>();
		functionCalls = 0;
		search(0, 0.0, new String());

		for (String tempWord:tempAdded){
			termsDict.removeWord(tempWord);
			//System.out.println("remove " + tempWord);
		}
		if (tokens == null || tokens.length == 0 || bestProb[0].get(controlKey) == -Double.MAX_VALUE){
			tokens = new String[phrase.length];
			
			for (int i = 0; i < phrase.length; i ++)
				tokens[i] = phrase[i].getText();
		}
		Token[] result = new Token[tokens.length];
		int i = 0;
		int j = 0;
		//System.out.print("Output:");
		for (String term:tokens){
			int length = term.split(" ").length;
			String token;
			if (i == 0 && loweredFirst /*&& length == firstTokenLength*/){
				token = phrase[0].getText();
				for (int k = 1; k < length; k++)
					token = token + " " + phrase[k].getText();
			}
			else
				token = term;
			Token tk = null;
			if (phrase[j].getType() == VLexer.NUMBER)
				tk = new Token(Token.NUMBER, token);
			else
				tk = new Token(Token.TERM, token);
			tk.setStartOffset(phrase[j].getStartOffset());
			tk.setEndOffset(phrase[j + length - 1].getEndOffset());
			tk.setCaption(phrase[j].getType());
			result[i++] = tk;
			j += length;
			//System.out.print(term.replace(" ", "_") + " ");
		}//System.out.println();
		
		return result;
	}
	
	private void search (int k, double prob, String word){
		functionCalls++;
		
		String lastText = null;
		if (word.isEmpty())
			lastText = new String(syllables[k]);
		else 
			lastText = new String(word + " " + syllables[k]);
		int state = termsDict.accept(lastText);
		if (state == DictTree.IS_NOT_WORD) 
			return;
		
		double lastProb = prob;
		if (state == DictTree.IS_WORD){
			int n = segments.size();
			if (n == 0)
				lastProb += bigram.getUnigramProb(lastText);
			else
				lastProb += bigram.getBigramProb(segments.get(n-1), lastText);
			
			if (//segments.size() + 1 <= minLength[0].get(controlKey) &&
					   lastProb > bestProb[0].get(controlKey) 
					&& k + 1 == syllables.length){
				segments.add(lastText);
				takeResult(lastProb);
				segments.remove(segments.size() - 1);
			}
		}
		if (k + 1 < syllables.length){
			
			search(k + 1, prob, lastText);
			
			if (lastProb > bestProb[0].get(controlKey) 
				&& state == DictTree.IS_WORD){
				
				double estimateProb = lastProb;
				Double nextProb = bestProb[k+1].get(lastText);
				if (nextProb != null)
					estimateProb += nextProb.doubleValue();
				else {
					nextProb = bestProb[k+1].get(controlKey);
					if (nextProb != null)
						estimateProb += nextProb.doubleValue();
				}
				
				int estimateLength = segments.size();
				Integer nextLength = minLength[k+1].get(lastText);
				if (nextLength != null)
					estimateLength += nextLength.intValue();
				else{
					nextLength = minLength[k+1].get(controlKey);
					if (nextLength != null)
						estimateLength += nextLength.intValue();
					else
						estimateLength += 1;
				}
							
				if (//estimateLength <= minLength[0].get(controlKey) &&
					estimateProb > bestProb[0].get(controlKey)){
					segments.add(lastText);
					//printSegments(lastProb, estimate);
					search(k + 1, lastProb, new String());
					segments.remove(segments.size() - 1);
				}
			}
		}
		
	}
	
	private void printSegments(double lastProb, double estimate){
		for (String s:segments)
			System.out.print(s + "\t");
		System.out.println("(" + df.format(lastProb) +"|" + df.format(estimate)+ ")");
	}
	
	
	private void takeResult(double lastProb){
		int i = 0;
		int length = segments.size();
		tokens = new String[segments.size()];
		segments.toArray(tokens);
		String previous = controlKey;
        
		for (String current:tokens){
				
			double currentProb = Objects.firstNonNull(bestProb[i].get(previous), -Double.MAX_VALUE);
			if (lastProb > currentProb)
				bestProb[i].put(previous, lastProb);
			
			int currentLength = Objects.firstNonNull(minLength[i].get(previous), minLength[0].get(controlKey));
			if (length < currentLength)
				minLength[i].put(previous, Integer.valueOf(length));
			
			if (i == 0){
				lastProb -= bigram.getUnigramProb(current);
			}
			else {
				lastProb -= bigram.getBigramProb(previous, current);
				
				if (Math.abs(lastProb) < Epsilon) 
					lastProb = 0.0;
				
				double controlProb = Objects.firstNonNull(bestProb[i].get(controlKey), -Double.MAX_VALUE);
				if (lastProb > controlProb)
					bestProb[i].put(controlKey, lastProb);
				
				int controlLength = Objects.firstNonNull(minLength[i].get(controlKey), minLength[0].get(controlKey));
				if (length < controlLength)
					minLength[i].put(controlKey, Integer.valueOf(length));
			}
			
			//System.out.print(current + "|\t");
			previous = current;
			i += current.split(" ").length;
		}
		//System.out.println(df.format(bestProb[0].get(controlKey)));
	}
}
