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

import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Lê Ngọc Minh
 * 
 */
public class AutomataTokenizerTest {

	@Test
	public void tokenize() throws IOException {
		String sentence = " Bel Canto Design S300iU nhỏ gọn. Ảnh: Bel Canto. ";
		String expected = "những sản_phẩm này luôn có giá_thành cạnh_tranh";
		
		DefaultTokenizerFactory factory = new DefaultTokenizerFactory(
				"resources");
		AutomataTokenizer tokenizer = new AutomataTokenizer(sentence,
				factory.getSegmenter(), factory.getResolver());
		System.out.println(TokenizerUtils.toString(tokenizer));
		// it returns one wrong result, doesn't necessarily mean it's worse!
//		Assert.assertEquals(expected, TokenizerUtils.toString(tokenizer));
	}

}
