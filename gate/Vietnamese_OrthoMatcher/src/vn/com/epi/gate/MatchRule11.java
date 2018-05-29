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
 * RULE #11: does one name consist of contractions
 * of the first two tokens of the other name?
 * e.g. "Communications Satellite" == "ComSat"
 * and "Pan American" == "Pan Am"
 * Condition(s): case-sensitive match
 * Applied to: organisation annotations only
 */
public class MatchRule11 implements OrthoMatcherRule {

	VietnameseOrthoMatcher orthomatcher;
	
	public MatchRule11(VietnameseOrthoMatcher orthmatcher){
			this.orthomatcher=orthmatcher;
	}
	
	public boolean value(String s1, String s2) {
	    // first do the easy case e.g. "Pan American" == "Pan Am"
      boolean result =false;
	    
	    String token11 = null;
	    String token12 = null;
	    String token21 = null;
	    String token22 = null;

	    if (orthomatcher.tokensLongAnnot.size() < 2)
	      result = false;
	    else {
    	    // 1st get the first two tokens of s1
    	    token11 = (String)
    	    ((Annotation) orthomatcher.tokensLongAnnot.get(0)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME);
    	    token12 = (String)
    	    ((Annotation) orthomatcher.tokensLongAnnot.get(1)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME);
    
    	    // now check for the first case i.e. "Pan American" == "Pan Am"
    	    if (orthomatcher.tokensShortAnnot.size() == 2)  {
    
    	      token21 = (String)
    	      ((Annotation) orthomatcher.tokensShortAnnot.get(0)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME);
    	      token22 = (String)
    	      ((Annotation) orthomatcher.tokensShortAnnot.get(0)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME);
    
    	      if (token11.startsWith(token21)
    	              && token12.startsWith(token22))
    	        result = true;
    
    	    } // if (tokensShortAnnot.countTokens() == 2)
    
    	    // now the second case e.g.  "Communications Satellite" == "ComSat"
    	    else if (orthomatcher.tokensShortAnnot.size()==1 && s2.length()>=3) {
    
    	      // split the token into possible contractions
    	      // ignore case for matching
    	      for (int i=2;i<s2.length();i++) {
    	        token21=s2.substring(0,i+1);
    	        token22=s2.substring(i+1);
    
    	        if (token11.startsWith(token21)
    	                && token12.startsWith(token22))
    	          result = true;
    	      }// for
    	    } // else if
	    }

	    if (result) OrthoMatcherHelper.usedRule(11);
	    return result;
	}
	
  public String getId(){
    return "MatchRule11";
  }
}
