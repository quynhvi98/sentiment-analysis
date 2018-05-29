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
package vn.hus.nlp.tokenizer;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import vn.com.epi.vnlp.dfa.io.DfaSaxReader;
import vn.com.epi.vnlp.tokenizer.dictionary.DfaDictionary;
import vn.com.epi.vnlp.tokenizer.dictionary.Dictionary;
import vn.hus.nlp.model.AbstractResolver;
import vn.hus.nlp.model.BigramResolver;
import vn.hus.nlp.model.UnigramResolver;
import vn.hus.nlp.tokenizer.segmenter.Segmenter;
import vn.hus.nlp.tokenizer.segmenter.StringNormalizer;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class DefaultTokenizerFactory implements TokenizerFactory {

	private Segmenter segmenter;
	private AbstractResolver resolver;

    /**
     * Create and fill a tokenizer factory with data.
     * 
     * @param resourceDir TODO
     * @param input
     * @param output
     * @throws IOException
     */
    public DefaultTokenizerFactory(String resourceDir) throws IOException {
        this(resourceDir, 2);
    }

    /**
     * Create and fill a tokenizer factory with data.
     * 
     * @param resourceDir TODO
     * @param input
     * @param output
     * @param nGramSize support only 1 or 2
     * @throws IOException
     */
    public DefaultTokenizerFactory(String resourceDir, int nGramSize) throws IOException {
		StringNormalizer normalizer = new StringNormalizer(Files.newReader(
				new File(resourceDir, "normalization/rules.txt"), Charsets.UTF_8));
		Dictionary dict = new DfaDictionary(DfaSaxReader.readFile(
				new File(resourceDir, "automata/dfaLexicon.xml")));
		segmenter = new Segmenter(dict, normalizer);
		if (nGramSize == 1) {
		    resolver = new UnigramResolver(
		            new File(resourceDir, "bigram/unigram.xml"));
        } else if (nGramSize == 2) {
    		resolver = new BigramResolver(
    				new File(resourceDir, "bigram/bigram.xml"), 
    				new File(resourceDir, "bigram/unigram.xml"));
        } else {
            throw new IllegalArgumentException("Unsupported n-gram size: " + nGramSize);
        }
	}

	/**
	 * @return the segmenter
	 */
	public Segmenter getSegmenter() {
		return segmenter;
	}
	
	/**
	 * @return the resolver
	 */
	public AbstractResolver getResolver() {
		return resolver;
	}
	
	@Override
	public Tokenizer getTokenizer(String sentence) {
		return new AutomataTokenizer(sentence, segmenter, resolver);
	}
	
	public Tokenizer getTokenizer(Reader reader) throws IOException {
		return new AutomataTokenizer(reader, segmenter, resolver);
	}
	
	public List<Token> run(String document) {
		AutomataTokenizer tokenizer = new AutomataTokenizer(document, segmenter, resolver);
		return TokenizerUtils.toList(tokenizer);
	}
	
	public List<String> tokenizeToStrings(String document) {
		AutomataTokenizer tokenizer = new AutomataTokenizer(document, segmenter, resolver);
		return TokenizerUtils.toStrings(tokenizer);
	}

}
