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
package vn.com.epi.vnlp.tokenizer.dictionary;


public interface Dictionary {

	/**
	 * Answer the question whether this dictionary accept the syllable as its
	 * word.
	 * 
	 * @param syllable
	 * @return
	 */
	boolean acceptFirst(String syllable);
	
	/**
	 * Answer the question whether this dictionary accept the word made by
	 * previous syllables (since the last call of {@link #acceptFirst(String)})
	 * plus provided syllable as its word.
	 * 
	 * @param syllable
	 * @return
	 */
	boolean acceptNext(String syllable);

	/**
	 * Answer the question whether this dictionary accept any other syllable,
	 * i.e. whether next calls of {@link #acceptNext(String)} ever return true.
	 * The implementation of this method should be as fast as a single
	 * comparison. If you cannot make it that fast, simply return true.
	 * 
	 * @return
	 */
	boolean hasNext();

	/**
	 * Get data associated with accepted word 
	 * @return
	 */
	Object getData();
	
}
