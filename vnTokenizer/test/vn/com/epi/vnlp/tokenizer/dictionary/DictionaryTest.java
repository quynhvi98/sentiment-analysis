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
