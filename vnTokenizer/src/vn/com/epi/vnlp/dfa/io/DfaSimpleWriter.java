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
package vn.com.epi.vnlp.dfa.io;

import java.io.Closeable;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import com.google.common.io.Closeables;

import vn.com.epi.vnlp.dfa.DFA;
import vn.com.epi.vnlp.dfa.Transition;
import vn.com.epi.vnlp.dfa.TransitionMap;

/**
 * Try to reduce lexicon file size but fail.
 * @author ngocminh.oss
 *
 */
public class DfaSimpleWriter implements Closeable {

	private Writer writer;
	
	public DfaSimpleWriter(Writer writer) {
		this.writer = writer;
	}

	public void write(DFA dfa) throws IOException {
		writer.write(dfa.getStartState() + "\n");
		writer.write(dfa.getStateCount() + "\n");
		for (int i = 0; i < dfa.getStateCount(); i++) {
			writer.write(dfa.getType(i) + "\n");
		}
		for (int i = 0; i < dfa.getStateCount(); i++) {
			TransitionMap transitions = dfa.getTransitions(i);
			if (transitions == null) {
				continue;
			}
			Transition[] arr = transitions.toArray();
			for (Transition t : arr) {
				writer.write(t.source + " " + t.target + " " + t.symbol + "\n");
			}
		}
	}

	public void close() throws IOException {
		writer.close();
	}
	
	public static void main(String[] args) throws IOException {
		DFA dfa = DfaSaxReader.readFile("resources/automata/dfaLexicon.xml");
		String path = "test.txt";
		writeFile(dfa, path);
	}

	private static void writeFile(DFA dfa, String path) throws IOException {
		DfaSimpleWriter writer = null;
		try {
			writer = new DfaSimpleWriter(new FileWriter(path));
			writer.write(dfa);
		} finally {
			Closeables.closeQuietly(writer);
		}
	}
	
}
