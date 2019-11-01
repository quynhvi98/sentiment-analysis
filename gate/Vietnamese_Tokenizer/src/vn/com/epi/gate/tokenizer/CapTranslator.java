package vn.com.epi.gate.tokenizer;

import java.util.HashMap;
import java.util.Map;

import vn.hus.nlp.tokenizer.VLexer;

public class CapTranslator {
	
	private static final Map<Integer, String> capMap;

	static {
		capMap = new HashMap<Integer, String>();
		capMap.put(VLexer.WORD_LOWER, "lowercase");
		capMap.put(VLexer.WORD_ALL_CAPS, "allCaps");
		capMap.put(VLexer.WORD_UPPER, "upperInitial");
		capMap.put(VLexer.WORD_OTHER, "mixedCaps");
	}

	public static String translate(int caption) {
		return capMap.get(caption);
	}
}
