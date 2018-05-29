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
package vn.com.epi.gate.rule;

import vn.com.epi.gate.OrthoMatcherRule;

public class CallByLastName implements OrthoMatcherRule {

	@Override
	public boolean value(String s1, String s2) {
		String[] chunks1 = s1.split(" +");
		String[] chunks2 = s2.split(" +");
		if (chunks1[0].equals(chunks2[0])) {
			for (int i = 1; i < chunks2.length; i++) {
				if (Character.isUpperCase(chunks2[i].charAt(0))) {
					return false;
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return "Gọi theo họ, vd: Hồ chủ tịch";
	}
	
}
