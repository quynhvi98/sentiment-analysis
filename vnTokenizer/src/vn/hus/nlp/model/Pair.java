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

public class Pair implements Comparable<Pair> {
	public final String first;
	public final String second;

	public Pair(String first, String second) {
		this.first = first;
		this.second = second;
	}

	public boolean equals(Object obj) {
		if (obj instanceof Pair) {
			Pair p = (Pair) obj;
			return first.equals(p.first) && second.equals(p.second);
		}
		return false;
	}

	public int hashCode() {
		int hash = first.hashCode();
		return (hash << 1) + (hash & 1) + second.hashCode();
	}

	public int compareTo(Pair other) {
		int c = first.compareTo(other.first);
		return c != 0 ? c : second.compareTo(other.second);
	}

	public String toString() {
		return "(" + first + ", " + second + ")";
	}
}
