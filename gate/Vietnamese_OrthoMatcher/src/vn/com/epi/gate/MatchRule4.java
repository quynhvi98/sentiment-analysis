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

import java.util.Iterator;

import static vn.com.epi.gate.VietnameseOrthoMatcher.*;

/**
 * RULE #4: Does the first non-punctuation token from the long string match
 * the first token from the short string?
 * e.g. "fred jones" == "fred"
 * Condition(s): case-insensitive match
 * Applied to: person annotations
 *
 * Modified by Andrew Borthwick, Spock Networks:  Disallow stop words
 */
public class MatchRule4 implements OrthoMatcherRule {
	
	VietnameseOrthoMatcher orthomatcher;
	
	public MatchRule4(VietnameseOrthoMatcher orthmatcher){
		this.orthomatcher=orthmatcher;
	}
	
	public boolean value(String s1, String s2) {

		boolean allTokensMatch = true;
	    // Out.prln("MR4:  Matching" + s1 + " with " + s2);

	    Iterator tokensLongAnnotIter = orthomatcher.tokensLongAnnot.iterator();
	    Iterator tokensShortAnnotIter = orthomatcher.tokensShortAnnot.iterator();
	    while (tokensLongAnnotIter.hasNext() && tokensShortAnnotIter.hasNext()) {
	      Annotation token = (Annotation) tokensLongAnnotIter.next();
	      if (((String)token.getFeatures().get(TOKEN_KIND_FEATURE_NAME)).equals(PUNCTUATION_VALUE) ||
	              token.getFeatures().containsKey("ortho_stop"))
	        continue;
	      if (! ((String)(((Annotation) tokensShortAnnotIter.next()).
	              getFeatures().get(TOKEN_STRING_FEATURE_NAME))).equals(
	                      (String) token.getFeatures().get(TOKEN_STRING_FEATURE_NAME))) {
	        allTokensMatch = false;
	        break;
	      } // if (!tokensLongAnnot.nextToken()
	    } // while
	//  if (allTokensMatch)
	//  Out.prln("rule4 fired. result is: " + allTokensMatch);
	     if (allTokensMatch && log.isDebugEnabled()) {
	       log.debug("rule 4 matched " + s1 + "(id: " + orthomatcher.longAnnot.getId() + ") to " + s2+ "(id: " + orthomatcher.shortAnnot.getId() + ")");
	     }
	     
	    if (allTokensMatch) OrthoMatcherHelper.usedRule(4);
	    
	    return allTokensMatch;
	}
	
  public String getId(){
    return "MatchRule4";
  }
}
