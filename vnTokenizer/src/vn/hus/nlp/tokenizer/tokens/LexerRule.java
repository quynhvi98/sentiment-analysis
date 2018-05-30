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
package vn.hus.nlp.tokenizer.tokens;

import java.util.regex.Pattern;

public class LexerRule {
	public final String Name;
	public final Pattern Regex;

	public LexerRule(String name, Pattern regex) {
		this.Name = name;
		this.Regex = regex;
	}

	public LexerRule(String name, String pattern) {
		this(name, Pattern.compile(pattern));
	}

	public LexerRule(String name) {
		this(name, (Pattern) null);
	}
}
