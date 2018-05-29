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

public class TaggedWord {
	/**
	 * A lexer rule
	 */
	private final LexerRule rule;
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
	public TaggedWord(LexerRule rule, String text, int line, int column) {
		this.rule = rule;
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
	public TaggedWord(String text)

	{
		this(null, text, -1, -1);
	}

	/**
	 * Create a lexer token from a lexer rule and a text.
	 * 
	 * @param rule
	 * @param text
	 */
	public TaggedWord(LexerRule rule, String text) {
		this(rule, text, -1, -1);
	}

	/**
	 * Return the rule that matched this token
	 * 
	 * @return the rule that match this token
	 */

	public LexerRule getRule() {
		return rule;
	}

	/**
	 * Return the text that matched by this token
	 * 
	 * @return the text matched by this token
	 */

	public String getText() {
		return text;
	}

	/**
	 * Test if this rule is a phrase rule. A phrase is processed by a lexical
	 * segmenter.
	 * 
	 * @return true/false
	 */

	public boolean getPhrase() {
		return rule != null && "phrase".equals(rule.Name);
	}

	public boolean getSimplePhrase() {
		return getPhrase() && text.indexOf(' ') < 0;
	}

	/**
	 * Test if this rule is a named entity rule.
	 * 
	 * @return true/false
	 */

	public boolean getNamedEntity() {
		return rule != null && rule.Name.startsWith("name");
	}

	/**
	 * @return true/false
	 */

	public boolean getDate() {
		return rule != null && rule.Name.startsWith("date");
	}

	/**
	 * @return true/false
	 */

	public boolean getDateDay() {
		return rule != null && rule.Name.contains("day");
	}

	/**
	 * @return true/false
	 */

	public boolean getDateMonth() {
		return rule != null && rule.Name.contains("month");
	}

	public boolean getDateYear() {
		return rule != null && rule.Name.contains("year");
	}

	public boolean getNumber() {
		return rule != null && rule.Name.startsWith("number");
	}

	/**
	 * @return Returns the column.
	 */

	public int getColumn() {
		return column;
	}

	/**
	 * @param column
	 *            The column to set.
	 */
	public void setColumn(int column) {
		this.column = column;
	}

	/**
	 * @return Returns the line.
	 */

	public int getLine() {

		return line;
	}

	/**
	 * @param line
	 *            The line to set.
	 */
	public void setLine(int line) {
		this.line = line;
	}

	/**
	 * Return a string representation of the token
	 */
	public String toString() {
		// return "[\"" + text + "\"" + " at (" + line + "," + column + ")]";
		// return rule.Name + ": " + text;
		return text.trim().replace(' ', '_');
	}

	public int hashCode() {
		return getText().hashCode();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (!(obj instanceof TaggedWord)) {
			return false;
		}
		// two lexer is considered equal if their text are equal.
		//
		return ((TaggedWord) obj).getText().equals(getText());
	}
}
