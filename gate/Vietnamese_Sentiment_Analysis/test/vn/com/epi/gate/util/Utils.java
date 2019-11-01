package vn.com.epi.gate.util;

/**
 * Posted from May 30, 2018 10:03 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
import gate.Annotation;
import gate.Document;
import gate.util.InvalidOffsetException;

public final class Utils {

	private Utils() {
	}

	public static Annotation findById(Document document, Integer id) {
		Annotation annotation = document.getAnnotations().get(id);
		if (annotation != null) {
			return annotation;
		}
		if (document.getAnnotationSetNames() != null) {
			for (String asName : document.getAnnotationSetNames()) {
				annotation = document.getAnnotations(asName).get(id);
				if (annotation != null) {
					return annotation;
				}
			}
		}
		return null;
	}

	public static String getContent(Annotation annotation, Document document)
			throws InvalidOffsetException {
		return document.getContent().getContent(
				annotation.getStartNode().getOffset(),
				annotation.getEndNode().getOffset()).toString();
	}
	
}
