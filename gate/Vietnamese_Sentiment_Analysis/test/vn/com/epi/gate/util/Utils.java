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
