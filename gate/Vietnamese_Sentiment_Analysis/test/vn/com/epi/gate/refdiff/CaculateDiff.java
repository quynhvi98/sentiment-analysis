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
 * along with VNLP.  If not, see <http://www.gnu.org/licenses/&gt;. 
 ******************************************************************************/
package vn.com.epi.gate.refdiff;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.FeatureMap;
import gate.util.SimpleFeatureMapImpl;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.stringtemplate.v4.compiler.CodeGenerator.primary_return;

public class CaculateDiff {

	// private String referenceSet;
	private Map<String, Set<String>> referenceSet;
	private Document keyDoc;
	private Document resDoc;

	public CaculateDiff(Document key, Document res, String refString) {
		this.keyDoc = key;
		this.resDoc = res;
		this.referenceSet = new HashMap<String, Set<String>>();
		initReferenceTypes(refString);
	}

	/**
	 * @param refString
	 */
	private void initReferenceTypes(String refString) {
		String[] preMap = refString.split(";");
		for (int i = 0; i < preMap.length; i++) {
			String[] annotRef = preMap[i].split(":");
			String annotName = annotRef[0];
			Set<String> feaAnnot = new HashSet<String>(
					Arrays.asList(annotRef[1].split(",")));
			this.referenceSet.put(annotName, feaAnnot);
		}
	}

	public boolean isCompatible(Annotation thisAnnot, Annotation anAnnot) {

		String annotName = thisAnnot.getType();
		Set<String> feaAnnot = new HashSet<String>();
		if (this.referenceSet.containsKey(annotName)) {
			feaAnnot = this.referenceSet.get(annotName);
		} else {
			feaAnnot = null;
		}

		if (anAnnot == null)
			return false;
		if (coextensive(thisAnnot, anAnnot)) {
			if (anAnnot.getFeatures() == null)
				return true;
			// if (anAnnot.getFeatures().subsumes(thisAnnot.getFeatures()))
			if (subsumes(anAnnot.getFeatures(), thisAnnot.getFeatures(),
					feaAnnot))
				return true;
		}// End if
		return false;
	}// isCompatible

	public boolean isCompatible(Annotation thisAnnot, Annotation anAnnot,
			Set aFeatureNamesSet) {
		// If the set is null then isCompatible(Annotation) will decide.
		String annotName = thisAnnot.getType();
		Set<String> feaAnnot = new HashSet<String>();
		if (this.referenceSet.containsKey(annotName)) {
			feaAnnot = this.referenceSet.get(annotName);
		} else {
			feaAnnot = null;
		}

		if (aFeatureNamesSet == null)
			return isCompatible(thisAnnot, anAnnot);
		if (anAnnot == null)
			return false;
		if (coextensive(thisAnnot, anAnnot)) {
			if (anAnnot.getFeatures() == null)
				return true;
			// if
			// (anAnnot.getFeatures().subsumes(thisAnnot.getFeatures(),aFeatureNamesSet))
			if (subsumes(anAnnot.getFeatures(), thisAnnot.getFeatures(),
					aFeatureNamesSet, feaAnnot))
				return true;
		}// End if
		return false;
	}// isCompatible()

	public boolean isPartiallyCompatible(Annotation thisAnnot,
			Annotation anAnnot) {
		String annotName = thisAnnot.getType();
		Set<String> feaAnnot = new HashSet<String>();
		if (this.referenceSet.containsKey(annotName)) {
			feaAnnot = this.referenceSet.get(annotName);
		} else {
			feaAnnot = null;
		}

		if (anAnnot == null)
			return false;
		if (overlaps(thisAnnot, anAnnot)) {
			if (anAnnot.getFeatures() == null)
				return true;
			if (subsumes(anAnnot.getFeatures(), thisAnnot.getFeatures(),
					feaAnnot))
				return true;
		}// End if
		return false;
	}// isPartiallyCompatible

	public boolean isPartiallyCompatible(Annotation thisAnnot,
			Annotation anAnnot, Set aFeatureNamesSet) {

		String annotName = thisAnnot.getType();
		Set<String> feaAnnot = new HashSet();
		if (this.referenceSet.containsKey(annotName)) {
			feaAnnot = this.referenceSet.get(annotName);
		} else {
			feaAnnot = null;
		}

		if (aFeatureNamesSet == null)
			return isPartiallyCompatible(thisAnnot, anAnnot, feaAnnot);
		if (anAnnot == null)
			return false;
		if (overlaps(thisAnnot, anAnnot)) {
			if (anAnnot.getFeatures() == null)
				return true;
			if (subsumes(anAnnot.getFeatures(), thisAnnot.getFeatures(),
					aFeatureNamesSet, feaAnnot))
				return true;
		}// End if
		return false;
	}// isPartiallyCompatible()

