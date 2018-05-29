/**
 * (C) Le Hong Phuong, phuonglh@gmail.com
 *  Vietnam National University, Hanoi, Vietnam.
 */
package vn.hus.nlp.tokenizer.segmenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.LinkedList;

/**
 * @author Le Hong Phuong, phuonglh@gmail.com
 *         <p>
 *         vn.hus.nlp.segmenter
 *         <p>
 *         Nov 15, 2007, 11:51:08 PM
 *         <p>
 *         An accent normalizer for Vietnamese string. The purpose of this class
 *         is to convert a syllable like "hòa" to "hoà", since the lexicon
 *         contains only the later form.
 */
public final class StringNormalizer {

	private String[] search;
	private String[] replacement;

	public StringNormalizer(Reader r) throws IOException {
		BufferedReader reader = null;
		try {
			LinkedList<String> search = new LinkedList<String>();
			LinkedList<String> replacement = new LinkedList<String>();
			reader = new BufferedReader(r);
			int i = 0;
			String rule;
			while ((rule = reader.readLine()) != null) {
				i++;
				String[] s = rule.split("\\s");
				if (s.length == 2) {
					search.addLast(s[0]);
					replacement.addLast(s[1]);
				} else {
					System.err.println("Wrong syntax in the map file at line " + i);
				}
			}
			this.search = (String[]) search.toArray(new String[search.size()]);
			this.replacement = (String[]) replacement.toArray(new String[replacement.size()]);
		} finally {
			if (reader != null) reader.close();
		}
	}

	/**
	 * Normalize a string.
	 * 
	 * @return a normalized string
	 * @param s
	 *            a string
	 */
	public String normalizeSyllable(String s) {
		for (int i = 0; i < search.length; i++) {
			int index;
			if ((index = s.indexOf(search[i])) >= 0) {
				s = s.substring(0, index) + replacement[i] + s.substring(index + search[i].length());
				break;
			}
		}
		return s;
	}

}
