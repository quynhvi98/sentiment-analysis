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
