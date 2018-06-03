package vn.com.epi.treebank.converter;

import edu.stanford.parser.nlp.trees.DependencyTreeTransformer;
import edu.stanford.parser.nlp.trees.Tree;
import edu.stanford.parser.nlp.trees.TreebankLanguagePack;

public class VietTreeTransformer extends DependencyTreeTransformer {
	protected final TreebankLanguagePack tlp;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.stanford.nlp.trees.TreeTransformer#transformTree(edu.stanford.nlp
	 * .trees.Tree)
	 */

	public VietTreeTransformer() {
		tlp = new VietTreebankLanguagePack();
	}

	@Override
	public Tree transformTree(Tree t) {
		// deal with empty root
		t.setValue(cleanUpRoot(t.value()));
		// strips tags
		stripTag(t);
		return super.stripEmptyNode(t);
	}

	protected String cleanUpLabel(String label) {
		if (label == null) {
			return ""; // This shouldn't really happen, but can happen if there
						// are unlabeled nodes further down a tree, as
						// apparently happens in at least the 20100730 era
						// American National Corpus
		}
		label = tlp.basicCategory(label);
		return label;
	}

}
