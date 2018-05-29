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

import static org.junit.Assert.*;

import org.junit.Test;

import vn.com.epi.vnlp.tokenizer.dictionary.Dictionary;

public abstract class DictionaryTest {

	protected abstract Dictionary dictionary();
	
	@Test
	public void testOneSyllables() {
		assertTrue(accept("a"));
		assertTrue(accept("nghe"));
		assertTrue(accept("nghè"));
		assertTrue(accept("nghé"));
		assertFalse(accept("bb"));
		assertFalse(accept("cc"));
		assertFalse(accept("dd"));
	}
	
	@Test
	public void testTwoSyllables() {
		assertTrue(accept("nhà cửa"));
		assertTrue(accept("ách tắc"));
		assertTrue(accept("giao thông"));
		assertFalse(accept("ăn xong"));
		assertFalse(accept("bị bệnh"));
		assertFalse(accept("văn từ"));
	}
	
	@Test
	public void testThreeSyllables() {
		assertTrue(accept("xã hội hoá"));
		assertTrue(accept("xa bô chê"));
		assertTrue(accept("vườn bách thú"));
		assertTrue(accept("bất khả kháng"));
		assertFalse(accept("tôi đi chơi")); // 1 - 1 - 1
		assertFalse(accept("ăn uống xong")); // 2 - 1
		assertFalse(accept("con ngựa chiến")); // 1 - 2
	}

	@Test
	public void testFourSyllables() {
		assertTrue(accept("xã hội chủ nghĩa"));
		assertTrue(accept("ái nam ái nữ"));
		
		assertFalse(accept("luật bất thành văn")); // 1 - 3
		assertFalse(accept("văn bản pháp luật")); // 2 - 2
	}
	
	private boolean accept(String word) {
		String[] syllables = word.split("\\s+");
		boolean accepted = dictionary().acceptFirst(syllables[0]);
		for (int i = 1; dictionary().hasNext() && i < syllables.length; i++) {
			accepted = dictionary().acceptNext(syllables[i]);
		}
		return accepted;
	}
	
}
