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
package vn.com.epi.vnlp.diacritics;

import java.io.FileReader;
import java.io.IOException;

import vn.com.epi.vnlp.dfa.DFA;
import vn.com.epi.vnlp.dfa.io.DfaSaxReader;
import vn.hus.nlp.model.BigramResolver;
import vn.hus.nlp.tokenizer.Tokenizer;

/**
 * @author Chu Thanh Quang
 *
 */
public class DiacriticsRestorerFactory {
	
	private DFA dfa;
	private BigramResolver resolver;
	private DiacriticsRestorer restorer;
	
	public DiacriticsRestorerFactory(String resourceDir) throws IOException {
		
		dfa = new DfaSaxReader(new FileReader(
				resourceDir + "/automata/dfaLexicon.xml")).read();
		resolver = new BigramResolver(new FileReader(
				resourceDir + "/bigram/bigram.xml"), new FileReader(
				resourceDir + "/bigram/unigram.xml"));
		restorer = new DiacriticsRestorer(dfa, resolver);
		//System.out.println("Key number=" + resolver.getUnigramKeys().size());
	}
	
	public Tokenizer getRestorer(String text){
		restorer.initTokenize(text);
		return restorer;
		
	}
	
	public int getNumberOfCalls(){
		return 0;
	}

}
