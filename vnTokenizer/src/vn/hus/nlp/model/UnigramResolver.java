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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import vn.hus.nlp.tokenizer.HasText;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class UnigramResolver extends AbstractResolver {

	private HashMap<String, Integer> unigram;

	public UnigramResolver(File unigramFile) {
		init();
		loadUnigram(unigramFile);
		
	}

	/**
	 * 
	 * @param unigramFilename
	 *            the unigram filename.
	 */
	public UnigramResolver(String unigramFilename) {
		this(new File(unigramFilename));
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
	private void loadUnigram(File unigramFile) {
		System.out.println("Loading unigram model... ");
		// load unigram model
		SAXBuilder sax = new SAXBuilder();
		Document doc;

		try {
			doc = sax.build(Files.newReader(unigramFile, Charsets.UTF_8));
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
		int maxFrequency = 0;
		for (int i = 0; i < segmentations.size(); i++) {
			T[] segmentation = segmentations.get(i);
			int frequency = 0;
			for (int j = 0; j < segmentation.length; j++) {
				String word = segmentation[j].getText();
				int wordFreq = 0;
				if (unigram.containsKey(word)) {
					wordFreq = unigram.get(word);
				}
				frequency += wordFreq;
			}
			if (frequency >= maxFrequency) {
				maxFrequency = frequency;
				choice = i;
			}
		}
		return choice;
	}
}
