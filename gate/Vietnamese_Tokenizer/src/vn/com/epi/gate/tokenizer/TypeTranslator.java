package vn.com.epi.gate.tokenizer;

import java.util.HashMap;
import java.util.Map;

import vn.hus.nlp.tokenizer.Token;

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
