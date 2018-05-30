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
package vn.com.epi.gate.util;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Utils;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.util.InvalidOffsetException;

import java.lang.reflect.InvocationTargetException;

/**
 * Sink annotations to remove leading and trailing spaces it contains.
 * Posted from May 30, 2018 10:43 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public class AnnotationTrimmerPlugin extends AbstractLanguageAnalyser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 0L;

	@Override
	public void execute() throws ExecutionException {
		try {
			process(document.getAnnotations());
			if (document.getAnnotationSetNames() != null) {
				for (String asName : document.getAnnotationSetNames()) {
					process(document.getAnnotations(asName));
				}
			}
		} catch (Exception e) {
			throw new ExecutionException(e);
		}
	}

	private void process(AnnotationSet annotations) throws InvalidOffsetException, 
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		Annotation[] arr = annotations.toArray(new Annotation[annotations.size()]);
		for (Annotation annotation : arr) {
			String str = Utils.stringFor(document, annotation);
			int startChange = 0;
			while (startChange < str.length() &&
					Character.isWhitespace(str.charAt(startChange))) {
				startChange++;
			}
			int endChange = 0;
			while (endChange < str.length()
					&& Character.isWhitespace(
							str.charAt(str.length() - 1 - endChange))) {
				endChange++;
			}
			if (startChange + endChange < str.length()) {
				moveAnnotation(annotations, annotation, 
						Utils.start(annotation) + startChange, 
						Utils.end(annotation) - endChange);
			} else {
				 annotations.remove(annotation);
			}
		}
	}
	
	  protected void moveAnnotation(AnnotationSet set, Annotation oldAnnotation, 
	          Long newStartOffset, Long newEndOffset) throws InvalidOffsetException{
	    //Moving is done by deleting the old annotation and creating a new one.
	    //If this was the last one of one type it would mess up the gui which 
	    //"forgets" about this type and then it recreates it (with a different 
	    //colour and not visible.
	    //In order to avoid this problem, we'll create a new temporary annotation.
	    Annotation tempAnn = null;
	    if(set.get(oldAnnotation.getType()).size() == 1){
	      //create a clone of the annotation that will be deleted, to act as a 
	      //placeholder 
	      Integer tempAnnId = set.add(oldAnnotation.getStartNode(), 
	              oldAnnotation.getStartNode(), oldAnnotation.getType(), 
	              oldAnnotation.getFeatures());
	      tempAnn = set.get(tempAnnId);
	    }

	    Integer oldID = oldAnnotation.getId();
	    set.remove(oldAnnotation);
	    set.add(oldID, newStartOffset, newEndOffset,
	            oldAnnotation.getType(), oldAnnotation.getFeatures());
	    //remove the temporary annotation
	    if(tempAnn != null) set.remove(tempAnn);
	  }   
	
}
