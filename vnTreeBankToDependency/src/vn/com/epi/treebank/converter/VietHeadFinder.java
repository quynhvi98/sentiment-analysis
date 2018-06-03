package vn.com.epi.treebank.converter;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import edu.stanford.parser.nlp.trees.AbstractCollinsHeadFinder;
import edu.stanford.parser.nlp.trees.Tree;

public class VietHeadFinder extends AbstractCollinsHeadFinder {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @throws FileNotFoundException
	 * @throws UnsupportedEncodingException
	 * 
	 */
	public VietHeadFinder(boolean in) {
		// TODO Auto-generated constructor stub
		super(new VietTreebankLanguagePack());
		nonTerminalInfo = new HashMap<String, String[][]>();
		// rulechanges();

	}

	/**
	 * 
	 */
	private void rulechanges() {
		nonTerminalInfo.put("S", new String[][] { { "left", "VP", "NP" },
				{ "right", "VP", "NP", "S" } });
		nonTerminalInfo.put("NP", new String[][] { { "left", "NP" } });
		nonTerminalInfo.put("VP", new String[][] { { "left", "VP" } });

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.stanford.nlp.trees.AbstractCollinsHeadFinder#findMarkedHead(edu.stanford
	 * .nlp.trees.Tree)
	 */
	@Override
	protected Tree findMarkedHead(Tree t) {
		// TODO Auto-generated method stub
		Tree head = null;
		Tree[] children = t.children();
		int limit = children.length;
		for (int i = 0; i < limit; i++) {
			String lable = children[i].label().value();
			if (lable.contains("-H")) {
				children[i].label().setValue(
						lable.substring(0, lable.length() - 2));
				head = children[i];
			}
		}
		return head;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.stanford.nlp.trees.AbstractCollinsHeadFinder#determineHead(edu.stanford
	 * .nlp.trees.Tree, edu.stanford.nlp.trees.Tree)
	 */
	@Override
	public Tree determineHead(Tree t, Tree parent) {
		Tree head = super.determineHead(t, parent);
		return head;
	}

}
