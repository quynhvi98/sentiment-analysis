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

import java.util.HashMap;
import java.util.Map;

import vn.hus.nlp.tokenizer.Token;
/**
 * @author Nguyen Vi Duong (vi.duong.bk@gmail.com)
 * 
 */
public class TypeTranslator {
	private static final Map<Integer, String> typeMap;
	
	static {
		typeMap = new HashMap<Integer, String>();
		typeMap.put(Token.UNKNOWN, "unknown");
		typeMap.put(Token.NUMBER, "number");
		typeMap.put(Token.TERM, "word");
		typeMap.put(Token.ENTITY, "entity");
		typeMap.put(Token.PUNCTUATION, "punctuation");
	}
	
	public static String translate(int type) {
		String ret = typeMap.get(type);
		return ret == null ? "error" : ret;
	}
}
