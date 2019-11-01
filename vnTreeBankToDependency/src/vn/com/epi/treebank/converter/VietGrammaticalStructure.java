package vn.com.epi.treebank.converter;

import edu.stanford.parser.nlp.trees.GrammaticalRelation;
import edu.stanford.parser.nlp.trees.GrammaticalStructure;
import edu.stanford.parser.nlp.trees.HeadFinder;
import edu.stanford.parser.nlp.trees.PennTreebankLanguagePack;
import edu.stanford.parser.nlp.trees.Tree;
import edu.stanford.parser.nlp.util.Filter;

public class VietGrammaticalStructure extends GrammaticalStructure {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7431579329501470389L;

	/**
	 * Construct a new <code>GrammaticalStructure</code> from an existing parse
	 * tree. The new <code>GrammaticalStructure</code> has the same tree
	 * structure and label values as the given tree (but no shared storage). As
	 * part of construction, the parse tree is analyzed using definitions from
	 * {@link GrammaticalRelation <code>GrammaticalRelation</code>} to populate
	 * the new <code>GrammaticalStructure</code> with as many labeled
	 * grammatical relations as it can.
	 * 
	 * @param t
	 *            Parse tree to make grammatical structure from
	 */
	public VietGrammaticalStructure(Tree t) {
		this(t, new PennTreebankLanguagePack().punctuationWordRejectFilter());
	}

	/**
	 * This gets used by GrammaticalStructureFactory (by reflection). DON'T
	 * DELETE.
	 * 
	 * @param t
	 *            Parse tree to make grammatical structure from
	 * @param puncFilter
	 *            Filter to remove punctuation dependencies
	 */
	public VietGrammaticalStructure(Tree t, Filter<String> puncFilter) {
		this(t, puncFilter, new VietHeadFinderCheck(true), true);
	}

	/**
	 * This gets used by GrammaticalStructureFactory (by reflection). DON'T
	 * DELETE.
	 * 
	 * @param t
	 *            Parse tree to make grammatical structure from
	 * @param puncFilter
	 *            Filter to remove punctuation dependencies
	 * @param hf
	 *            HeadFinder to use when building it
	 */
	public VietGrammaticalStructure(Tree t, Filter<String> puncFilter,
			HeadFinder hf) {
		this(t, puncFilter, hf, true);
	}

	/**
	 * Construct a new <code>GrammaticalStructure</code> from an existing parse
	 * tree. The new <code>GrammaticalStructure</code> has the same tree
	 * structure and label values as the given tree (but no shared storage). As
	 * part of construction, the parse tree is analyzed using definitions from
	 * {@link GrammaticalRelation <code>GrammaticalRelation</code>} to populate
	 * the new <code>GrammaticalStructure</code> with as many labeled
	 * grammatical relations as it can.
	 * 
	 * @param t
	 *            Parse tree to make grammatical structure from
	 * @param puncFilter
	 *            Filter for punctuation words
	 * @param hf
	 *            HeadFinder to use when building it
	 * @param threadSafe
	 *            Whether or not to support simultaneous instances among
	 *            multiple threads
	 */
	public VietGrammaticalStructure(Tree t, Filter<String> puncFilter,
			HeadFinder hf, boolean threadSafe) {
		// the tree is normalized (for index and functional tag stripping)
		// inside CoordinationTransformer
		super((new VietTreeTransformer()).transformTree(t),
				VietGrammaticalRelations.values(threadSafe),
				threadSafe ? VietGrammaticalRelations.valuesLock() : null, hf,
				puncFilter);

		// super((new CoordinationTransformer()).transformTree(t),
		// VietGrammaticalRelations.values(threadSafe), threadSafe ?
		// VietGrammaticalRelations.valuesLock() : null, hf, puncFilter);
	}

}