	public boolean coextensive(Annotation thisAnnot, Annotation anAnnot) {
		// If their start offset is not the same then return false
		if ((anAnnot.getStartNode() == null)
				^ (thisAnnot.getStartNode() == null))
			return false;

		if (anAnnot.getStartNode() != null) {
			if ((anAnnot.getStartNode().getOffset() == null)
					^ (thisAnnot.getStartNode().getOffset() == null))
				return false;
			if (anAnnot.getStartNode().getOffset() != null
					&& (!anAnnot.getStartNode().getOffset()
							.equals(thisAnnot.getStartNode().getOffset())))
				return false;
		}// End if

		// If their end offset is not the same then return false
		if ((anAnnot.getEndNode() == null) ^ (thisAnnot.getEndNode() == null))
			return false;

		if (anAnnot.getEndNode() != null) {
			if ((anAnnot.getEndNode().getOffset() == null)
					^ (thisAnnot.getEndNode().getOffset() == null))
				return false;
			if (anAnnot.getEndNode().getOffset() != null
					&& (!anAnnot.getEndNode().getOffset()
							.equals(thisAnnot.getEndNode().getOffset())))
				return false;
		}// End if

		// If we are here, then the annotations hit the same position.
		return true;
	}// coextensive

	public boolean overlaps(Annotation thisAnnot, Annotation aAnnot) {
		if (aAnnot == null)
			return false;
		if (aAnnot.getStartNode() == null || aAnnot.getEndNode() == null
				|| aAnnot.getStartNode().getOffset() == null
				|| aAnnot.getEndNode().getOffset() == null)
			return false;

		// if ( (aAnnot.getEndNode().getOffset().longValue() ==
		// aAnnot.getStartNode().getOffset().longValue()) &&
		// this.getStartNode().getOffset().longValue() <=
		// aAnnot.getStartNode().getOffset().longValue() &&
		// aAnnot.getEndNode().getOffset().longValue() <=
		// this.getEndNode().getOffset().longValue()
		// ) return true;

		if (aAnnot.getEndNode().getOffset().longValue() <= thisAnnot
				.getStartNode().getOffset().longValue())
			return false;

		if (aAnnot.getStartNode().getOffset().longValue() >= thisAnnot
				.getEndNode().getOffset().longValue())
			return false;

		return true;
	}// overlaps

	public boolean subsumes(FeatureMap anFeatureMap, FeatureMap aFeatureMap,
			Set<String> feaAnnot) {
		// null is included in everything
		if (aFeatureMap == null)
			return true;

		if (anFeatureMap.size() < aFeatureMap.size())
			return false;

		SimpleFeatureMapImpl sfm = (SimpleFeatureMapImpl) aFeatureMap;
		SimpleFeatureMapImpl anSfm = (SimpleFeatureMapImpl) anFeatureMap;
		Object[] keySet = sfm.keySet().toArray();
		Object key;
		Object keyValueFromAFeatureMap;
		Object keyValueFromThis;

		for (int i = 0; i < sfm.size(); i++) {
			key = keySet[i];
			keyValueFromAFeatureMap = sfm.get(key);
			int v = getSubsumeKey(anFeatureMap, key);
			if (v < 0)
				return false;
			keyValueFromThis = anSfm.get(i);// was: get(key);

			if ((keyValueFromThis == null && keyValueFromAFeatureMap != null)
					|| (keyValueFromThis != null && keyValueFromAFeatureMap == null))
				return false;

			/*
			 * ontology aware subsume implementation ontotext.bp
			 */
			if ((keyValueFromThis != null) && (keyValueFromAFeatureMap != null)) {
				// commented out as ontology subsumes is now explicitly called
				// if
				// an ontology is provided. <valyt>
				// if ( key.equals(LOOKUP_CLASS_FEATURE_NAME) ) {
				// /* ontology aware processing */
				// Object sfmOntoObj = sfm.get(LOOKUP_ONTOLOGY_FEATURE_NAME);
				// Object thisOntoObj = this.get(LOOKUP_ONTOLOGY_FEATURE_NAME);
				// if (null!=sfmOntoObj && null!= thisOntoObj) {
				// if (sfmOntoObj.equals(thisOntoObj)) {
				// boolean doSubsume = ontologySubsume(
				// sfmOntoObj.toString(),
				// keyValueFromAFeatureMap.toString(),
				// keyValueFromThis.toString());
				// if (!doSubsume ) {
				// return false;
				// }
				// } // if ontologies are with the same url
				// } //if not null objects
				// else {
				// // incomplete feature set: missing ontology feature
				// return false;
				// }
				// } else {
				/* processing without ontology awareness */
				if (feaAnnot != null) {
					if (feaAnnot.contains(key)) {
						return compareReference((Integer) keyValueFromThis,
								(Integer) keyValueFromAFeatureMap);
					}
				}

				if (!keyValueFromThis.equals(keyValueFromAFeatureMap))
					return false;
				// } // else
			} // if
		} // for
		return true;
	}// subsumes()

