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

import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import vn.com.epi.vnlp.dfa.DFA;
import vn.com.epi.vnlp.dfa.DFABuilder;
import vn.com.epi.vnlp.dfa.TermData;

public class DfaJdomReader extends DfaReader {

	public DfaJdomReader(Reader reader) {
		super(reader);
	}

	@Override
	public DFA read() {
		DFABuilder builder = new DFABuilder();
		SAXBuilder sax = new SAXBuilder();

		Document doc;
		try {
			doc = sax.build(reader);
			Element root = doc.getRootElement();
			List childrenLv1 = root.getChildren();
			Iterator iteratorLv1 = childrenLv1.iterator();
			while (iteratorLv1.hasNext()) {
				Element childLv1 = (Element) iteratorLv1.next();
				List childrenLv2 = childLv1.getChildren();
				Iterator iteratorLv2 = childrenLv2.iterator();
				while (iteratorLv2.hasNext()) {
					Element childLv2 = (Element) iteratorLv2.next();
					if (childLv2.getName().equals("s")) {
						int type = Integer.parseInt(childLv2.getAttributeValue("type"));
						int id = Integer.parseInt(childLv2.getAttributeValue("id"));
						TermData data = null;
						if (type == DFA.STATE_TYPE_FINAL) {
							boolean isStopWord = Boolean.parseBoolean(childLv2.getAttributeValue("stopWord"));
							boolean isName = Boolean.parseBoolean(childLv2.getAttributeValue("name"));
							data = TermData.valueOf(isStopWord, isName);
						}
						builder.addState(id, type, data);
					}
					else if (childLv2.getName().equals("t")) {
						int source = Integer.parseInt(childLv2.getAttributeValue("src"));
						int target = Integer.parseInt(childLv2.getAttributeValue("tar"));
						char symbol = childLv2.getAttributeValue("inp").charAt(0);
						builder.addTransition(source, symbol, target);
					}
				}
			}
		}
		catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return builder.build();
	}
}
