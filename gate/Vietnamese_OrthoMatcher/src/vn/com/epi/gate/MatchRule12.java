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
 * RULE #12: do the first and last tokens of one name
 * match the first and last tokens of the other?
 * Condition(s): case-sensitive match
 * Applied to: organisation annotations only
 */
public class MatchRule12 implements OrthoMatcherRule {

    VietnameseOrthoMatcher orthomatcher;
	
	public MatchRule12(VietnameseOrthoMatcher orthmatcher){
			this.orthomatcher=orthmatcher;
	}
	
	public boolean value(String s1, String s2) {
      // first do the easy case e.g. "Pan American" == "Pan Am"
	    boolean result=false;
   
	    if (orthomatcher.tokensLongAnnot.size()>1 && orthomatcher.tokensShortAnnot.size()>1) {
        // Out.prln("Rule 12");

	      // get first and last tokens of s1 & s2
	      String s1_first = (String)
	      ((Annotation) orthomatcher.tokensLongAnnot.get(0)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME);
	      String s2_first = (String)
	      ((Annotation) orthomatcher.tokensShortAnnot.get(0)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME);

	      if (!OrthoMatcherHelper.straightCompare(s1_first,s2_first,orthomatcher.caseSensitive))
	        result = false;
	      else {
    	      String s1_last = (String)
    	      ((Annotation) orthomatcher.tokensLongAnnot.get(orthomatcher.tokensLongAnnot.size()-1)).getFeatures().get(VietnameseOrthoMatcher.TOKEN_STRING_FEATURE_NAME);
    	      String s2_last = (String)
    	      ((Annotation) orthomatcher.tokensShortAnnot.get(orthomatcher.tokensShortAnnot.size()-1)).getFeatures().get(VietnameseOrthoMatcher.TOKEN_STRING_FEATURE_NAME);
    
    	      boolean retVal =  OrthoMatcherHelper.straightCompare(s1_last,s2_last,orthomatcher.caseSensitive);
    	      if (retVal && orthomatcher.log.isDebugEnabled()) {
    	        orthomatcher.log.debug("rule 12 matched " + s1 + "(id: " + orthomatcher.longAnnot.getId() + ") to "
    	                + s2+ "(id: " + orthomatcher.shortAnnot.getId() + ")");
    	      }
    	      result = retVal;
	      }
	      
	    } // if (tokensLongAnnot.countTokens()>1
	    
	    if (result) OrthoMatcherHelper.usedRule(12);
	    return result;
	}
	
  public String getId(){
    return "MatchRule12";
  }
}
