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

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.net.URL;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import vn.com.epi.vnlp.dfa.DFA;
import vn.com.epi.vnlp.dfa.DFABuilder;
import vn.com.epi.vnlp.dfa.TermData;

import com.google.common.base.Charsets;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;

public class DfaSaxReader extends DfaReader {

	public DfaSaxReader(Reader reader) {
		super(reader);
	}

	private static SAXParserFactory factory = SAXParserFactory.newInstance();
	
	@Override
	public DFA read() throws IOException {
		try {
			SAXParser parser = factory.newSAXParser();
			DFABuilder builder = new DFABuilder();
			parser.parse(new InputSource(reader), new Handler(builder));
			return builder.build();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private static class Handler extends DefaultHandler {

		private DFABuilder builder;

		public Handler(DFABuilder builder) {
			this.builder = builder;
		}
		
		@Override
		public void startElement(String uri, String localName, String qName,
				Attributes attributes) throws SAXException {
			if ("s".equals(qName)) {
				addState(attributes);
			} else if ("t".equals(qName)) {
				addTransition(attributes);
			}
		}

		private void addTransition(Attributes attributes) {
			int source = Integer.parseInt(attributes.getValue("src"));
			int target = Integer.parseInt(attributes.getValue("tar"));
			char symbol = attributes.getValue("inp").charAt(0);
			builder.addTransition(source, symbol, target);
		}

		private void addState(Attributes attributes) {
			int type = Integer.parseInt(attributes.getValue("type"));
			int id = Integer.parseInt(attributes.getValue("id"));
			TermData data = null;
			if (type == DFA.STATE_TYPE_FINAL) {
				boolean isStopWord = Boolean.parseBoolean(attributes.getValue("stopWord"));
				boolean isName = Boolean.parseBoolean(attributes.getValue("name"));
				data = TermData.valueOf(isStopWord, isName);
			}
			builder.addState(id, type, data);
		}
		
	}
	
	public static DFA readFile(String path) throws IOException {
		return readFile(new File(path));
	}

	public static DFA readFile(File file) throws IOException {
		return read(Files.newReaderSupplier(file, Charsets.UTF_8));
	}

	public static DFA readURL(URL url) throws IOException {
		return read(Resources.newReaderSupplier(url, Charsets.UTF_8));
	}
	
	public static DFA read(InputSupplier<? extends Reader> readerSupplier) throws IOException {
		DfaReader dfaReader = null;
		try {
			dfaReader = new DfaSaxReader(readerSupplier.getInput());
			return dfaReader.read();
		} finally {
			Closeables.closeQuietly(dfaReader);
		}
	}
	
}
