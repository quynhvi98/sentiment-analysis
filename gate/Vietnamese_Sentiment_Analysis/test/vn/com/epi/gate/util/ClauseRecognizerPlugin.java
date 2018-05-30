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
package vn.com.epi.gate.util;

import gate.Annotation;
import gate.Factory;
import gate.Node;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.util.OffsetComparator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

/**
 * Posted from May 30, 2018 10:54 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public class ClauseRecognizerPlugin extends AbstractLanguageAnalyser {

	private static final long serialVersionUID = 1L;
	
	@Override
	public Resource init() throws ResourceInstantiationException {
		return super.init();
	}
	
	@Override
	public void execute() throws ExecutionException {
		// sort sentences for easier debugging
//		List<Annotation> sentences =
//				new ArrayList<Annotation>(
//						document.getAnnotations().get("Sentence"));
//		Collections.sort(sentences, comparator);
		// don't sort sentences for better performance
		Set<Annotation> sentences = document.getAnnotations().get("Sentence");
		
		for (Annotation sent : sentences) {
			List<Annotation> tokens = getContainedTokens(sent);
			Collections.sort(tokens, comparator);
			
			Integer lastClauseId = null;
			int clauseEnd = tokens.size()-1;
			int state = STATE_AFTER_VERB;
			for (int i = tokens.size()-1; i >= 0; i--) {
				Annotation token = tokens.get(i);
				switch (state) {
				case STATE_AFTER_VERB:
					if (isVerb(token)) {
						state = STATE_VERB;
					}
					break;
				case STATE_VERB:
					if (isValidSubject(token)) {
						state = STATE_SUBJECT;
					}
					break;
				case STATE_SUBJECT:
					if (isVerb(token)) {
						state = STATE_VERB;
					} else if (isValidClauseSeperator(token)) {
						lastClauseId = markClause(tokens, i+1, clauseEnd);
						clauseEnd = i-1;
						state = STATE_AFTER_VERB;
					}
					break;
				}
			}

			if ((state == STATE_SUBJECT || lastClauseId == null)
					&& clauseEnd > 0) {
				markClause(tokens, 0, clauseEnd);
			} else {
				Node startNode = tokens.get(0).getStartNode();
				Node endNode = tokens.get(tokens.size()-1).getEndNode();
				if (lastClauseId != null) {
					Annotation lastClause = document.getAnnotations().get(lastClauseId);
					document.getAnnotations().remove(lastClause);
					endNode = lastClause.getEndNode();
				}
				document.getAnnotations().add(startNode, endNode, "Clause",
						Factory.newFeatureMap());
			}
		}
	}

	private ArrayList<Annotation> getContainedTokens(Annotation sent) {
		return new ArrayList<Annotation>(document
				.getAnnotations()
				.getContained(sent.getStartNode().getOffset(),
						sent.getEndNode().getOffset()).get("Token"));
	}
	
	private Integer markClause(List<Annotation> tokens, int startIndex, int endIndex) {
		Node start = tokens.get(startIndex).getStartNode(); 
		Node end = tokens.get(endIndex).getEndNode();
		return document.getAnnotations().add(start, end, "Clause",
				Factory.newFeatureMap());
	}

	private boolean isValidClauseSeperator(Annotation token) {
		String category = getCategory(token);
		return ",".equals(category) || ";".equals(category) || category.startsWith("C");
	}

	private boolean isValidSubject(Annotation token) {
		final String category = getCategory(token);
		return category.startsWith("N") || category.startsWith("P")
				|| category.startsWith("L")	|| category.startsWith("M");
	}

	private boolean isVerb(Annotation token) {
		return getCategory(token).startsWith("V");
	}

	private String getCategory(Annotation token) {
		return (String) token.getFeatures().get("category");
	}

	private static final int STATE_AFTER_VERB = 0;
	private static final int STATE_VERB = 1;
	private static final int STATE_SUBJECT = 2;
	private Comparator<Annotation> comparator = new OffsetComparator();
	
}
