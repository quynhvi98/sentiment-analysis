package vn.com.epi.gate;

import java.util.HashSet;
import java.util.Map;

import gate.Annotation;

/**
 * RULE #9: does one of the names match the token
 * just before a trailing company designator
 * in the other name?
 * The company designator has already been chopped off,
 * so the token before it, is in fact the last token
 * e.g. "R.H. Macy Co." == "Macy"
 * Applied to: organisation annotations only
 */
public class MatchRule9 implements OrthoMatcherRule {

  VietnameseOrthoMatcher orthomatcher;
	
	public MatchRule9(VietnameseOrthoMatcher orthmatcher){
		this.orthomatcher=orthmatcher;
	}
	
	public boolean value(String s1, String s2) {
	  
	  boolean result=false;
	  
		// if (s1.equalsIgnoreCase("news") || s2.equalsIgnoreCase("news"))
		//  Out.prln("Rule 9 " + s1 + " and " + s2);
		    String s1_short = (String)
		    ((Annotation) orthomatcher.tokensLongAnnot.get(
		    		orthomatcher.tokensLongAnnot.size()-1)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME);
		//  Out.prln("Converted to " + s1_short);
		    if (orthomatcher.tokensLongAnnot.size()>1) {
		      boolean matched = OrthoMatcherHelper.straightCompare(s1_short, s2, orthomatcher.caseSensitive);
		      //we need to make sure all names match, instead of assuming transitivity,
		      //to avoid matching BBC News with News then News with ITV News, which
		      //by transitivity leads to BBC News matching ITV News which is not what
		      //we want
		      if (matched)
		    	  orthomatcher.allMatchingNeeded = true;
		      result = matched;
		    } //if

		    if (result) OrthoMatcherHelper.usedRule(9);
		    return result;
	}
	
  public String getId(){
    return "MatchRule9";
  }
}
