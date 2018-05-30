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
package vn.hus.nlp.tokenizer.tokens;

/**
 * @author LE Hong Phuong, phuonglh@gmail.com
 *         <p>
 *         A word token. It is a lexer token with an additional information -
 *         the part of speech. But in general, we do not use this information.
 * 
 */
public class WordToken extends TaggedWord {

	private final String pos;

	/**
	 * Instantiate a new token
	 * 
	 * @param rule
	 *            a lexical rule
	 * @param text
	 *            the text
	 * @param line
	 *            line location of the text in a file
	 * @param column
	 *            column position of the text in a file
	 */
	public WordToken(LexerRule rule, String text, int line, int column) {
		super(rule, text, line, column);
		pos = null;
	}

	/**
	 * Instantiate a new token
	 * 
	 * @param rule
	 *            a lexical rule
	 * @param text
	 *            the text
	 * @param line
	 *            line location of the text in a file
	 * @param column
	 *            column position of the text in a file
	 * @param pos
	 *            parts-of-speech of the word token
	 */
	public WordToken(LexerRule rule, String text, int line, int column, String pos) {
		super(rule, text, line, column);
		this.pos = pos;
	}

	/**
	 * Get the parts-of-speech of the token
	 * 
	 * @return parts-of-speech of the token
	 */
	public String getPOS() {
		return pos;
	}

	/**
	 * Return a string representation of a word token
	 */
	@Override
	public String toString() {
		return super.toString();
	}
}
