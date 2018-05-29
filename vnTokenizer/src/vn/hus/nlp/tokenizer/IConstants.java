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
 * (C) Le Hong Phuong, phuonglh@gmail.com
 * vn.hus.toolkit
 * 2006
 */
package vn.hus.nlp.tokenizer;

/**
 * @author LE Hong Phuong
 *         <p>
 *         31 déc. 06
 *         </p>
 *         Some predefined contants for vnTokenizer tool.
 * 
 */
public interface IConstants {
	/**
	 * Vietnamese word set
	 */
	public static final String WORD_SET = "data/dictionaries/words_v4.txt";

	/**
	 * The Vietnamese lexicon
	 */
	public static final String LEXICON = "data/dictionaries/words_v4.xml";

	/**
	 * The Vietnamese DFA lexicon
	 */
	public static final String LEXICON_DFA = "resources/automata/lexicon_dfa_minimal.xml";

	/**
	 * Lexer specification
	 */
	public static final String LEXER_SPECIFICATION = "resources/lexers/lexers.xml";
	/**
	 * Unigram model
	 */
	public static final String UNIGRAM_MODEL = "resources/bigram/unigram.xml";
	/**
	 * Bigram model
	 */
	public static final String BIGRAM_MODEL = "resources/bigram/bigram.xml";

	/**
	 * The named entity prefix.
	 */
	public static final String NAMED_ENTITY_PREFIX = "resources/prefix/namedEntityPrefix.xml";
}
