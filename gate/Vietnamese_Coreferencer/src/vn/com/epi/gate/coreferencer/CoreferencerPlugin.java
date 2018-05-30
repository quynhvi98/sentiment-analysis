package vn.com.epi.gate.coreferencer;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleResource;

import java.util.ArrayList;

/**
 * @author Nguyen Vi Duong (vi.duong.bk@gmail.com)
 * 
 */
@CreoleResource(name = "Coreferencer PR", comment = "processing resource")
public class CoreferencerPlugin extends AbstractLanguageAnalyser {

	private static final long serialVersionUID = 1L;
	private static final String SENTENCE_LABLE = "Sentence";
	private static final String PRODUCT_LABLE = "Product";
	private static final String LOOKUP_LABLE = "Lookup";

	public Resource init() throws ResourceInstantiationException {
		return this;
	}

	public void execute() throws ExecutionException {

		AnnotationSet sentences = document.getAnnotations("").get(
				SENTENCE_LABLE);
		AnnotationSet products = document.getAnnotations("").get(PRODUCT_LABLE);
		AnnotationSet lookups = document.getAnnotations("").get(LOOKUP_LABLE);

		ArrayList<Annotation> unNamedProduct = getCorrectionLookup(products,
				lookups);
		ArrayList<Annotation> namedProduct = getUnCorrectionLookup(products,
				lookups);

		for (Annotation unNamed : unNamedProduct) {
			boolean isMatch = false;
			for (Annotation sentence : sentences) {
				ArrayList<Annotation> sentenProduct = new ArrayList();
				for (Annotation named : namedProduct) {
					if (isSentenceConcurable(sentence, named, unNamed)) {
						sentenProduct.add(named);
					}
				}
				if (sentenProduct.size() > 0) {
					Annotation nearestNamed = getNearestAnnot(unNamed,
							sentenProduct);
					if (nearestNamed != null) {
						nearestNamed.getFeatures().put("co-reference",
								unNamed.getId());
						unNamed.getFeatures().put("co-reference",
								nearestNamed.getId());
						isMatch = true;
					}
				}
			}

			if (!isMatch) {
				Annotation nearestNamed = getNearestAnnot(unNamed, namedProduct);
				if (nearestNamed != null) {
					nearestNamed.getFeatures().put("co-reference",
							unNamed.getId());
					unNamed.getFeatures().put("co-reference",
							nearestNamed.getId());
				}
			}
		}

	}

	private boolean isSentenceConcurable(Annotation sentence,
			Annotation product, Annotation lookup) {
		if (sentence.overlaps(product) && sentence.overlaps(lookup)) {
			return true;
		} else {
			return false;
		}
	}

	private Annotation getNearestAnnot(Annotation unnamed,
			ArrayList<Annotation> products) {
		Annotation output = null;
		for (Annotation product : products) {
			if (getLocation(unnamed) - getLocation(product) != 0
					&& Math.abs(getLocation(unnamed) - getLocation(product)) < Math
							.abs(getLocation(unnamed) - getLocation(output))) {
				output = product;
			}
		}
		return output;
	}

	private long getLocation(Annotation anot) {
		if (anot != null) {
			return (anot.getEndNode().getOffset() + anot.getStartNode()
					.getOffset()) / 2;
		} else {
			return 9999999;
		}
	}

	private ArrayList<Annotation> getCorrectionLookup(AnnotationSet products,
			AnnotationSet lookups) {
		ArrayList<Annotation> productCorrected = new ArrayList();
		for (Annotation lookup : lookups) {
			for (Annotation product : products) {
				if (lookup.getStartNode().getOffset() == product.getStartNode()
						.getOffset()
						&& lookup.getEndNode().getOffset() == product
								.getEndNode().getOffset()
						&& lookup.getFeatures().get("type").equals("unnamed")) {
					productCorrected.add(product);
				}
			}
		}
		return productCorrected;
	}

	private ArrayList<Annotation> getUnCorrectionLookup(AnnotationSet products,
			AnnotationSet lookups) {
		ArrayList<Annotation> productUnCorrected = new ArrayList();
		for (Annotation lookup : lookups) {
			for (Annotation product : products) {
				if (lookup.getStartNode().getOffset() != product.getStartNode()
						.getOffset()
						|| lookup.getEndNode().getOffset() != product
								.getEndNode().getOffset()) {
					productUnCorrected.add(product);
				}
			}
		}
		return productUnCorrected;
	}

}
