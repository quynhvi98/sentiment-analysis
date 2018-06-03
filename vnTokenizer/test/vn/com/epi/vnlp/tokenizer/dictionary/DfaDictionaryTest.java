package vn.com.epi.vnlp.tokenizer.dictionary;

import java.io.IOException;

import org.junit.BeforeClass;

import vn.com.epi.vnlp.dfa.io.DfaSaxReader;

public class DfaDictionaryTest extends DictionaryTest {

	private static final String AUTOMATA_PATH = "data/dfa.xml";
	private static DfaDictionary dictionary;
	
	@BeforeClass
	public static void setupClass() throws IOException {
		dictionary = new DfaDictionary(DfaSaxReader.readFile(AUTOMATA_PATH));
	}
	
	@Override
	protected Dictionary dictionary() {
		return dictionary;
	}
	
}
