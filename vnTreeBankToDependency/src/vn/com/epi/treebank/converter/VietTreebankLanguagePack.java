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
package vn.com.epi.treebank.converter;

import edu.stanford.parser.nlp.trees.PennTreebankLanguagePack;

/**
 * @author Nguyen Vi Duong (vi.duong.bk@gmail.com)
 * 
 */
public class VietTreebankLanguagePack extends PennTreebankLanguagePack {

	private int postBasicCategoryIndex(String category) {
		boolean sawAtZero = false;
		char seenAtZero = '\u0000';
		int timeSaw = 0;
		int i = 0;
		for (int leng = category.length(); i < leng; i++) {
			char ch = category.charAt(i);
			if (Character.isDigit(ch)) {
				i--;
				break;
			}
			if (isLabelAnnotationIntroducingCharacter(ch)) {
				timeSaw++;
				if (i == 0) {
					sawAtZero = true;
					seenAtZero = ch;
				} else if (sawAtZero && i > 1 && ch == seenAtZero) {
					sawAtZero = false;
				} else {
					if (timeSaw == 2) {
						break;
					}
				}
			}
		}
		return i;
	}

	public String basicCategory(String category) {
		if (category == null) {
			return null;
		}
		return category.substring(0, postBasicCategoryIndex(category));
	}
}
