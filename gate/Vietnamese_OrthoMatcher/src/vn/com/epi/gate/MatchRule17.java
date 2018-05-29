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

import gate.creole.ANNIEConstants;

/**
 * RULE #16: Conservative match rule
 * Require every token in one name to match the other except for tokens that are on a stop word list
 */
public class MatchRule17 implements OrthoMatcherRule {

    VietnameseOrthoMatcher orthomatcher;
	
	public MatchRule17(VietnameseOrthoMatcher orthmatcher){
			this.orthomatcher=orthmatcher;
	}
	
	public boolean value(String s1, String s2) {
	  
	  boolean result=false;
	  OrthoMatcherHelper.usedRule(17);
	  
		//reversed execution of allNonStopTokensInOtherAnnot
		if (orthomatcher.getOrthography().allNonStopTokensInOtherAnnot(orthomatcher.tokensLongAnnot, orthomatcher.tokensShortAnnot,ANNIEConstants.TOKEN_STRING_FEATURE_NAME,orthomatcher.caseSensitive)) {
		      result = orthomatcher.getOrthography().allNonStopTokensInOtherAnnot(orthomatcher.tokensShortAnnot, orthomatcher.tokensLongAnnot,ANNIEConstants.TOKEN_STRING_FEATURE_NAME,orthomatcher.caseSensitive);
		    }
		
		return result;
	}
	
  public String getId(){
    return "MatchRule17";
  }
}
