package vn.com.epi.tagger;

import gate.Annotation;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.OffsetComparator;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import jvntagger.MaxentTagger;

public class TaggerPlugin extends AbstractLanguageAnalyser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The maxent tagger.
	 */
	private MaxentTagger tagger;

	private URL modelUrl;

	private final Comparator<Annotation> comparator = new OffsetComparator();

	@Override
	public Resource init() throws ResourceInstantiationException {
		try {
			tagger = new MaxentTagger(modelUrl.getPath().substring(1)
					.replace("%20", " "));
			return this;
		} catch (Exception e) {
			throw new ResourceInstantiationException(e);
		}
	}

	@Override
	public void execute() throws ExecutionException {
		for (Annotation sent : document.getAnnotations().get("Sentence")) {
			List<Annotation> tokens = getTokens(sent);
			String sentence = toSentence(tokens);
			String taggedSentence = tagger.tagging(sentence);
			applyCategoryFeature(tokens, getTagger(taggedSentence));
		}
	}

	private List<String> getTagger(String input) {
		// split the tagged string using the word/tag delimiter
		List<String> tag = new ArrayList<String>();
		String[] wordAndTags = input.split(" ");
		int limit = wordAndTags.length;
		for (int i = 0; i < limit; i++) {
			if(wordAndTags[i].equals("///")){
				// tag "/"
				tag.add("/");
			}else{
				String[] splitWordAndTag = wordAndTags[i].split("/");
				// the case of date with / separator, for example 20/10/1980/N
				// the word is 20/10/1980, tag is lastest /
				tag.add(splitWordAndTag[splitWordAndTag.length - 1]);
			}

		}
		return tag;
	}

	private void applyCategoryFeature(List<Annotation> tokens,
			List<String> taggedSentence) {
		for (int i = 0; i < tokens.size(); i++) {
			tokens.get(i).getFeatures().put("category", taggedSentence.get(i));
		}
	}

	private String toSentence(List<Annotation> tokens) {
		StringBuilder sentence = new StringBuilder();
		for (Annotation token : tokens) {
			String string = ((String) token.getFeatures().get("string"))
					.replace(" ", "_");
			sentence.append(string);
			sentence.append(" ");
		}
		return sentence.toString().trim();
	}

	/**
	 * TODO: measure running time
	 * 
	 * @param sent
	 * @return
	 */
	private List<Annotation> getTokens(Annotation sent) {
		List<Annotation> tokens = new ArrayList<Annotation>(document
				.getAnnotations().get("Token", sent.getStartNode().getOffset(),
						sent.getEndNode().getOffset()));
		Collections.sort(tokens, comparator);
		return tokens;
	}

	public URL getModelUrl() {
		return modelUrl;
	}

	public void setModelUrl(URL modelUrl) {
		this.modelUrl = modelUrl;
	}

}
