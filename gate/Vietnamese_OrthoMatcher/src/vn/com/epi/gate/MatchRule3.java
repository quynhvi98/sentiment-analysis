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

import java.util.ArrayList;

import gate.Annotation;

/**
 * RULE #3: adding a possessive at the end
 * of one name causes a match
 * e.g. "Standard and Poor" == "Standard and Poor's"
 * and also "Standard and Poor" == "Standard's"
 * Condition(s): case-insensitive match
 * Applied to: all name annotations
 */
public class MatchRule3 implements OrthoMatcherRule {

	  VietnameseOrthoMatcher orthomatcher;
		
		public MatchRule3(VietnameseOrthoMatcher orthmatcher){
			this.orthomatcher=orthmatcher;
		}
	
	public boolean value(String s1,  String s2) { //short string

	  boolean result=false;
	  
		if (s2.endsWith("'s") || s2.endsWith("'")
	            ||(s1.endsWith("'s")|| s1.endsWith("'"))) {

	      String s2_poss = null;

	      if (!s2.endsWith("'s")) s2_poss = s2.concat("'s");
	      else s2_poss = s2.concat("'");

	      if (s2_poss != null && OrthoMatcherHelper.straightCompare(s1, s2_poss,orthomatcher.caseSensitive)) {
	        if (orthomatcher.log.isDebugEnabled()) {
	          orthomatcher.log.debug("rule 3 matched " + s1 + " to " + s2);
	        }
	        result = true;
	      }

	      // now check the second case i.e. "Standard and Poor" == "Standard's"
	      String token = (String)
	      ((Annotation) orthomatcher.tokensLongAnnot.get(0)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME);

	      if (!token.endsWith("'s")) s2_poss = token.concat("'s");
	      else s2_poss = token.concat("'");

	      if (s2_poss != null && OrthoMatcherHelper.straightCompare(s2_poss,s2,orthomatcher.caseSensitive)) {
	        if (orthomatcher.log.isDebugEnabled()){
	          orthomatcher.log.debug("rule 3 matched " + s1 + " to " + s2);
	        }
	        result = true;
	      }

	    } // if (s2.endsWith("'s")
		
		  if (result) OrthoMatcherHelper.usedRule(3);
		  
	    return result;
	}
	
  public String getId(){
    return "MatchRule3";
  }
}
