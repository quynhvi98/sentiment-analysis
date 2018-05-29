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
package vn.com.epi.gate;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Document;
import gate.creole.ExecutionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.google.common.collect.Multimap;

/*
 * This interface is used so that one can create an orthography class that that defines the behavior of the orthomatcher. 

 */
public interface AnnotationOrthography {
  
    /**
     * Returns normalized content of an annotation - removes extra white spaces.
     * @param a
     * @param d
     * @return
     * @throws ExecutionException
     */
    public String getStringForAnnotation(Annotation a, gate.Document d) throws ExecutionException;
    public boolean fuzzyMatch (String s1, String s2);
    public boolean allNonStopTokensInOtherAnnot(ArrayList<Annotation> firstName,ArrayList<Annotation> secondName,String TOKEN_STRING_FEATURE_NAME,boolean caseSensitive);
    
    public String stripPersonTitle (String annotString, Annotation annot, Document doc, Multimap<Integer, Annotation> tokensMap, Multimap<Integer, Annotation> normalizedTokensMap,AnnotationSet nameAllAnnots) throws ExecutionException;
    public boolean matchedAlready(Annotation annot1, Annotation annot2,List matchesDocFeature,AnnotationSet nameAllAnnots);
    public Annotation updateMatches(Annotation newAnnot, String annotString,HashMap processedAnnots,AnnotationSet nameAllAnnots,List matchesDocFeature);
    public void updateMatches(Annotation newAnnot, Annotation prevAnnot,List matchesDocFeature,AnnotationSet nameAllAnnots);
    public HashSet buildTables(AnnotationSet nameAllAnnots);
    public boolean isUnknownGender(String gender);

}
