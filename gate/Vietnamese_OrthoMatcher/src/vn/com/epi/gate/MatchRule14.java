package vn.com.epi.gate;

import gate.Annotation;

/**
 * RULE #13: do multi-word names match except for
 * one token e.g.
 * "Second Force Recon Company" == "Force Recon Company"
 * Note that this rule has NOT been used in LaSIE's 1.5
 * namematcher
 * Restrictions: - remove cdg first
 *               - shortest name should be 2 words or more
 *               - if N is the number of tokens of the longest
 *                 name, then N-1 tokens should be matched
 * Condition(s): case-sensitive match
 * Applied to: organisation or person annotations only
 */
public class MatchRule14 implements OrthoMatcherRule {

  VietnameseOrthoMatcher orthomatcher;
	
	public MatchRule14(VietnameseOrthoMatcher orthmatcher){
			this.orthomatcher=orthmatcher;
	}
	
	public boolean value(String s1, String s2) {
	    
	    boolean result = false; 
	  
	    int matched_tokens = 0, mismatches = 0;

	    // if names < 2 words then rule is invalid
	    if (orthomatcher.tokensLongAnnot.size() < 3 || orthomatcher.tokensShortAnnot.size() < 2) 
	      result =  false;
	    else {
    	    // now do the matching
    	    for (int i=0,j= 0; i < orthomatcher.tokensShortAnnot.size() && mismatches < 2; i++) {
    
    //	    Out.prln("i = " + i);
    //	    Out.prln("j = " + j);
    	      if ( ((Annotation) orthomatcher.tokensLongAnnot.get(j)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME).equals(
    	              ((Annotation) orthomatcher.tokensShortAnnot.get(i)).getFeatures().get(orthomatcher.TOKEN_STRING_FEATURE_NAME)) ) {
    	        matched_tokens++;
    	        j++;
    	      } else
    	        mismatches++;
    	    } // for
    
    	    if (matched_tokens >= orthomatcher.tokensLongAnnot.size()-1)
    	      result = true;
	    }

	    if (result) OrthoMatcherHelper.usedRule(14);
	    return result;
	}
	
  public String getId(){
    return "MatchRule14";
  }
}
