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

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

public class PrefixReader {

	public ArrayList<String> read(String path) {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(path), "utf-8"));
		}
		catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return read(reader);
	}

	public ArrayList<String> read(BufferedReader reader) {
		SAXBuilder sax = new SAXBuilder();
		Document doc;
		ArrayList<String> prefixes = new ArrayList<String>();
		prefixes.clear();

		try {
			doc = sax.build(new BufferedReader(reader));

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
						prefixes.add(childLv2.getValue());
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
		return prefixes;

	}
}
