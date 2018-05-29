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

/**
 * Le Hong Phuong, phuonglh@gmail.com
 */
package vn.hus.nlp.tokenizer;

/**
 * @author Le Hong Phuong, phuonglh@gmail.com
 *         <p>
 *         8 juil. 2009, 20:49:31
 *         <p>
 *         Some options of the tokenizer.
 */
public class TokenizerOptions {
	/**
	 * Use a sentence detector before tokenizing text or not.
	 */
	public static boolean USE_SENTENCE_DETECTOR = false;

	/**
	 * Use underscores for separating syllbles of words or not.
	 */
	public static boolean USE_UNDERSCORE = true;

	/**
	 * Export results as XML format or not.
	 */
	public static boolean XML_OUTPUT = false;

	/**
	 * Default file extension for tokenization.
	 */
	public static String TEXT_FILE_EXTENSION = ".txt";
}
