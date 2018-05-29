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
import java.io.Reader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Lê Ngọc Minh
 *
 */
public class JDomNGramReader {

	private static SAXParserFactory factory = SAXParserFactory.newInstance();
	
	private double totalFreq;
	private double totalReadFreq;

	public double getTotalFreq() {
		return totalFreq;
	}

	public double getTotalReadFreq() {
		return totalReadFreq;
	}

	public Map<String, Integer> readUnigram(Reader reader) throws IOException {
		try {
			totalFreq = 0;
			HashMap<String, Integer> unigramFreqs = new HashMap<String, Integer>();

			System.out.print("Loading unigram model... ");
			SAXParser parser = factory.newSAXParser();
			parser.parse(new InputSource(reader), new UnigramHandler(unigramFreqs));
			System.out.println("OK");

			return unigramFreqs;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Load a probability file and initialize the <code>probabilities</code> map.
	 * @param reader
	 * @return
	 * @throws IOException
	 */
	public Map<Pair, Integer> readBigram(Reader reader) throws IOException {
		try {
			System.out.print("Loading bigram model... ");
			HashMap<Pair, Integer> bigramFreqs = new HashMap<Pair, Integer>();
			totalFreq = 0;

			SAXBuilder sax = new SAXBuilder();
			Document doc = sax.build(reader);
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
						String twoWords = childLv2.getValue();
						String[] words = twoWords.split(",");
						if (words.length == 2) {
							bigramFreqs.put(new Pair(words[0], words[1]), freq);
							totalFreq += freq;
						}
					}
				}
			}
			System.out.println("OK");
			return bigramFreqs;
		} catch (JDOMException e) {
			throw new RuntimeException(e);
		}
	}

	public Map<LinkedList<String>, Integer> read(String file) {
		HashMap<LinkedList<String>, Integer> ngramFreqs = new HashMap<LinkedList<String>, Integer>();
		load(ngramFreqs, file);
		return ngramFreqs;
	}

	public int load(Map<LinkedList<String>, Integer> ngramFreqs, String file) {
		System.out.println("Loading n-gram model... ");
		totalReadFreq = 0;
		int count = 0;
		// load unigram model
		SAXBuilder sax = new SAXBuilder();
		Document doc;

		try {
			doc = sax.build(new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8")));
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

						String[] chunks = childLv2.getValue().split(",");
						LinkedList<String> ngram = new LinkedList<String>();
						for (String word : chunks) {
							ngram.addLast(word.trim());
						}
						int currFreq = ngramFreqs.get(ngram);
						ngramFreqs.put(ngram, currFreq + freq);

						totalReadFreq += freq;
						count++;
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
		return count;
	}

	private class UnigramHandler extends DefaultHandler {

		private static final int START = 0;
		private static final int IN_W = 1;
		
		private int state = START;
		private int freq;
		private HashMap<String, Integer> unigramFreqs;
		private StringBuilder word;
		
		public UnigramHandler(HashMap<String, Integer> unigramFreqs) {
			this.unigramFreqs = unigramFreqs;
		}

		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("w".equals(qName)) {
				state = IN_W;
				freq = Integer.parseInt(attributes.getValue("msd"));
				word = new StringBuilder();
			}
		}
		
		@Override
		public void endElement(String uri, String localName, String qName)
				throws SAXException {
			if ("w".equals(qName)) {
				unigramFreqs.put(word.toString(), freq);
				totalFreq += freq;
				state = START;
			}
		}
		
		@Override
		public void characters(char[] ch, int start, int length)
				throws SAXException {
			if (state == IN_W) 
				word.append(String.valueOf(ch, start, length));
		}
		
	}


}
