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

/** RULE #1: If the two names are identical then they are the same
 * no longer used, because I do the check for same string via the
 * hash table of previous annotations
 * Condition(s): depend on case
 * Applied to: annotations other than names
 */
public class MatchRule1 implements OrthoMatcherRule{

	VietnameseOrthoMatcher orthomatcher;
	
	public MatchRule1(VietnameseOrthoMatcher orthmatcher){
		this.orthomatcher=orthmatcher;
	}
	
	public boolean value(String s1,
	          String s2) {
	    
	  
	    boolean retVal = OrthoMatcherHelper.straightCompare(s1, s2, orthomatcher.caseSensitive);
	    //if straight compare didn't work, try a little extra logic
	    if (!retVal)
	      retVal = orthomatcher.getOrthography().fuzzyMatch(s1, s2);

	    if (retVal && orthomatcher.log.isDebugEnabled()) {
	      orthomatcher.log.debug("rule 1 matched " + s1 + "(id: " + orthomatcher.longAnnot.getId() + ") to "
	              + s2+ "(id: " + orthomatcher.shortAnnot.getId() + ")");
	    }
	  
	    if (retVal) OrthoMatcherHelper.usedRule(1);
	    
	    return retVal;
	}
	
  public String getId(){
    return "MatchRule1";
  }
}
