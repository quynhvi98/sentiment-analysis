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
 * RULE #5: if the 1st token of one name
 * matches the second name
 * e.g. "Pepsi Cola" == "Pepsi"
 * Condition(s): case-insensitive match
 * Applied to: all name annotations
 *
 * Note that we don't want to use nicknames here because you don't use nicknames for last names
 */
public class MatchRule6 implements OrthoMatcherRule {

  VietnameseOrthoMatcher orthomatcher;
	
	public MatchRule6(VietnameseOrthoMatcher orthmatcher){
		this.orthomatcher=orthmatcher;
	}
	
	public boolean value(String s1, String s2) {
	  
	  boolean result=false;
	  
		   if (orthomatcher.tokensLongAnnot.size()> 1 &&
	            ((Annotation) orthomatcher.tokensLongAnnot.get(0)).getFeatures().get("kind").equals("number"))
	     result=false;
		   {
        	    //  if (s1.startsWith("Patrick") || s2.startsWith("Patrick")) {
        	    //  Out.prln("Rule 5: " + s1 + "and " + s2);
        	    //  }
        
        	    //require that when matching person names, the shorter one to be of length 1
        	    //for the rule to apply. In other words, avoid matching Peter Smith and
        	    //Peter Kline, because they share a Peter token.
        	    if ( (orthomatcher.shortAnnot.getType().equals(orthomatcher.personType)
        	            || orthomatcher.longAnnot.getType().equals(orthomatcher.personType)
        	    )
        	    &&
        	    orthomatcher.tokensShortAnnot.size()>1
        	    )
        	      result = false;
        	    else {
              	    if (orthomatcher.tokensLongAnnot.size()<=1)
              	      result = false; else 
              	    if (((Annotation) orthomatcher.tokensShortAnnot.get(0)).getFeatures().containsKey("ortho_stop"))
              	      result = false; else
              	    
              	    {result = OrthoMatcherHelper.straightCompare((String)
              	            ((Annotation) orthomatcher.tokensLongAnnot.get(0)
              	            ).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME),
              	            s2,
              	            orthomatcher.caseSensitive);
              	    }
        	    }
	    
		   }
		   
		  if (result && orthomatcher.log.isDebugEnabled()) {
         orthomatcher.log.debug("rule 6 matched " + s1 + " to " + s2);
       }
	    if (result) OrthoMatcherHelper.usedRule(6);
	    
	    return result;
	}
	
  public String getId(){
    return "MatchRule6";
  }
}
