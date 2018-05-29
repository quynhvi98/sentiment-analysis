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
package vn.com.epi.gate.rule;

import java.util.HashMap;

import vn.com.epi.gate.VietnameseOrthoMatcher;
import vn.com.epi.gate.OrthoMatcherHelper;
import vn.com.epi.gate.OrthoMatcherRule;

/** RULE #0: If the two names are listed in table of
 * spurius matches then they do NOT match
 * Condition(s): -
 * Applied to: all name annotations
 */
public class SpuriusNotMatch implements OrthoMatcherRule {
 
    VietnameseOrthoMatcher orthomatcher;
	
	  public SpuriusNotMatch(VietnameseOrthoMatcher orthmatcher){
		   this.orthomatcher=orthmatcher;
	  }
	 
	  public boolean value(String string1,String string2){
		 
	      boolean result=false;
	    
	      if (orthomatcher.spur_match.containsKey(string1)
	            && orthomatcher.spur_match.containsKey(string2) )
	      result=
	      orthomatcher.spur_match.get(string1).toString().equals(orthomatcher.spur_match.get(string2).toString());

	      if (result) OrthoMatcherHelper.usedRule(0);
	      
	      return result;
	  }
	  
	  public String getId(){
	    return "MatchRule0";
	  }
}
