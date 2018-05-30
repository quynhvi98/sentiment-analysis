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
 * RULE #14: if the last token of one name
 * matches the second name
 * e.g. "Hamish Cunningham" == "Cunningham"
 * Condition(s): case-insensitive match
 * Applied to: all person annotations
 *
 * Don't need to nicknames here
 */
public class MatchRule15 implements OrthoMatcherRule {

  VietnameseOrthoMatcher orthomatcher;
	
	public MatchRule15(VietnameseOrthoMatcher orthmatcher){
			this.orthomatcher=orthmatcher;
	}
	
	public boolean value(String s1, String s2) {
		
	  boolean result=false;
	  
	//  if (s1.equalsIgnoreCase("chin") || s2.equalsIgnoreCase("chin"))
	//  Out.prln("Rule 14 " + s1 + " and " + s2);
	    String s1_short = (String)
	    ((Annotation) orthomatcher.tokensLongAnnot.get(
	    		orthomatcher.tokensLongAnnot.size()-1)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME);
	//  Out.prln("Converted to " + s1_short);
	    if (orthomatcher.tokensLongAnnot.size()>1 && OrthoMatcherHelper.straightCompare(s1_short, s2,orthomatcher.caseSensitive)) {
	     if (orthomatcher.log.isDebugEnabled()) {
	       orthomatcher.log.debug("rule 15 matched " + s1 + "(id: " + orthomatcher.longAnnot.getId() + ") to "  + s2 
	                + "(id: " + orthomatcher.shortAnnot.getId() + ")");
	     }
	      result = true;
	    }

	    if (result) OrthoMatcherHelper.usedRule(15);
	    return result;
	}
	
  public String getId(){
    return "MatchRule15";
  }
}
