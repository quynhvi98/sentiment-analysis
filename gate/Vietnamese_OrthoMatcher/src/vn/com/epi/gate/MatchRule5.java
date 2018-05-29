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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import static vn.com.epi.gate.VietnameseOrthoMatcher.*;
/**
 * RULE #4Name: Does all the non-punctuation tokens from the long string match the corresponding tokens 
 * in the short string?  
 * This basically identifies cases where the two strings match token for token, excluding punctuation
 * Applied to: person annotations
 *
 * Modified by Andrew Borthwick, Spock Networks:  Allowed for nickname match
 */
public class MatchRule5 implements OrthoMatcherRule {

	VietnameseOrthoMatcher orthomatcher;
	
	public MatchRule5(VietnameseOrthoMatcher orthmatcher){
		this.orthomatcher=orthmatcher;
	}
	
	public boolean value(String s1, String s2) {
	  
		boolean allTokensMatch = true;
//	    if (s1.equals("wilson")) {
//	      log.debug("MR4 Name: Matching" + tokensLongAnnot + " with " + tokensShortAnnot);
//	      log.debug("MR4 Name: Matching " + s1 + " with " + s2);
//	    }  
	    if (orthomatcher.tokensLongAnnot.size() == 0 || orthomatcher.tokensShortAnnot.size() == 0) {
	      log.debug("Rule 5 rejecting " + s1 + " and " + s2 + " because one doesn't have any tokens");
	      return false;
	    }
	    Iterator<Annotation> tokensLongAnnotIter = orthomatcher.tokensLongAnnot.iterator();
	    Iterator<Annotation> tokensShortAnnotIter = orthomatcher.tokensShortAnnot.iterator();
	    while (tokensLongAnnotIter.hasNext() && tokensShortAnnotIter.hasNext()) {
	      Annotation token = (Annotation) tokensLongAnnotIter.next();
	      if (((String)token.getFeatures().get(TOKEN_KIND_FEATURE_NAME)).equals(PUNCTUATION_VALUE))
	        continue;
	      if (! orthomatcher.getOrthography().fuzzyMatch((String)(tokensShortAnnotIter.next().
	              getFeatures().get(TOKEN_STRING_FEATURE_NAME)),
	              (String) token.getFeatures().get(TOKEN_STRING_FEATURE_NAME))) {
	        allTokensMatch = false;
	        break;
	      }
	    }
	    if (allTokensMatch && log.isDebugEnabled()) {
	      log.debug("rule 5 matched " + s1 + "(id: " + orthomatcher.longAnnot.getId() + ", offset: " + orthomatcher.longAnnot.getStartNode().getOffset() + ") to " + 
	                                    s2+  "(id: " + orthomatcher.shortAnnot.getId() + ", offset: " + orthomatcher.shortAnnot.getStartNode().getOffset() + ")");
	    }   
	    
	    if (allTokensMatch) OrthoMatcherHelper.usedRule(5);
	    
	    return allTokensMatch;
	}
	
  public String getId(){
    return "MatchRule5";
  }
}