	public boolean subsumes(FeatureMap thisFeatureMap, FeatureMap aFeatureMap,
			Set aFeatureNamesSet, Set<String> feaAnnot) {
		// This means that all features are taken into consideration.
		if (aFeatureNamesSet == null) {
			return this.subsumes(thisFeatureMap, aFeatureMap, feaAnnot);
		}
		// null is included in everything
		if (aFeatureMap == null)
			return true;
		// This means that subsumes is supressed.
		if (aFeatureNamesSet.isEmpty())
			return true;

		SimpleFeatureMapImpl sfm = (SimpleFeatureMapImpl) aFeatureMap;
		Object[] keySet = sfm.keySet().toArray();
		Object key;
		Object keyValueFromAFeatureMap;
		Object keyValueFromThis;

		for (int i = 0; i < sfm.size(); i++) {
			key = keySet[i];
			if (!aFeatureNamesSet.contains(key))
				continue;

			keyValueFromAFeatureMap = sfm.get(key);
			keyValueFromThis = get(thisFeatureMap, key);
			if ((keyValueFromThis == null && keyValueFromAFeatureMap != null)
					|| (keyValueFromThis != null && keyValueFromAFeatureMap == null))
				return false;

			if ((keyValueFromThis != null) && (keyValueFromAFeatureMap != null)) {
				// Commented out as ontology subsumes is now explicitly called
				// when an ontology
				// is provided. <valyt>
				// if ( key.equals(LOOKUP_CLASS_FEATURE_NAME) ) {
				// /* ontology aware processing */
				// if (!aFeatureNamesSet.contains(LOOKUP_ONTOLOGY_FEATURE_NAME))
				// continue;
				//
				// Object sfmOntoObj = sfm.get(LOOKUP_ONTOLOGY_FEATURE_NAME);
				// Object thisOntoObj = this.get(LOOKUP_ONTOLOGY_FEATURE_NAME);
				// if (null!=sfmOntoObj && null!= thisOntoObj) {
				// if (sfmOntoObj.equals(thisOntoObj)) {
				// if (! ontologySubsume(
				// sfmOntoObj.toString(),
				// keyValueFromAFeatureMap.toString(),
				// keyValueFromThis.toString()))
				// return false;
				// } // if ontologies are with the same url
				// } //if not null objects
				// else {
				// // incomplete feature set: missing ontology feature
				// return false;
				// }
				// } else {
				/* processing without ontology awareness */
				if (feaAnnot != null) {
					if (feaAnnot.contains(key)) {
						return compareReference((Integer) keyValueFromThis,
								(Integer) keyValueFromAFeatureMap);
					}
				}

				if (!keyValueFromThis.equals(keyValueFromAFeatureMap))
					return false;
				// } //else
			} // if values not null
		} // for

		return true;
	}// subsumes()

	public Object get(FeatureMap aFeature, Object key) {
		int pos = -1;
		Object[] keySet = aFeature.keySet().toArray();
		for (int i = 0; i < keySet.length; i++) {
			if (key == keySet[i]) {
				pos = i;
				break;
			}
		}
		return (pos == -1) ? null : aFeature.get(key);
	} // get

	public int getSubsumeKey(FeatureMap anFeature, Object key) {
		Object[] keySet = anFeature.keySet().toArray();
		for (int i = 0; i < anFeature.size(); i++) {
			if (key == keySet[i])
				return i;
		} // for
		return -1;
	} // getPostionByKey

	private boolean compareReference(int resId, int keyId) {
		Annotation keyAnnot = getAnnotation(keyId, keyDoc);
		Annotation resAnnot = getAnnotation(resId, resDoc);
		if (keyAnnot == null || resAnnot == null) {
			return false;
		} else {
			return coextensive(keyAnnot, resAnnot);
		}
	}

	private Annotation getAnnotation(int id, Document doc) {
		Set<String> annotSetName = doc.getAnnotationSetNames();
		AnnotationSet defaultAnnot = doc.getAnnotations("");
		Annotation outAnnot = defaultAnnot.get(id);
		if (outAnnot != null) {
			return outAnnot;
		} else if (annotSetName != null) {
			for (String ASName : annotSetName) {
				outAnnot = doc.getAnnotations(ASName).get(id);
				if (outAnnot != null) {
					return outAnnot;
				}
			}
		}
		return null;
	}
}
