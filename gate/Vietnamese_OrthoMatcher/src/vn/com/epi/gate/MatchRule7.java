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
 * RULE #6: if one name is the acronym of the other
 * e.g. "Imperial Chemical Industries" == "ICI"
 * Applied to: organisation annotations only
 */

public class MatchRule7 implements OrthoMatcherRule {

    VietnameseOrthoMatcher orthomatcher;
	
	public MatchRule7(VietnameseOrthoMatcher orthmatcher){
		this.orthomatcher=orthmatcher;
	}
	
	public boolean value(String s1, String s2) {
		
	      boolean result=false;
	  
	      int i = 0;

		    //check and if the shorted string has a space in it, then it's not
		    //an acronym
		    if (s2.indexOf(" ") > 0)
		      result = false;
		    else {
    		    // Abbreviations of one-word names are very rare and can lead to weird errors
    		    if (orthomatcher.tokensLongAnnot.size() <= 1) {
    		      result = false;
    		    }
    		    else {
        		    //Out.prln("Acronym: Matching " + s1 + "and " + s2);
        		    StringBuffer acronym_s1 = new StringBuffer("");
        		    StringBuffer acronymDot_s1 = new StringBuffer("");
        
        		    for ( ;i < orthomatcher.tokensLongAnnot.size(); i++ ) {
        		      String toAppend = ( (String) ((Annotation) orthomatcher.tokensLongAnnot.get(i)
        		      ).getFeatures().get(VietnameseOrthoMatcher.TOKEN_STRING_FEATURE_NAME)).substring(0,1);
        		      acronym_s1.append(toAppend);
        		      acronymDot_s1.append(toAppend);
        		      acronymDot_s1.append(".");
        		    }
        
        		    //Out.prln("Acronym dot: To Match " + acronymDot_s1 + "and " + s2);
        		    //Out.prln("Result: " + matchRule1(acronymDot_s1.toString(),s2,caseSensitive));
        
        		    if (OrthoMatcherHelper.straightCompare(acronym_s1.toString(),s2,orthomatcher.caseSensitive) ||
        		    		OrthoMatcherHelper.straightCompare(acronymDot_s1.toString(),s2,orthomatcher.caseSensitive) )
        		      result = true;
    		    }
		    }

		    if (result) OrthoMatcherHelper.usedRule(7);
		    return result;
	}
	
  public String getId(){
    return "MatchRule7";
  }
}
