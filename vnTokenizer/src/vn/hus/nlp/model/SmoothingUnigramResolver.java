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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import vn.hus.nlp.tokenizer.HasText;

public class SmoothingUnigramResolver extends AbstractResolver {

	private HashMap<String, Integer> unigram;
	private double N, V;

	/**
	 * Default construtor.
	 * 
	 * @param unigramFilename
	 *            the unigram filename.
	 */
	public SmoothingUnigramResolver(String unigramFilename) {
		init();
		// load the unigram model.
		loadUnigram(unigramFilename);
	}

	private void init() {
		unigram = new HashMap<String, Integer>();
	}

	/**
	 * Load unigram model and calculate frequencies.
	 * 
	 * @param unigramFilename
	 *            the unigram filename
	 */
	private void loadUnigram(String unigramFilename) {
		System.out.println("Loading unigram model... ");
		N = 0;
		V = 0;
		// load unigram model
		SAXBuilder sax = new SAXBuilder();
		Document doc;

		try {
			doc = sax.build(new BufferedReader(new InputStreamReader(new FileInputStream(unigramFilename), "utf-8")));
			Element root = doc.getRootElement();
			List childrenLv1 = root.getChildren();
			Iterator iteratorLv1 = childrenLv1.iterator();

			while (iteratorLv1.hasNext()) {
				Element childLv1 = (Element) iteratorLv1.next();
				List childrenLv2 = childLv1.getChildren();
				Iterator iteratorLv2 = childrenLv2.iterator();
				while (iteratorLv2.hasNext()) {
					Element childLv2 = (Element) iteratorLv2.next();
					if (childLv2.getName().equals("w")) {
						int freq = Integer.parseInt(childLv2.getAttributeValue("msd"));
						String word = childLv2.getValue();
						unigram.put(word, freq);
						N += freq;
						V += 1;
					}
				}
			}
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("OK");
		System.out.println("N=" + N + ", V=" + V);
	}

	/**
	 * Unigram resolver for segmentations. Given a list of segmentations, this
	 * method calculates the probabilites of segmentations and choose the one
	 * with highest probabilities. Since in the unigram model, the probabilities
	 * of words are represented by their frequencies, we can calculate and
	 * compare the sum of frequencies of words of segmentations. It is always
	 * much more rapid to perform simple operations (like addition) on integers
	 * than complex operations (like multiply or division) on doubles.
	 * 
	 * @see vn.hus.nlp.tokenizer.segmenter.AbstractResolver#resolve(java.util.List)
	 */

	public <T extends HasText> int resolve(ArrayList<T[]> segmentations) {
		int choice = -1;
		double maxProb = Double.MIN_VALUE;
		for (int i = 0; i < segmentations.size(); i++) {
			T[] segmentation = segmentations.get(i);
			double prob = 0;
			for (int j = 0; j < segmentation.length; j++) {
				String word = segmentation[j].getText();
				int wordFreq = 0;
				if (unigram.containsKey(word)) {
					wordFreq = unigram.get(word);
				}
				prob += Math.log((wordFreq + 1) / (N + V));
			}
			if (prob >= maxProb) {
				maxProb = prob;
				choice = i;
			}
		}
		return choice;
	}
}
