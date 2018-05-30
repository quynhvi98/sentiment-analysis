package vn.com.epi.gate.sentiment;

import gate.Annotation;
import gate.AnnotationSet;
import gate.CorpusController;
import gate.Document;
import gate.Gate;
import gate.ProcessingResource;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.io.IOException;

import vn.com.epi.gate.AnnotationOffsetComparator;

/**
 * Posted from May 25, 2018 1:43 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public class ContextMatcher extends AbstractLanguageAnalyser implements
		ProcessingResource {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8651268467873933965L;

	public static final String PRODUCT_ANNOTATION_TYPE = "Product";

	private static AnnotationOffsetComparator ANNOTATION_OFFSET_COMPARATOR;
	private AnnotationSet products;

	static {
		ANNOTATION_OFFSET_COMPARATOR = new AnnotationOffsetComparator();
	}

	public Resource init() throws ResourceInstantiationException {
		return this;
	}

	public void reInit() throws ResourceInstantiationException {
		init();
	}

	public void execute() throws ExecutionException {
		preprocess();
		if (products == null || products.isEmpty()) {
			return;
		}

		for (Annotation product : products) {
			AnnotationSet annotationBetween = document.getAnnotations()
					.getContained(product.getStartNode().getOffset(),
							product.getEndNode().getOffset());
			AnnotationSet lookupBetween = annotationBetween
					.get(LOOKUP_ANNOTATION_TYPE);
			for (Annotation lookup : lookupBetween) {
				String stringID = (String) lookup.getFeatures().get("ID");
				String stringContextID = (String) lookup.getFeatures().get(
						"ContextID");
				String majorType = (String) lookup.getFeatures().get(
						"majorType");
				int contextID = 0;
				if (stringContextID != null) {
					contextID = Integer.parseInt(stringContextID);
				} else if (stringID != null && majorType.equals("context")) {
					contextID = Integer.parseInt(stringID);
				} else {
					continue;
				}
				// try {
				// System.out.println(document.getContent().getContent(lookup.getStartNode().getOffset(),
				// lookup.getEndNode().getOffset()));
				// }
				// catch (InvalidOffsetException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
				product.getFeatures().put("ID", contextID);
				break;
			}
		}
	}

	public void preprocess() {
		AnnotationSet defaultAnnotationSet = document.getAnnotations();
		products = defaultAnnotationSet.get(PRODUCT_ANNOTATION_TYPE);
	}

	public static void main(String args[]) throws GateException, IOException {
		Gate.init();
		CorpusController controller = (CorpusController) PersistenceManager
				.loadObjectFromFile(new File("E://vnSent.gapp"));
		controller.execute();
		ContextMatcher matcher = new ContextMatcher();
		matcher.setCorpus(controller.getCorpus());
		matcher.init();
		for (int i = 0; i < matcher.getCorpus().size(); i++) {
			Document doc = (Document) matcher.getCorpus().get(i);
			matcher.setDocument(doc);
			matcher.execute();
		}
	}

}
