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
package vn.hus.nlp.model;

class IndexPair {
	public final int index1;
	public final int index2;

	public IndexPair(int index1, int index2) {
		this.index1 = index1;
		this.index2 = index2;
	}

	public boolean equals(Object obj) {
		if (obj instanceof IndexPair) {
			IndexPair p = (IndexPair) obj;
			return index1 == p.index1 && index2 == p.index2;
		}
		return false;
	}

	public int hashCode() {
		return index1 | index2;
	}

	public String toString() {
		return "(" + index1 + ", " + index2 + ")";
	}

}
