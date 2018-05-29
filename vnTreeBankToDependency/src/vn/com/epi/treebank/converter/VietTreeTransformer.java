/*******************************************************************************
 * Copyright (c) 2012 ePi Technologies.
 * 
 * This file is part of VNLP: a Natural Language Processing framework 
 * for Vietnamese.
 * 
 * VNLP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * VNLP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with VNLP.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package vn.com.epi.treebank.converter;

import edu.stanford.parser.nlp.trees.DependencyTreeTransformer;
import edu.stanford.parser.nlp.trees.Tree;
import edu.stanford.parser.nlp.trees.TreebankLanguagePack;

/**
 * @author Nguyen Thi Dam (nguyendam2211@gmail.com)
 * 
 */
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
