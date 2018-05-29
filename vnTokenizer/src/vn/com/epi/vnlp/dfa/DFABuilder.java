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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;



import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

public class DFABuilder {
	
	private ArrayList<Object> stateData = new ArrayList<Object>();
	private ArrayList<TransitionMap> transitions = new ArrayList<TransitionMap>();
	
	private Multimap<Integer, Transition> inTransitions = HashMultimap.create();
	private Multimap<Integer, Transition> outTransitions = HashMultimap.create();
	private int startState;	
	private int uniqueId = 0;	

	public DFABuilder(){
		addState(DFA.STATE_TYPE_START);	
	}
	public int addState(int type) {
		int createdId = uniqueId;
		addState(uniqueId, type);
		return createdId;
	}
	
	public int addState(int type, Object data) {
		int createdId = uniqueId;
		addState(uniqueId, type, data);
		return createdId;
	}
	
	public void addState(int id, int type) {
		addState(id, type, null);
	}

	public void addState(int id, int type, Object data) {
		while (id >= stateData.size()) {
			stateData.add(0);
		}
		if (data == null && type == DFA.STATE_TYPE_FINAL) {
			stateData.set(id, DFA.FINAL_MARKER); // mark it as final			
		}
		else {
			stateData.set(id, data);
		}
		if (type == DFA.STATE_TYPE_START) {
			startState = id;
		}
		if (id >= uniqueId) {
			uniqueId = id+1;
		}
	}

	public void addTransition(int source, char symbol, int target) {		
		while (target >= transitions.size()||source>=transitions.size()) {
			transitions.add(null);
		}
		TransitionMap sourceTrans = transitions.get(source);
		if (sourceTrans == null) {
			sourceTrans = new MultiRangeTranslationMap(source);
			transitions.set(source, sourceTrans);
		}		
		sourceTrans.setAt(symbol, target);
		TransitionMap targetTrans=transitions.get(target);
		if(targetTrans==null){
			targetTrans = new MultiRangeTranslationMap(source);
			transitions.set(target, targetTrans);
		}
		Transition transition = new Transition(symbol, source, target);
		outTransitions.put(source, transition);
		inTransitions.put(target, transition);
	}
		
	public DFA build() {
		Object[] stateTypeArr = stateData.toArray();		
		MultiRangeTranslationMap[] transitionArr = new MultiRangeTranslationMap[transitions.size()];
		transitions.toArray(transitionArr);	
		return new DFA(startState, stateTypeArr, transitionArr);
	}
	
	public int getStartState() {
		return startState;
	}
	public int getType(int state) {
		return stateData.get(state) == null ? DFA.STATE_TYPE_NORMAL : DFA.STATE_TYPE_FINAL;
	}
	
	public boolean isFinal(int state){
		if(getType(state)==DFA.STATE_TYPE_FINAL){
			return true;
		}
		return false;
	}
	
	public int findTarget(int state, char symbol) {
		TransitionMap trans = transitions.get(state);
		return trans == null ? -1 : trans.getAt(symbol);
	}
	
    public boolean isConfluence(int state) {
    	return inTransitions.get(state).size() > 1;
    }

	public Collection<Transition> getOutTransitions(int state) {
		return outTransitions.get(state);
	}
	
	public Collection<Transition> getInTransitions(int state) {
		return inTransitions.get(state);
	}	
	
	public boolean isEquivalentState(int firstState,int secondState){
		if(secondState<0||firstState<0){
			return false;
		}
		if(firstState==secondState){
			return false;
		}
		if(isFinal(firstState)!=isFinal(secondState)){
			return false;
		}

		Collection<Transition> firstTransitions = getOutTransitions(firstState);
		Collection<Transition> secondTransitions = getOutTransitions(secondState);
		
		int transSize=firstTransitions.size();
		if(transSize!=secondTransitions.size()){
			return false;
		}
			
		Transition[] firstTransArray= firstTransitions.toArray(new Transition[transSize]);
		
		boolean isEquivelance=false;
		for(int i=0;i<transSize;i++){
			isEquivelance=false;
				if(firstTransArray[i].getTarget()==findTransition(secondState, firstTransArray[i].getSymbol())){
					isEquivelance=true;
				}//			
			if(!isEquivelance){
				return false;
			}
		}
		return true;
	}
	
	public void deleteState(int child) {
		// TODO Auto-generated method stub
		if(child>=0){
			Collection<Transition> nextTransitions=getOutTransitions(child);
			Collection<Transition> prevTransitions=getInTransitions(child);			
			Iterator<Transition> itor=nextTransitions.iterator();
			Transition curTrans;
			int nextState;
			while(itor.hasNext()){
				curTrans=itor.next();
				nextState=curTrans.getTarget();
				getInTransitions(nextState).remove(curTrans);				
			}
			itor=prevTransitions.iterator();
			while(itor.hasNext()){
				curTrans=itor.next();
				nextState=curTrans.getSource();
				getOutTransitions(nextState).remove(curTrans);				
			}
			if(child<transitions.size()){
				transitions.set(child, null);
			}
			stateData.set(child, null);		
		}
	}

	public int addNewState() {
		return addState(DFA.STATE_TYPE_NORMAL);
		
	}
	public int getStateHashCode(int state){
		int sum=0;
		Iterator<Transition> inItera= outTransitions.get(state).iterator();
		Transition curIntran=null;
		while(inItera.hasNext()){
			curIntran=inItera.next();		
			sum+=curIntran.getTarget();			
			sum+=curIntran.getSymbol();		
		}
		return sum%100000;
	}
	
		
	public Transition findFirstTransition(int source, char symbol) {
		// TODO Auto-generated method stub
		if(source>=0){
			Iterator<Transition> nextTransItor=getOutTransitions(source).iterator();			
			Transition curTransition=null;
			while(nextTransItor.hasNext()){
				curTransition=nextTransItor.next();
				if(curTransition.getSymbol()==symbol){
					return curTransition;
				}
			}
		}
		return null;
	}
	public void setTransition(int source, char symbol, int target) {
		// TODO Auto-generated method stub
		TransitionMap sourceTrans = transitions.get(source);
		if (sourceTrans == null) {
			sourceTrans = new MultiRangeTransalationDynamicMap(source);
			transitions.set(source, sourceTrans);
		}
		sourceTrans.setAt(symbol, target);
	}
	
	public void setIntransition(int target,Transition transition){
		inTransitions.put(target, transition);		
	}
	
	public void setFinal(int currentState) {
		stateData.set(currentState, DFA.FINAL_MARKER);
	}
	
	public int findTransition(int state, char symbol) {	
		if(state>=transitions.size()){
			return -1;
		}
		TransitionMap currTrans = transitions.get(state);
		if(currTrans==null){
			return -1;
		}
		state=currTrans.getAt(symbol);
		if(state>0){
			return state;
		}
		return -1;
		
	}
	
	public void removeInTransiton(int target, Transition transition) {
		// TODO Auto-generated method stub
		inTransitions.remove(target, transition);
	}
	
	
	
}
