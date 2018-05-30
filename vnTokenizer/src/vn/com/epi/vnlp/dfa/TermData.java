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

public class TermData {
	private final boolean stopWordFlag;
	private final boolean nameFlag;

	private TermData(boolean isStopWord, boolean isName) {
		stopWordFlag = isStopWord;
		nameFlag = isName;
	}

	private static TermData[] INSTANCES = {
			new TermData(false, false), new TermData(false, true),
			new TermData(true, false), new TermData(true, true),
	};

	public String toString() {
		return String.format("[stop word: %b, name: %b]", isStopWord(), isName());
	}

	public static TermData valueOf(boolean isStopWord, boolean isName) {
		return INSTANCES[(isStopWord ? 2 : 0) + (isName ? 1 : 0)];
	}

	public boolean isName() {
		return nameFlag;
	}

	public boolean isStopWord() {
		return stopWordFlag;
	}
}
