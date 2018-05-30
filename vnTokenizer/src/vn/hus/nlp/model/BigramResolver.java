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
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

import vn.hus.nlp.tokenizer.HasText;

import com.google.common.base.Charsets;
import com.google.common.base.Objects;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;

/**
 * @author LE Hong Phuong
 *         <p>
 *         20 mars 07
 *         <p>
 *         vn.hus.tokenizer
 *         <p>
 *         The ambiguity resolver for the lexical tokenizer. The resolver uses
 *         of a bigram model.
 */
public class BigramResolver extends AbstractResolver {

	private Map<Pair, Integer> bigramFreqs;
	private Map<String, Integer> unigramFreqs;
	private double total, count;

	/**
	 * Lambda 1
	 */
	private static double LAMBDA1 = 0.996;
	/**
	 * Lambda 2
	 */
	private static double LAMBDA2 = 0.004;
	
	private static double EPSILON = 1E-100;

	public BigramResolver(URL bigramUrl, URL unigramUrl) throws IOException {
		this(Resources.newReaderSupplier(bigramUrl, Charsets.UTF_8),
				Resources.newReaderSupplier(unigramUrl, Charsets.UTF_8));
	}

	public BigramResolver(String bigramPath, String unigramPath) throws IOException {
		this(new File(bigramPath), new File(unigramPath));
	}
	
	public BigramResolver(File bigramFile, File unigramFile) throws IOException {
		this(Files.newReaderSupplier(bigramFile, Charsets.UTF_8), 
				Files.newReaderSupplier(unigramFile, Charsets.UTF_8));
	}

	public BigramResolver(InputSupplier<InputStreamReader> bigramReaderSupplier,
			InputSupplier<InputStreamReader> unigramReaderSupplier)
			throws IOException {
		Reader bigramReader = null;
		Reader unigramReader = null;
		try {
			bigramReader = bigramReaderSupplier.getInput();
			unigramReader = unigramReaderSupplier.getInput();
			read(bigramReader, unigramReader);
		} finally {
			Closeables.closeQuietly(bigramReader);
			Closeables.closeQuietly(unigramReader);
		}
	}

	/**
	 * 
	 * @param probFilename
	 *            a conditional probability filename
	 * @param unigramFilename
	 *            unigram filename
	 * @throws IOException 
	 */
	public BigramResolver(Reader bigramReader, Reader unigramReader) throws IOException {
		read(bigramReader, unigramReader);
	}
	
	public BigramResolver(Reader bigramReader, Reader unigramReader, double lambda1, double lambda2, double epsilon) throws IOException {
		LAMBDA1 = lambda1;
		LAMBDA2 = lambda2;
		EPSILON = epsilon;
		read(bigramReader, unigramReader);
	}

	private void read(Reader bigramReader, Reader unigramReader)
			throws IOException {
		total = count = 0;
		JDomNGramReader reader = new JDomNGramReader();
		
		bigramFreqs = reader.readBigram(bigramReader);
		total += reader.getTotalFreq();
		count += bigramFreqs.size();

		unigramFreqs = reader.readUnigram(unigramReader);
		total += reader.getTotalFreq();
		count += unigramFreqs.size();
	}
	
	public double getUnigramProb(String word){
		int freq = Objects.firstNonNull(unigramFreqs.get(word), 0);
		double result = (freq + EPSILON) / (total + count);
		return Math.log(result);
	}

	private double computeUnigramProb(HasText word) {
		int freq = Objects.firstNonNull(unigramFreqs.get(word.getText()), 0);
		return (freq + EPSILON) / (total + count);
	}
	
	public double getBigramProb(String segment1, String segment2){
		int bigramFreq = Objects.firstNonNull(bigramFreqs.get(
				new Pair(segment1, segment2)), 0);
		int firstFreq = Objects.firstNonNull(unigramFreqs.get(segment1), 0);
		int secondFreq = Objects.firstNonNull(unigramFreqs.get(segment2), 0);

		double result = LAMBDA1 * (bigramFreq + EPSILON/*1*/) / (firstFreq + 1) + 
						LAMBDA2 * (secondFreq + 1) / (total + count);
		return Math.log(result);
	}

	private double computeBigramProb(HasText segment1, HasText segment2) {
		int bigramFreq = Objects.firstNonNull(bigramFreqs.get(
				new Pair(segment1.getText(), segment2.getText())), 0);
		int firstFreq = Objects.firstNonNull(unigramFreqs.get(segment1.getText()), 0);
		int secondFreq = Objects.firstNonNull(unigramFreqs.get(segment2.getText()), 0);

		return LAMBDA1 * (bigramFreq + EPSILON) / (firstFreq + 1) + 
				LAMBDA2 * (secondFreq + 1) / (total + count);
	}

	public <T extends HasText> int resolve(ArrayList<T[]> segmentations) {
		int choice = -1;
		double maxProb = -Double.MAX_VALUE;
		for (int i = 0; i < segmentations.size(); i++) {
			T[] segmentation = segmentations.get(i);
			double prob = Math.log(computeUnigramProb(segmentation[0]));
			for (int j = 1; j < segmentation.length; j++) {
				prob += Math.log(computeBigramProb(segmentation[j - 1], segmentation[j]));
			}
			if (prob >= maxProb) {
				maxProb = prob;
				choice = i;
			}
		}
		return choice;
	}
	
	public Set<String> getUnigramWords(){
		return unigramFreqs.keySet();
	}
	
}