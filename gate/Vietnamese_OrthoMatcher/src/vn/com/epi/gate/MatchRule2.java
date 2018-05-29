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

/**
 * RULE #2: if the two names are listed as equivalent in the
 * lookup table (alias) then they match
 * Condition(s): -
 * Applied to: all name annotations
 */
public class MatchRule2 implements OrthoMatcherRule {

  VietnameseOrthoMatcher orthomatcher;
	
	public MatchRule2(VietnameseOrthoMatcher orthmatcher){
		this.orthomatcher=orthmatcher;
	}
	
	public boolean value(String s1, String s2) {

	  boolean result=false;
	  
	    if (orthomatcher.alias.containsKey(s1) && orthomatcher.alias.containsKey(s2)) {
	      if (orthomatcher.alias.get(s1).toString().equals(orthomatcher.alias.get(s2).toString())) {
	        if (orthomatcher.log.isDebugEnabled()) {
	          orthomatcher.log.debug("rule 2 matched " + s1 + " to " + s2);
	        }
	        result=true;
	      }
	    }

	    if(result) OrthoMatcherHelper.usedRule(2);
	    
	    return result;
	  }
	
  public String getId(){
    return "MatchRule2";
  }
}
