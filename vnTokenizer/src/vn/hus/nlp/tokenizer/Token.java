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

public class Token implements HasText {

	public static final int UNKNOWN = -1;
	public static final int NUMBER = 0;
	public static final int NAME = 1;
	public static final int TERM = 2;
	public static final int PUNCTUATION = 4;
	public static final int SPACE = 5;
	public static final int ENTITY = 6;

	private int type;

	private boolean stopwordFlag;
	private boolean nameFlag;
	
	/**
	 * The text
	 */
	private final String text;

	/**
	 * The line location of the text in the file
	 */
	private int line;

	/**
	 * The column location of the text in the file
	 */
	private int column;

	private int startOffset;
	private int endOffset;
	private int caption;

	/**
	 * Create a LexerToken
	 * 
	 * @param rule
	 *            a rule
	 * @param text
	 *            the text
	 * @param line
	 *            the line location of the text in a file
	 * @param column
	 *            the column location of the text in a file
	 */
	public Token(int type, String text, int line, int column) {
		this.type = type;
		this.text = text;
		this.line = line;
		this.column = column;
	}

	/**
	 * Create a lexer token from a text
	 * 
	 * @param text
	 *            a text
	 */
	public Token(String text)

	{
		this(UNKNOWN, text, -1, -1);
	}

	/**
	 * Create a lexer token from a lexer rule and a text.
	 * 
	 * @param rule
	 * @param text
	 */
	public Token(int type, String text)

	{
		this(type, text, -1, -1);
	}

	public int getType() {
		return type;
	}

	/**
	 * Return the text that matched by this token
	 * 
	 * @return the text matched by this token
	 */

	public String getText() {
		return text;
	}

	public int getColumn() {
		return column;
	}

	/**
	 * @return Returns the line.
	 */

	public int getLine() {
		return line;
	}

	public int getStartOffset() {
		return startOffset;
	}

	public void setStartOffset(int startOffset) {
		this.startOffset = startOffset;
	}

	public int getEndOffset() {
		return endOffset;
	}

	public void setEndOffset(int endOffset) {
		this.endOffset = endOffset;
	}

	public int getCaption() {
		return caption;
	}

	public void setCaption(int caption) {
		this.caption = caption;
	}

	private int intValue;
	private boolean parsed;
	private boolean isInt;

	private boolean tryParse() {
		if (!parsed) {
			try {
				intValue = Integer.parseInt(text);
				isInt = true;
			}
			catch (NumberFormatException e) {
				isInt = false;
			}
			parsed = true;
		}
		return isInt;
	}

	public boolean isDateDay() {
		if (type != NUMBER || !tryParse()) {
			return false;
		}
		return intValue >= 1 && intValue <= 31;
	}

	public boolean getDateMonth() {
		if (type != NUMBER || !tryParse()) {
			return false;
		}
		return intValue >= 1 && intValue <= 12;
	}

	public boolean getDateYear() {
		if (type != NUMBER || !tryParse()) {
			return false;
		}
		return intValue >= 1;
	}

	/**
	 * Return a string representation of the token
	 */
	public String toString() {
		// return "[\"" + text + "\"" + " at (" + line + "," + column + ")]";
		// return rule.Name + ": " + text;
		return text.trim().replace(' ', '_');
	}

	public int getHashCode() {
		return getText().hashCode();
	}

	/*
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof Token)) {
			return false;
		}
		return ((Token) obj).getText().equals(getText());
	}

	public boolean isStopWord() {
		return stopwordFlag;
	}

	public void setStopWord(boolean stopword) {
		this.stopwordFlag = stopword;
	}

	public boolean isName() {
		return nameFlag;
	}

	public void setName(boolean name) {
		this.nameFlag = name;
	}
}
