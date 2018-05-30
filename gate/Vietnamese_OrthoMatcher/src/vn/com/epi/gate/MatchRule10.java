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

import gate.Annotation;


/**
 * RULE #10: is one name the reverse of the other
 * reversing around prepositions only?
 * e.g. "" == ""
 * Condition(s): case-sensitive match
 * Applied to: organisation annotations only
 */

public class MatchRule10 implements OrthoMatcherRule {

	VietnameseOrthoMatcher orthomatcher;
		
	public MatchRule10(VietnameseOrthoMatcher orthmatcher){
			this.orthomatcher=orthmatcher;
	}
	
	public boolean value(String s1, String s2) {
		
	    boolean result=false;
	  
		  String token = null;
	    String previous_token = null;
	    String next_token = null;
	    boolean invoke_rule=false;

	    if (orthomatcher.tokensLongAnnot.size() >= 3
	            && orthomatcher.tokensShortAnnot.size() >= 2) {

	      // first get the tokens before and after the preposition
	      int i = 0;
	      for (; i< orthomatcher.tokensLongAnnot.size(); i++) {
	        token = (String)
	        ((Annotation) orthomatcher.tokensLongAnnot.get(i)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME);
	        if (orthomatcher.prepos.containsKey(token)) {
	          invoke_rule=true;
	          break;
	        }//if
	        previous_token = token;
	      }//while

	      if (! invoke_rule)
	        result = false;
	      else {
    	      if (i < orthomatcher.tokensLongAnnot.size()
    	              && previous_token != null) {
    	        next_token= (String)
    	        ((Annotation) orthomatcher.tokensLongAnnot.get(i++)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME);
    	        
    	        String s21 = (String)
              ((Annotation) orthomatcher.tokensShortAnnot.get(0)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME);
              String s22 = (String)
              ((Annotation) orthomatcher.tokensShortAnnot.get(1)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME);
              // then compare (in reverse) with the first two tokens of s2
              if (OrthoMatcherHelper.straightCompare(next_token,(String) s21,orthomatcher.caseSensitive)
                      && OrthoMatcherHelper.straightCompare(previous_token, s22,orthomatcher.caseSensitive))
                result = true ;
    	        }
    	      else result = false;
	      }
	    }//if (tokensLongAnnot.countTokens() >= 3
	    
	    if (result) OrthoMatcherHelper.usedRule(10);
	    return result;
	}
	
  public String getId(){
    return "MatchRule10";
  }
}
