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
package vn.com.epi.vnlp.dfa;

import gnu.trove.iterator.TCharIntIterator;
import gnu.trove.map.TCharIntMap;
import gnu.trove.map.hash.TCharIntHashMap;

import org.apache.log4j.Logger;

/**
 * An attempt to increase performance but not success.
 * @deprecated Slower than {@link TransitionDictionary}
 * @author ngocminh.oss
 *
 */
public class AlphabetMappingTransitionMap implements TransitionMap {

	private static final Logger LOGGER = Logger
			.getLogger(AlphabetMappingTransitionMap.class);

	private static final char[] NON_CONTIGUOUS_CHARACTERS = { '.', ',', '/',
			'-', 'À', 'Á', 'Â', 'Ã', 'È', 'É', 'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô',
			'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â', 'ã', 'è', 'é', 'ê', 'ì', 'í',
			'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý', 'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ',
			'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ', 'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ',
			'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ', 'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ',
			'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ', 'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề',
			'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ', 'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ',
			'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ', 'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ',
			'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ', 'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ',
			'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ', 'ữ', 'Ự', 'ự', };
	private static final short[] ALPHABET_MAP;

	static {
		ALPHABET_MAP = new short[1 << 16 - 1];
		for (int i = 0; i < ALPHABET_MAP.length; i++) {
			ALPHABET_MAP[i] = -1;
		}
		short counter = 0;
		for (char i = 'a'; i < 'z'; i++) {
			ALPHABET_MAP[i] = counter++;
		}
		for (char i = 'A'; i < 'Z'; i++) {
			ALPHABET_MAP[i] = counter++;
		}
		for (int i = 0; i < NON_CONTIGUOUS_CHARACTERS.length; i++) {
			ALPHABET_MAP[NON_CONTIGUOUS_CHARACTERS[i]] = counter++;
		}
	}

	private int source;
	private int[] mainArr = new int[ALPHABET_MAP.length];
	private TCharIntMap auxiliaryMap = null;
	private int mainSize = 0;

	public AlphabetMappingTransitionMap(int source) {
		this.source = source;
	}

	public int getSource() {
		return source;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vn.com.epi.vnlp.dfa.TransitionMap#getAt(char)
	 */
	@Override
	public int getAt(char ch) {
		int target = -1;
		if (ALPHABET_MAP[ch] >= 0) {
			target = mainArr[ALPHABET_MAP[ch]];
		} else if (auxiliaryMap != null) {
			target = auxiliaryMap.get(ch);
		}
		return target;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see vn.com.epi.vnlp.dfa.TransitionMap#setAt(char, int)
	 */
	@Override
	public void setAt(char ch, int target) {
		if (ALPHABET_MAP[ch] >= 0) {
			if (mainArr[ALPHABET_MAP[ch]] == -1) {
				mainSize++;
			}
			mainArr[ALPHABET_MAP[ch]] = target;
		} else {
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Auxiliary character added: " + ch);
			}
			if (auxiliaryMap == null) {
				auxiliaryMap = new TCharIntHashMap();
			}
			auxiliaryMap.put(ch, target);
		}
	}

	@Override
	public Transition[] toArray() {
		final Transition[] transitionArr = new Transition[size()];
		int n = 0;
		for (int i = 0; i < mainArr.length; i++) {
			for (char ch = 'a'; ch < 'z'; ch++) {
				if (mainArr[ALPHABET_MAP[ch]] >= 0) {
					transitionArr[n++] = new Transition(ch, source, mainArr[ALPHABET_MAP[ch]]);
				}
			}
			for (char ch = 'A'; ch < 'Z'; ch++) {
				if (mainArr[ALPHABET_MAP[ch]] >= 0) {
					transitionArr[n++] = new Transition(ch, source, mainArr[ALPHABET_MAP[ch]]);
				}
			}
			for (int j = 0; i < NON_CONTIGUOUS_CHARACTERS.length; j++) {
				char ch = NON_CONTIGUOUS_CHARACTERS[j];
				if (mainArr[ALPHABET_MAP[ch]] >= 0) {
					transitionArr[n++] = new Transition(ch, source, mainArr[ALPHABET_MAP[ch]]);
				}
			}
		}
		if (auxiliaryMap != null) {
			for (TCharIntIterator it = auxiliaryMap.iterator(); it.hasNext(); ) {
				it.advance();
				transitionArr[n++] = new Transition(it.key(), source, it.value());
			}
		}
		return transitionArr;
	}

	public int size() {
		return mainSize + (auxiliaryMap == null ? 0 : auxiliaryMap.size());
	}
	
	public String getKeys(){
		StringBuffer result = new StringBuffer();
		for (char i = 'a'; i < 'z'; i++) {
			if (getAt(i) >= 0)
				result.append(i);
		}
		for (char i = 'A'; i < 'Z'; i++) {
			if (getAt(i) >= 0)
				result.append(i);
		}
		for (int i = 0; i < NON_CONTIGUOUS_CHARACTERS.length; i++) {
			if (getAt(NON_CONTIGUOUS_CHARACTERS[i]) >= 0)
				result.append(NON_CONTIGUOUS_CHARACTERS[i]);
		}
		return result.toString();
	}

}
