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

import java.util.ArrayList;
import java.util.List;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonToken;

public class TokenizerUtils {

	/*public static String toString(Tokenizer tokenizer) {
		StringBuilder sb = new StringBuilder();
		while (tokenizer.hasNext()) {
			Token token = tokenizer.next();
			sb.append(token.getText().replace(' ', '_'));
			if (".".equals(token.getText())) {
				sb.append('\n');
			} else {
				sb.append(' ');
			}
		}
		return sb.toString();
	}*/
	
	public static String toString(Tokenizer tokenizer) {
		StringBuilder sb = new StringBuilder();
		while (tokenizer.hasNext()) {
			Token token = tokenizer.next();
			if (token.getType() != Token.SPACE)
				sb.append(token.getText().replace(' ', '_'));
			else
				sb.append(token.getText());
		}
		return sb.toString();
	}
	
	public static String trimUnderscore(String input) {
		VLexer lexer = new VLexer(new ANTLRStringStream(input));
		StringBuilder sb = new StringBuilder();
		CommonToken previous = (CommonToken) lexer.nextToken(), current = null, next = null;
		
		if (previous.getType() == VLexer.EOF) 
			return null;
		
		sb.append(previous.getText());
		current = (CommonToken) lexer.nextToken();
		
		while (current.getType() != VLexer.EOF) {
			next = (CommonToken) lexer.nextToken();
			
			if (current.getType() == VLexer.UNDERSCORE){
				if ( (previous.getType() > 32 || previous.getType() == VLexer.NUMBER)
					&& (next.getType() > 32 || next.getType() == VLexer.NUMBER))
					sb.append("_");
			}
			else
				sb.append(current.getText());
			
			previous = current;
			current = next;
		}
		return sb.toString();
	}

	public static List<String> toStrings(Tokenizer tokenizer) {
		List<String> strings = new ArrayList<String>();
		while (tokenizer.hasNext()) {
			Token token = tokenizer.next();
			strings.add(token.getText());
		}
		return strings;
	}
	
	public static List<Token> toList(Tokenizer tok) {
		ArrayList<Token> tokens = new ArrayList<Token>();
		while (tok.hasNext()) {
			tokens.add(tok.next());
		}
		return tokens;
	}

	public static String[] toStringArray(Tokenizer tokenizer) {
		List<String> list = toStrings(tokenizer);
		return list.toArray(new String[list.size()]);
	}
	
	public static List<Token> toTokens(Tokenizer tokenizer) {
		ArrayList<Token> tokens = new ArrayList<Token>();
		while (tokenizer.hasNext()) {
			tokens.add(tokenizer.next());
		}
		return tokens;
	}

	public static Token[] toTokenArray(String[] strings) {
		Token[] tokenArr = new Token[strings.length];
		for (int i = 0; i < strings.length; i++) {
			tokenArr[i] = new Token(strings[i]);
		}
		return tokenArr;
	}
	
}
