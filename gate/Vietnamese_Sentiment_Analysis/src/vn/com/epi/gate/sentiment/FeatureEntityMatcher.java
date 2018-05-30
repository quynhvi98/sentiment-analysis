package vn.com.epi.gate.sentiment;

import gate.Document;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;

import java.util.List;

/**
 * Posted from May 25, 2018 1:45 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public interface FeatureEntityMatcher {

	void init() throws ResourceInstantiationException;

	void execute() throws ExecutionException;

	List<String> getEntityAnnotationTypes();

	void setEntityAnnotationTypes(List<String> entityAnnotationTypes);

	String getInputASName();

	void setInputASName(String inputAS);

	Document getDocument();

	void setDocument(Document document);

}