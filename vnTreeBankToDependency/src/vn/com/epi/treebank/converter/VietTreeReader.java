package vn.com.epi.treebank.converter;

import java.io.Reader;

import edu.stanford.parser.nlp.trees.LabeledScoredTreeFactory;
import edu.stanford.parser.nlp.trees.PennTreeReader;
import edu.stanford.parser.nlp.trees.TreeFactory;

public class VietTreeReader extends PennTreeReader {

	public VietTreeReader(Reader r) {
		this(r, new LabeledScoredTreeFactory());
	}

	public VietTreeReader(Reader r, TreeFactory tf) {
		super(r, tf, null, new VietTreebankTokenizer(r));
	}

}
