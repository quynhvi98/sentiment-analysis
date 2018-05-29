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

import java.util.HashSet;
import java.util.Map;

import gate.Annotation;

/**
 * RULE #15: Does every token in the shorter name appear in the longer name?
 */
public class MatchRule16 implements OrthoMatcherRule {

    VietnameseOrthoMatcher orthmatcher;
	
	public MatchRule16(VietnameseOrthoMatcher orthomatcher){
			this.orthmatcher=orthomatcher;
	}
	
	public boolean value(String s1, String s2) {
	
	  boolean result =true;
	  
		//do a token-by-token test
	    Annotation token1, token2;
	    // catch (ExecutionException e) {}
	    for (int i=0; i < orthmatcher.tokensShortAnnot.size(); i++) {
	      token1 = (Annotation) orthmatcher.tokensShortAnnot.get(i);
	      //first check if not punctuation, because we need to skip it
	      if (token1.getFeatures().get(orthmatcher.TOKEN_KIND_FEATURE_NAME).equals(orthmatcher.PUNCTUATION_VALUE))
	        continue;

	      String ts1 = (String)token1.getFeatures().get(orthmatcher.TOKEN_STRING_FEATURE_NAME);
	      boolean foundMatch = false;
	      for (int j=0; j<orthmatcher.tokensLongAnnot.size(); j++) {
	        // Out.prln("i = " + i);
	        token2 = (Annotation) orthmatcher.tokensLongAnnot.get(j);
	        if (token2.getFeatures().get(VietnameseOrthoMatcher.TOKEN_KIND_FEATURE_NAME).equals(VietnameseOrthoMatcher.PUNCTUATION_VALUE))
	          continue;

	        String ts2 = (String)token2.getFeatures().get(VietnameseOrthoMatcher.TOKEN_STRING_FEATURE_NAME);

	        if (i == 0 && j == 0) {
	          foundMatch = orthmatcher.getOrthography().fuzzyMatch(ts1, ts2);
	        }
	        else {
	          if (orthmatcher.caseSensitive) {
	            if (ts2.equals(ts1)) {
	              foundMatch = true;
	              break;
	            }
	          }
	          else {
	            if (ts2.equalsIgnoreCase(ts1)) {
	              foundMatch = true;
	              break;
	            }
	          }
	        }
	      }//for
	      //if no match for the current tokenShortAnnot, then it is not a coref of the
	      //longer annot
	      if (!foundMatch)
	        result = false;
	    } // for

	    //only get to here if all word tokens in the short annot were found in
	    //the long annot, so there is a coref relation
	    if (orthmatcher.log.isDebugEnabled())
	      orthmatcher.log.debug("rule 16 matched " + s1 + " to " + s2);
	    
	    if(result) OrthoMatcherHelper.usedRule(16);
	    return result;
	}
	
  public String getId(){
    return "MatchRule16";
  }
}
