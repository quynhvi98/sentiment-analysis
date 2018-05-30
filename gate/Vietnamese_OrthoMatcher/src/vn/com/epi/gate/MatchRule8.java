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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import gate.Annotation;

/**
 * RULE #7: if one of the tokens in one of the
 * names is in the list of separators eg. "&"
 * then check if the token before the separator
 * matches the other name
 * e.g. "R.H. Macy & Co." == "Macy"
 * Condition(s): case-sensitive match
 * Applied to: organisation annotations only
 */
public class MatchRule8 implements OrthoMatcherRule {

  VietnameseOrthoMatcher orthomatcher;
	
	public MatchRule8(VietnameseOrthoMatcher orthmatcher){
		this.orthomatcher=orthmatcher;
	}
	
	public boolean value(String s1, String s2) {
		
	    boolean result=false;
	   
		  //don't try it unless the second string is just one token
	    if (orthomatcher.tokensShortAnnot.size() != 1)
	      result = false;
	    else
	    {
      	    String previous_token = null;
      
      	    for (int i = 0;  i < orthomatcher.tokensLongAnnot.size(); i++ ) {
      	      if (orthomatcher.connector.containsKey( ((Annotation) orthomatcher.tokensLongAnnot.get(i)
      	      ).getFeatures().get(VietnameseOrthoMatcher.TOKEN_STRING_FEATURE_NAME) )) {
      	        previous_token = (String) ((Annotation) orthomatcher.tokensLongAnnot.get(i-1)
      	        ).getFeatures().get(VietnameseOrthoMatcher.TOKEN_STRING_FEATURE_NAME);
      
      	        break;
      	      }
      	    }
      
      	    //now match previous_token with other name
      	    if (previous_token != null) {
      //	    if (s1.equalsIgnoreCase("chin") || s2.equalsIgnoreCase("chin"))
      //	    Out.prln("Rule7");
      	      result = OrthoMatcherHelper.straightCompare(previous_token,s2,orthomatcher.caseSensitive);
      
      	    }
	    }
	    
	    if (result) OrthoMatcherHelper.usedRule(8);
	    return result;

	}
	
  public String getId(){
    return "MatchRule8";
  }
}
