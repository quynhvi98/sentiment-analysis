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
package vn.com.epi.vnlp.dfa;

import java.util.HashMap;
import java.util.Map;


public class DFA {
	
	static final Object FINAL_MARKER = new Object();	

	public static final int STATE_TYPE_START = 0;
	public static final int STATE_TYPE_NORMAL = 1;
	public static final int STATE_TYPE_FINAL = 2;

	private int startState;
	private Object[] stateData;
	private int state;	
	private int stateCount;
	
	private TransitionMap[] transitions;	

	public DFA(int startState, Object[] stateData, TransitionMap[] transitions) {
		this.startState = startState;
		this.stateData = stateData;
		this.transitions = transitions;
		computeStateCount();
	}
	public DFA(){
			
	}
	
	public int getStartState() {
		return startState;
	}
	
	public int getStateCount() {	
		return stateCount;
	}
	private void computeStateCount(){
		for(int i=0;i<transitions.length;i++){
			if(transitions[i]!=null){
				stateCount++;
			}
		}		
	}

	public int getLastState() {
		return state;
	}	

	public boolean accept(String s) {
		return accept(s, startState);
	}

	public boolean accept(String s, int lastState) {
		state = lastState;
		for (int i = 0; i < s.length(); i++) {
			if (state < 0) {
				return false;
			}
			state = findTransition(state, s.charAt(i));
		}
		return state >= 0 && isFinal(state);
	}

	private int findTransition(int stateId, char symbol) {
		TransitionMap currTrans = transitions[stateId];
		return currTrans == null ? -1 : currTrans.getAt(symbol);
	}
	
	public TransitionMap getTransitions(int state) {
		return transitions[state];
	}		
	public int getType(int state) {
		return stateData[state] == null? STATE_TYPE_NORMAL : STATE_TYPE_FINAL;
	}
	
	public boolean isFinal(int state){
		if(getType(state)==DFA.STATE_TYPE_FINAL){
			return true;
		}
		return false;
	}
	
	public Object getData(int state) {
		return stateData[state];
	}
	
	public Object getLastData() {
		return stateData[state] == FINAL_MARKER ? null : stateData[state];
	}
	
	public Map<String, Integer> getTermList(){
		Map<String, Integer> result = new HashMap<String, Integer>();
		tryState(startState, result, new String());
		return result;
	}
	
	public void tryState(int currentState, Map<String, Integer> result, String term){
		if (currentState >= 0 && isFinal(currentState)){
			if (!result.containsKey(term))
				result.put(term, result.size());
		}
		TransitionMap currTrans = transitions[currentState];
		if (currTrans == null)
			return;
		String keys = currTrans.getKeys();
		if (keys.length() == 0)
			return;
		
		for (int i=0; i < keys.length(); i ++){
			char ch = keys.charAt(i);
			int target = currTrans.getAt(ch);
			if (target >= 0)
				tryState(target, result, term + ch);
		}
	}
	
}
