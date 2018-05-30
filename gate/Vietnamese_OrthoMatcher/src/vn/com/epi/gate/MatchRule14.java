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

/**
 * RULE #13: do multi-word names match except for
 * one token e.g.
 * "Second Force Recon Company" == "Force Recon Company"
 * Note that this rule has NOT been used in LaSIE's 1.5
 * namematcher
 * Restrictions: - remove cdg first
 *               - shortest name should be 2 words or more
 *               - if N is the number of tokens of the longest
 *                 name, then N-1 tokens should be matched
 * Condition(s): case-sensitive match
 * Applied to: organisation or person annotations only
 */
public class MatchRule14 implements OrthoMatcherRule {

  VietnameseOrthoMatcher orthomatcher;
	
	public MatchRule14(VietnameseOrthoMatcher orthmatcher){
			this.orthomatcher=orthmatcher;
	}
	
	public boolean value(String s1, String s2) {
	    
	    boolean result = false; 
	  
	    int matched_tokens = 0, mismatches = 0;

	    // if names < 2 words then rule is invalid
	    if (orthomatcher.tokensLongAnnot.size() < 3 || orthomatcher.tokensShortAnnot.size() < 2) 
	      result =  false;
	    else {
    	    // now do the matching
    	    for (int i=0,j= 0; i < orthomatcher.tokensShortAnnot.size() && mismatches < 2; i++) {
    
    //	    Out.prln("i = " + i);
    //	    Out.prln("j = " + j);
    	      if ( ((Annotation) orthomatcher.tokensLongAnnot.get(j)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME).equals(
    	              ((Annotation) orthomatcher.tokensShortAnnot.get(i)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME)) ) {
    	        matched_tokens++;
    	        j++;
    	      } else
    	        mismatches++;
    	    } // for
    
    	    if (matched_tokens >= orthomatcher.tokensLongAnnot.size()-1)
    	      result = true;
	    }

	    if (result) OrthoMatcherHelper.usedRule(14);
	    return result;
	}
	
  public String getId(){
    return "MatchRule14";
  }
}
