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
package vn.hus.nlp.tokenizer.model;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;

import org.junit.Test;

import vn.hus.nlp.model.AbstractResolver;
import vn.hus.nlp.model.BigramResolver;
import vn.hus.nlp.tokenizer.Token;
import vn.hus.nlp.tokenizer.TokenizerUtils;

import com.google.common.collect.Lists;

/**
 * @author Lê Ngọc Minh
 *
 */
public class BigramResolverTest {

	@Test
	public void resolve() throws IOException {
		Token[] s1 = TokenizerUtils.toTokenArray(new String[] {
				"những", "sản phẩm", "này", "luôn", 
				"có", "giá thành", "cạnh tranh"
		});
		Token[] s2 = TokenizerUtils.toTokenArray(new String[] {
				"những", "sản phẩm", "này", "luôn", 
				"có giá", "thành", "cạnh tranh"
		});
		
		AbstractResolver resolver = getResolver("vnTokenizer/resources");
		int result = resolver.resolve(Lists.newArrayList(s1, s2));
		// it returns one wrong result, doesn't necessarily mean it's worse!
		Assert.assertEquals(1, result);
	}

	private static AbstractResolver getResolver(String resourceDir) throws IOException {
		return new BigramResolver(
				new File(resourceDir, "bigram/bigram.xml"), 
				new File(resourceDir, "bigram/unigram.xml"));
	}
	
}
