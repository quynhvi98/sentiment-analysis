package vn.com.epi.vnlp.tokenizer.dictionary;

import java.io.IOException;

import org.junit.BeforeClass;

import vn.com.epi.vnlp.tokenizer.dictionary.Dictionary;
import vn.com.epi.vnlp.tokenizer.dictionary.HashDictionary;

public class HashDictionaryTest extends DictionaryTest {

	private static HashDictionary dictionary;
	
	@BeforeClass
	public static void setupClass() throws IOException {
		dictionary = HashDictionary.fromFile("data/words.txt");
	}
	
	@Override
	protected Dictionary dictionary() {
		return dictionary;
	}

}
