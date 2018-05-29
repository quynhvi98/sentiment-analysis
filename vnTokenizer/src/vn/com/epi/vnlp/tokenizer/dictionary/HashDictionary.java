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
package vn.com.epi.vnlp.tokenizer.dictionary;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

public class HashDictionary implements Dictionary {

	private List<Map<String, Object>> wordSets = new ArrayList<Map<String, Object>>();
	private String current = "";
	private final Object marker = new Object();
	private Object data;
	
	public HashDictionary() {
	}
	
	public void add(String word) {
		int length = word.length();
		while (wordSets.size() <= length) {
			wordSets.add(new HashMap<String, Object>());
		}
		wordSets.get(length).put(word, marker);
	}
	
	@Override
	public boolean acceptFirst(String syllable) {
		current = syllable;
		return accept();
	}

	@Override
	public boolean acceptNext(String syllable) {
		current = current + " " + syllable;
		return accept();
	}

	private boolean accept() {
		if (wordSets.size() <= current.length()) {
			return false;
		}
		data = wordSets.get(current.length()).get(current);
		return data != null;
	}

	/**
	 * TODO this method may be important for performance
	 */
	@Override
	public boolean hasNext() {
		return true;
	}
	
	/* (non-Javadoc)
	 * @see vn.com.epi.vnlp.tokenizer.dictionary.Dictionary#getData()
	 */
	@Override
	public Object getData() {
		return data == marker ? null : data;
	}

	public static HashDictionary fromFile(String path) throws IOException {
		return fromFile(new File(path));
	}

	public static HashDictionary fromFile(File file) throws IOException {
		final HashDictionary dict = new HashDictionary();
		Files.readLines(file, Charsets.UTF_8, new LineProcessor<Void>() {
			@Override
			public boolean processLine(String line) throws IOException {
				dict.add(line.trim());
				return true;
			}

			@Override
			public Void getResult() {
				return null;
			}
		});
		return dict;
	}

}
