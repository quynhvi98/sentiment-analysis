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
package vn.hus.nlp.tokenizer;

import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.antlr.runtime.ANTLRReaderStream;
import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonToken;

import vn.hus.nlp.model.AbstractResolver;
import vn.hus.nlp.tokenizer.segmenter.Segmenter;

public class AutomataTokenizer implements Tokenizer {
	
	private VLexer lexer = new VLexer();
	private LinkedList<Token> result = new LinkedList<Token>();
	private ResultMerger merger = new ResultMerger();
	private Segmenter segmenter;
	private AbstractResolver resolver;
	private boolean endOfReader = false;

	public AutomataTokenizer(Reader reader, Segmenter segmenter, AbstractResolver resolver) throws IOException {
		this.resolver = resolver;
		this.segmenter = segmenter;
		lexer = new VLexer(new ANTLRReaderStream(reader));
	}

	public AutomataTokenizer(String document, Segmenter segmenter, AbstractResolver resolver) {
		this.resolver = resolver;
		this.segmenter = segmenter;
		lexer = new VLexer(new ANTLRStringStream(document));
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
		if (result.size() <= 0 && !endOfReader) {
			tryTokenize();
		}
		return result.size() > 0;
	}

	private void tryTokenize() {
		CommonToken token = nextLexerToken();
		while (token.getType() != VLexer.PUNCTUATION && token.getType() != VLexer.EOF) {
			if (token.getType() == VLexer.WORD_UPPER || token.getType() == VLexer.WORD_LOWER) {
				LinkedList<CommonToken> syllables = new LinkedList<CommonToken>();
				while (token.getType() == VLexer.WORD_UPPER || token.getType() == VLexer.WORD_LOWER) {
					syllables.addLast(token);
					token = nextLexerToken();
				}
	
				CommonToken[] temp = new CommonToken[syllables.size()];
				ArrayList<Token[]> segmentationTokens = segmenter.segment(syllables.toArray(temp));
				int index = segmentationTokens.size() > 1 ? resolver.resolve(segmentationTokens) : 0;
				Token[] tokens = segmentationTokens.get(index);
				for (int j = 0; j < tokens.length; j++) {
					addResult(tokens[j]);
				}
			}
			else {
				int type = Token.UNKNOWN;
				switch (token.getType()) {
				case VLexer.ENTITY:
					type = Token.ENTITY;
					break;
				case VLexer.WORD_ALL_CAPS:
				case VLexer.WORD_OTHER:
					type = Token.TERM;
					break;
				case VLexer.NUMBER:
					type = Token.NUMBER;
					break;
				}
				addResult(fromLexerToken(token, type));
				token = nextLexerToken();
			}
		}
		merger.mergeList(result);
		
		while (token.getType() == VLexer.PUNCTUATION) {
			addResult(fromLexerToken(token, Token.PUNCTUATION));
			token = nextLexerToken();
		}
		stepBack();
	}

	private Token fromLexerToken(CommonToken token, int type) {
		Token tempToken = new Token(type, token.getText());
		tempToken.setStartOffset(token.getStartIndex());
		tempToken.setEndOffset(token.getStopIndex() + 1);
		tempToken.setCaption(token.getType());
		return tempToken;
	}

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

	private boolean steppedBack = false;
	private CommonToken savedToken = null;

	private CommonToken nextLexerToken() {
		if (steppedBack) {
			steppedBack = false;
			return savedToken;
		}
		do {
			savedToken = (CommonToken) lexer.nextToken();
			if (savedToken.getType() == VLexer.SPACE)
				addResult(fromLexerToken(savedToken, Token.SPACE));
		}
		while (savedToken.getType() == VLexer.SPACE);
		return savedToken;
	}

	private void stepBack() {
		steppedBack = true;
	}

}
