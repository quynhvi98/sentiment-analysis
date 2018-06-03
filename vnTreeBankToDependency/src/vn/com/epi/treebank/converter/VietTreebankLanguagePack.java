package vn.com.epi.treebank.converter;

import edu.stanford.parser.nlp.trees.PennTreebankLanguagePack;

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
