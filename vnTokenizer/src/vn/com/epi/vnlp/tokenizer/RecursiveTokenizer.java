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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonToken;

import vn.com.epi.vnlp.dfa.DFA;
import vn.com.epi.vnlp.tokenizer.dictionary.DictTree;
import vn.hus.nlp.model.BigramResolver;
import vn.hus.nlp.tokenizer.ResultMerger;
import vn.hus.nlp.tokenizer.Token;
import vn.hus.nlp.tokenizer.Tokenizer;
import vn.hus.nlp.tokenizer.VLexer;

/**
 * @author Chu Thanh Quang
 *
 */
public class RecursiveTokenizer implements Tokenizer {
	static final int END_FILE = -1;
	static final int END_SEGMENT = 0;
	static final int BEFORE_END_SEGMENT = 1;
	static final int IN_SEGMENT = 2;
	
	private VLexer lexer;
	private DictTree syllableDict;
	private RecursiveSegmenter segmenter;
	private LinkedList<Token> result;
	private ResultMerger merger;
	private int boundState;
	
	public RecursiveTokenizer(DFA dfa, BigramResolver resolver) {
		segmenter = new RecursiveSegmenter(dfa,resolver);
		syllableDict = new DictTree(dfa, true);
		for (String term:resolver.getUnigramWords())
			for (String syllable: term.split(" "))
				syllableDict.addWord(syllable);
	}

	public void initTokenize(String sentence){
		lexer = new VLexer(new ANTLRStringStream(sentence));
		merger = new ResultMerger();
		result = new LinkedList<Token>();
		boundState = 0;
	}
	
	public Token next() {
		if (hasNext()) {
			Token word = result.getFirst();
			result.removeFirst();
			return word;
		}
		return null;
	}

	public boolean hasNext() {
		if (result.size() <= 0) {
			tryTokenize();
		}
		return result.size() > 0;
	}
	
	private Token nextToken() {
		Token resultToken = null;
		CommonToken temp = null;
		do {
			temp = (CommonToken) lexer.nextToken();
			if (temp.getType() == VLexer.SPACE)
				addResult(fromLexerToken(temp, Token.SPACE));
		}
		while (temp.getType() == VLexer.SPACE);
		
		CommonToken last = temp;
		String text = new String();
		int start = temp.getStartIndex();
		int end = temp.getStopIndex() + 1;
		int type = temp.getType();
		
		while (temp.getType() != VLexer.SPACE && temp.getType() != VLexer.EOF){
			text = text + temp.getText();
			end = temp.getStopIndex() + 1;
			last = temp;
			temp = (CommonToken) lexer.nextToken();
		}
		
		if (temp.getType() == VLexer.EOF && last == temp){
			boundState = END_FILE;
			return null;
		}
		
		if (last.getType() == VLexer.PUNCTUATION && text.length() == 1){
			resultToken = fromLexerToken(last, Token.PUNCTUATION);
			addResult(resultToken);
			boundState = END_SEGMENT;
		}
		else
		if (last.getType() == VLexer.PUNCTUATION && syllableDict.accept(text) != DictTree.IS_WORD){
			resultToken = new Token(type, text.substring(0, text.length() - 1));
			resultToken.setStartOffset(start);
			resultToken.setEndOffset(end - 1);
			resultToken.setCaption(type);
			addResult(fromLexerToken(last, Token.PUNCTUATION));
			boundState = BEFORE_END_SEGMENT;
		}
		else{
			resultToken = new Token(type, text);
			resultToken.setStartOffset(start);
			resultToken.setEndOffset(end);
			resultToken.setCaption(type);
			boundState = IN_SEGMENT;
		}
		
		if (temp.getType() == VLexer.SPACE)
			addResult(fromLexerToken(temp, Token.SPACE));
		
		return resultToken;
	}

	private void tryTokenize() {
		
		Token token = null;
		while (boundState == BEFORE_END_SEGMENT || boundState == END_SEGMENT) 
			token = nextToken();
		
		LinkedList<Token> syllables = new LinkedList<Token>();
		while (boundState == IN_SEGMENT) {
			syllables.addLast(token);
			token = nextToken();
		}
		
		if (boundState == BEFORE_END_SEGMENT)
			syllables.addLast(token);
		
		if (syllables.size() > 0){
			Token[] temp = new Token[syllables.size()];
			Token[] tokens = segmenter.segment(syllables.toArray(temp));
			for (int j = 0; j < tokens.length; j++)
				addResult(tokens[j]);
			
			merger.mergeList(result);
		}
	}
	
	private Token fromLexerToken(CommonToken token, int type) {
		Token tempToken = new Token(type, token.getText());
		int start = token.getStartIndex();
		tempToken.setStartOffset(start);
		tempToken.setEndOffset(token.getStopIndex() + 1);
		tempToken.setCaption(token.getType());
		return tempToken;
	}

	//Tim dung vi tri cua token de add
	private void addResult(Token token) {
		//result.addLast(token);
		if (result.size() == 0){
			result.addLast(token);
			return;
		}
		int i = result.size();
		List<Token> spaces = new ArrayList<Token>();
		Token current = null;
		do{
			if (current != null 
				&& current.getEndOffset() < token.getEndOffset()){
				spaces.add(current);
				result.remove(i);
			}
			i--;
			if (i < 0) break;
			current = result.get(i);
		}while (current.getStartOffset() > token.getStartOffset());
			
		if (spaces.size() == 0)
			result.add(i + 1, token);
		else {//Insert space tokens vao dung vi tri
			String[] syllables = token.getText().split(" ");
			StringBuffer tempText = new StringBuffer();
			int offset = token.getStartOffset();
			tempText.append(syllables[0]);
			offset += syllables[0].length();
			for (int j = 1; j < syllables.length; j ++){
				boolean found = true;
				while (found && spaces.size() > 0){
					found = false;
					for (int k = 0; k < spaces.size(); k++){
						Token space = spaces.get(k);
						if (space.getStartOffset() == offset){
							tempText.append(space.getText());
							offset += space.getText().length();
							spaces.remove(k);
							found = true;
							break;
						}
					}
				}
				tempText.append(syllables[j]);
				offset += syllables[j].length();
			}
			Token temp = new Token(Token.TERM, tempText.toString());
			temp.setStartOffset(token.getStartOffset());
			temp.setEndOffset(token.getEndOffset());
			temp.setCaption(token.getCaption());
			result.add(i + 1, temp);
		}
	}

}
