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

import java.util.Arrays;
import java.util.LinkedList;

import org.apache.log4j.Logger;


public class MultiRangeTranslationMap implements TransitionMap {

	private static final Logger LOGGER = Logger.getLogger(MultiRangeTranslationMap.class);
	
	private static final char RANGE1_START = '\u0000';
	private static final char RANGE1_STOP = '\u00FF';
	private static final char RANGE2_START = '\u00C0';
	private static final char RANGE2_STOP = '\u01B0';
	private static final char RANGE3_START = '\u1EA0';
	private static final char RANGE3_STOP = '\u1EF9';
	private static final int MAX_OUTRANGE_CHARS = 100;

	private int[] range1;
	private int[] range2;
	private int[] range3;
	private Transition[] outranges;	
	
	//
		
	private int outrangeCount;	
	private int source;

	public MultiRangeTranslationMap(int source) {
		this.source = source;
	}
	
	@Override
	public int getSource() {
		return source;
	}

	private int[] createArray(int size) {
		int[] arr = new int[size];
		Arrays.fill(arr, -1);
		return arr;
	}	

	public int getAt(char ch) {
		if (ch >= RANGE1_START && ch <= RANGE1_STOP) {
			return range1 == null ? -1 : range1[ch - RANGE1_START];
		}
		if (ch >= RANGE2_START && ch <= RANGE2_STOP) {
			return range2 == null ? -1 : range2[ch - RANGE2_START];
		}
		if (ch >= RANGE3_START && ch <= RANGE3_STOP) {
			return range3 == null ? -1 : range3[ch - RANGE3_START];
		}
		if (outranges != null) {
			return search(ch);
		}
		return -1;
		
	}

	public void setAt(char ch, int value) {
		if (ch >= RANGE1_START && ch <= RANGE1_STOP) {
			if (range1 == null) {
				range1 = createArray(RANGE1_STOP - RANGE1_START + 1);
			}
			range1[ch - RANGE1_START] = value;
		}
		else if (ch >= RANGE2_START && ch <= RANGE2_STOP) {
			if (range2 == null) {
				range2 = createArray(RANGE2_STOP - RANGE2_START + 1);
			}
			range2[ch - RANGE2_START] = value;
		}
		else if (ch >= RANGE3_START && ch <= RANGE3_STOP) {
			if (range3 == null) {
				range3 = createArray(RANGE3_STOP - RANGE3_START + 1);
			}
			range3[ch - RANGE3_START] = value;
		}
		else {
			insert(ch, value);
			if (LOGGER.isDebugEnabled()) {
				LOGGER.debug("Out of ranges: " + ch); 
			}
		}
		
	}
	
	private void insert(char ch, int value) {
		if (outranges == null) {
			outranges = new Transition[MAX_OUTRANGE_CHARS];
		}
		int i = 0;
		while (i < outrangeCount && outranges[i].symbol < ch) {
			i++;
		}
		if (i < outrangeCount) {
			if (outranges[i].symbol == ch) {
				outranges[i] = new Transition(ch, 0, value);
				return;
			}
			for (int j = outrangeCount; j > i; j--) {
				outranges[j] = outranges[j - 1];
			}
		}
		outrangeCount++;
		outranges[i] = new Transition(ch, 0, value);
	}

	private int search(char ch) {
		int l = 0, r = outrangeCount - 1;
		while (l <= r) {
			int m = (l + r) / 2;
			int key = outranges[m].symbol;
			if (key > ch) {
				r = m - 1;
			}
			else if (key < ch) {
				l = m + 1;
			}
			else {
				return outranges[m].target;
			}
		}
		return -1;
	}
	
	
	public Transition[] toArray() {
		LinkedList<Transition> list = new LinkedList<Transition>();
		if (range1 != null) {
			for (int i = 0; i < range1.length; i++) {
				if (range1[i] >= 0) {
					list.add(new Transition((char) (i + RANGE1_START), source,
							range1[i]));
				}
			}
		}
		if (range2 != null) {
			for (int i = 0; i < range2.length; i++) {
				if (range2[i] >= 0) {
					list.add(new Transition((char) (i + RANGE2_START), source,
							range2[i]));
				}
			}
		}
		if (range3 != null) {
			for (int i = 0; i < range3.length; i++) {
				if (range3[i] >= 0) {
					list.add(new Transition((char) (i + RANGE3_START), source,
							range3[i]));
				}
			}
		}
		if (outranges != null) {
			for (int i = 0; i < outrangeCount; i++) {
				list.add(outranges[i]);
			}
		}
		return list.toArray(new Transition[list.size()]);	
	}	
	
	public String getKeys(){
		StringBuffer result = new StringBuffer();
		for (char i = RANGE1_START; i <= RANGE1_STOP; i++){
			if (getAt(i) >= 0)
				result.append(i);
		}
		for (char i = RANGE2_START; i <= RANGE2_STOP; i++){
			if (getAt(i) >= 0)
				result.append(i);
		}
		for (char i = RANGE3_START; i <= RANGE3_STOP; i++){
			if (getAt(i) >= 0)
				result.append(i);
		}
		return result.toString();
	}
}
