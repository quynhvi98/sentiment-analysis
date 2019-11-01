package vn.com.epi.tagger.benchmark;

import gate.creole.ResourceInstantiationException;
import jvntagger.MaxentTagger;

public class JVnTagger {

	private MaxentTagger tagger;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	public void init(String url) throws ResourceInstantiationException {
		try {
			tagger = new MaxentTagger(url);
		} catch (Exception e) {
			throw new ResourceInstantiationException(e);
		}
	}

	public String tag(String sentence) {
		return tagger.tagging(sentence);
	}
	
	
}
