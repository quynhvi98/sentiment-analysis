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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map.Entry;

public class NGramWriter {

	public void write(HashMap<LinkedList<String>, Integer> gramMap, String file) {
		BufferedWriter writer = null;

		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"yes\"?>\n");
			writer.write("<corpus>\n");
			writer.write("<body>\n");
			for (Entry<LinkedList<String>, Integer> pair : gramMap.entrySet()) {
				writer.write(String.format("<w msd=\"%d\">", pair.getValue()));
				ListIterator<String> e = pair.getKey().listIterator();
				writer.write(e.next());
				while (e.hasNext()) {
					writer.write(String.format(",%s", e.next()));
				}
				writer.write("</w>\n");
			}
			writer.write("</body>\n");
			writer.write("</corpus>\n");
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
