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
package vn.com.epi.gate.tokenizer;

import gate.AnnotationSet;
import gate.Factory;
import gate.FeatureMap;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleResource;
import gate.util.InvalidOffsetException;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import vn.com.epi.vnlp.dfa.DFA;
import vn.com.epi.vnlp.dfa.io.DfaSaxReader;
import vn.com.epi.vnlp.tokenizer.RecursiveTokenizer;
import vn.com.epi.vnlp.tokenizer.dictionary.DfaDictionary;
import vn.hus.nlp.model.AbstractResolver;
import vn.hus.nlp.model.BigramResolver;
import vn.hus.nlp.tokenizer.Token;
import vn.hus.nlp.tokenizer.segmenter.Segmenter;
import vn.hus.nlp.tokenizer.segmenter.StringNormalizer;

/**
 * @author Nguyen Vi Duong (vi.duong.bk@gmail.com)
 * 
 */
@CreoleResource(name = "Vietnamese Tokenizer", comment = "An tokenizer for Vietnamese")
public class TokenizerPlugin extends AbstractLanguageAnalyser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String doc;
	private String outputASName;

	private URL normalizationRuleUrl;
	private URL dfaUrl;
	private URL unigramUrl;
	private URL bigramUrl;
	private double lambda1;
	private double lambda2;
	private double epsilon;
	private Segmenter segmenter;
	private AbstractResolver resolver;
	private RecursiveTokenizer recursiveTokenizer;

	public Resource init() throws ResourceInstantiationException {
		try {
			doc = null;
			StringNormalizer normalizer = new StringNormalizer(
					toReader(normalizationRuleUrl));
			DFA dfa = DfaSaxReader.readURL(dfaUrl);
			segmenter = new Segmenter(new DfaDictionary(dfa), normalizer);
			segmenter.setNameConnectingEnabled(true);
			if (lambda1 == 0 && lambda2 == 0 && epsilon == 0) {
				resolver = new BigramResolver(toReader(bigramUrl),
						toReader(unigramUrl));
			} else {
				resolver = new BigramResolver(toReader(bigramUrl),
						toReader(unigramUrl), lambda1, lambda2, epsilon);
			}
			recursiveTokenizer = new RecursiveTokenizer(dfa,
					(BigramResolver) resolver);
			return this;
		} catch (IOException e) {
			throw new ResourceInstantiationException(e);
		}
	}

	private static Reader toReader(URL url) throws IOException {
		return new InputStreamReader(url.openStream(), "UTF-8");
	}

	public void execute() throws ExecutionException {
		if (outputASName != null && outputASName.equals(""))
			outputASName = null;
		AnnotationSet outputAS = (outputASName == null) ? document
				.getAnnotations() : document.getAnnotations(outputASName);
		doc = document.getContent().toString();
		recursiveTokenizer.initTokenize(doc);
		try {
			while (recursiveTokenizer.hasNext()) {
				Token token = recursiveTokenizer.next();
				int start = token.getStartOffset();
				int end = token.getEndOffset();
				FeatureMap fmap = Factory.newFeatureMap();
				fmap.put("string", token.getText());
				fmap.put("length", token.getText().length());

				if (token.getType() == Token.SPACE) {
					fmap.put("kind", "space");
					outputAS.add((long) start, (long) end, "SpaceToken", fmap);
				} else {
					fmap.put("kind", TypeTranslator.translate(token.getType()));
					String orth = CapTranslator.translate(token.getCaption());
					if (orth != null)
						fmap.put("orth", orth);
					outputAS.add((long) start, (long) end, "Token", fmap);
				}
			}
		} catch (InvalidOffsetException e) {
			throw new ExecutionException(e);
		}
	}

	public URL getNormalizationRuleUrl() {
		return normalizationRuleUrl;
	}

	public void setNormalizationRuleUrl(URL normalizationRuleUrl) {
		this.normalizationRuleUrl = normalizationRuleUrl;
	}

	public URL getDfaUrl() {
		return dfaUrl;
	}

	public void setDfaUrl(URL dfaUrl) {
		this.dfaUrl = dfaUrl;
	}

	public URL getUnigramUrl() {
		return unigramUrl;
	}

	public void setUnigramUrl(URL unigramUrl) {
		this.unigramUrl = unigramUrl;
	}

	public URL getBigramUrl() {
		return bigramUrl;
	}

	public void setBigramUrl(URL bigramUrl) {
		this.bigramUrl = bigramUrl;
	}

	public Double getLambda1() {
		return lambda1;
	}

	public void setLambda1(Double lambda1) {
		this.lambda1 = lambda1;
	}

	public Double getLambda2() {
		return lambda2;
	}

	public void setLambda2(Double lambda2) {
		this.lambda2 = lambda2;
	}

	public Double getEpsilon() {
		return epsilon;
	}

	public void setEpsilon(Double epsilon) {
		this.epsilon = epsilon;
	}
}
