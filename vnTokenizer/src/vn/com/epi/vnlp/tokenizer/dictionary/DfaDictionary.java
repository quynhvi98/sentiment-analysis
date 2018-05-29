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

import vn.com.epi.vnlp.dfa.DFA;

public class DfaDictionary implements Dictionary {

	private DFA dfa;
	
	public DfaDictionary(DFA dfa) {
		super();
		this.dfa = dfa;
	}

	@Override
	public boolean acceptFirst(String syllable) {
		return dfa.accept(syllable);
	}

	@Override
	public boolean acceptNext(String syllable) {
		dfa.accept(" ", dfa.getLastState());
		return dfa.accept(syllable, dfa.getLastState());
	}
	
	@Override
	public boolean hasNext() {
		return dfa.getLastState() >= 0;
	}

	/* (non-Javadoc)
	 * @see vn.com.epi.vnlp.tokenizer.dictionary.Dictionary#getData()
	 */
	@Override
	public Object getData() {
		return dfa.getLastData();
	}

}
