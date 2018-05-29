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

import org.antlr.runtime.ANTLRStringStream;
import org.junit.Assert;
import org.junit.Test;

import vn.hus.nlp.tokenizer.VLexer;

public class VLexerTest {

	@Test
	public void capitalization() {
		VLexer lexer = new VLexer(new ANTLRStringStream("thường Hoa HOATOÀNBỘ lUnGtuNG"));
		Assert.assertEquals(VLexer.WORD_LOWER, lexer.nextToken().getType());
		lexer.nextToken();
		Assert.assertEquals(VLexer.WORD_UPPER, lexer.nextToken().getType());
		lexer.nextToken();
		Assert.assertEquals(VLexer.WORD_ALL_CAPS, lexer.nextToken().getType());
		lexer.nextToken();
		Assert.assertEquals(VLexer.WORD_OTHER, lexer.nextToken().getType());
		Assert.assertEquals(VLexer.EOF, lexer.nextToken().getType());
	}
	
}
