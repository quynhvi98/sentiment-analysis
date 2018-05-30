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

import java.util.LinkedList;

/**
 * @author phuonglh
 *         <p>
 *         This is a post-processor of vnTokeninzer. It corrects the
 *         tokenization result by performing some fine-tuning operations, for
 *         example, mergence of dates.
 */
public class ResultMerger {

	private static String DAY_STRING_1 = "ngày";
	private static String DAY_STRING_2 = "Ngày";

	private static String MONTH_STRING_1 = "tháng";
	private static String MONTH_STRING_2 = "Tháng";

	private static String YEAR_STRING_1 = "năm";
	private static String YEAR_STRING_2 = "Năm";

	public ResultMerger() {

	}

	private Token mergeDateDay(Token day, Token nextToken) {
		Token token = null;
		if (nextToken.isDateDay()) {
			String text = day.getText() + " " + nextToken.getText();
			token = new Token(Token.TERM, text, nextToken.getLine(), day.getColumn());
		}
		return token;
	}

	private Token mergeDateMonth(Token month, Token nextToken) {
		Token token = null;
		if (nextToken.getDateMonth()) {
			String text = month.getText() + " " + nextToken.getText();
			token = new Token(Token.TERM, text, nextToken.getLine(), month.getColumn());
		}
		return token;
	}

	private Token mergeDateYear(Token year, Token nextToken) {
		Token token = null;
		if (nextToken.getDateYear()) {
			String text = year.getText() + " " + nextToken.getText();
			token = new Token(Token.TERM, text, nextToken.getLine(), year.getColumn());
		}
		return token;
	}

	/**
	 * @param token
	 * @param nextToken
	 * @return a lexer token merging from two tokens or <tt>null</tt>.
	 */
	private Token mergeDate(Token token, Token nextToken) {
		if (token.getText().equals(DAY_STRING_1) || token.getText().equals(DAY_STRING_2)) {
			return mergeDateDay(token, nextToken);
		}
		if (token.getText().equals(MONTH_STRING_1) || token.getText().equals(MONTH_STRING_2)) {
			return mergeDateMonth(token, nextToken);
		}
		if (token.getText().equals(YEAR_STRING_1) || token.getText().equals(YEAR_STRING_2)) {
			return mergeDateYear(token, nextToken);
		}
		return null;
	}

	/**
	 * Merge the result of the tokenization.
	 * 
	 * @param tokens
	 * @return a list of lexer tokens
	 */
	public LinkedList<Token> mergeList(LinkedList<Token> tokens) {
		LinkedList<Token> result = new LinkedList<Token>();
//		Token token = new Token(""); // a fake start token
//		for (Token nextToken : tokens) {
//			// try to merge the two tokens
//			Token mergedToken = mergeDate(token, nextToken);
//			// if they are merged
//			if (mergedToken != null) {
//				// System.out.println(mergedToken.Text); // DEBUG
//				mergedToken.setStartOffset(token.getStartOffset());
//				mergedToken.setEndOffset(nextToken.getEndOffset());
//				result.removeLast();
//				result.addLast(mergedToken);
//			}
//			else { // if they aren't merge
//				result.addLast(nextToken);
//			}
//			token = nextToken;
//		}
		if (tokens.size() > 1) {
			Token token = tokens.get(0);
			Token nextToken = tokens.get(1);
			Token mergedToken = mergeDate(token, nextToken);
			// if they are merged
			if (mergedToken != null) {
				// System.out.println(mergedToken.Text); // DEBUG
				mergedToken.setStartOffset(token.getStartOffset());
				mergedToken.setEndOffset(nextToken.getEndOffset());
				mergedToken.setCaption(token.getType());
				result.addLast(mergedToken);
			}
			else { // if they aren't merge
				result = tokens;
			}
		}
		else {
			result = tokens;
		}
		return result;
	}
}
