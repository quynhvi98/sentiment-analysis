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

import gnu.trove.list.TIntList;
import gnu.trove.list.array.TIntArrayList;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import vn.com.epi.vnlp.dfa.io.DfaXmlWriter;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;



//import oss.ngocminh.vnlp.dfa.DFABuilder;

public class MinimalDFABuilder {
	
	private DFABuilder dfa;	
	
	/*
	 * The first confluence state.
	 */
	private int firstState;

	/*
	 * The last state on the common prefix path.
	 */
	private int lastState;

	/*
	 * States on the common prefix path.
	 */
	private TIntList commonPrefix;

	/*
	 * The beginning index of the suffix substring.
	 */
	private int suffixIndex;

	/*
	 * The index of the first confluence state when calling method void commonPrefix(T[] string).
	 */
	private int currentIndex;
	
	/*
	 * the indicator when finding uniquivelant state of the new added state
	 */
	private boolean isFindUnequivelantState=false;

	private Map<Integer,LinkedList<Integer>> register =new HashMap<Integer,LinkedList<Integer>>();	
	public MinimalDFABuilder() {
		dfa=new DFABuilder();			
	}	
	public DFA build(){
		return dfa.build();
	}

	private void commonPrefix(char[] string) {
		commonPrefix = new TIntArrayList();
		int state = dfa.getStartState();
		int i;
		for (i = 0; i < string.length && state >= 0; i++) {
			commonPrefix.add(state);
			state=dfa.findTransition(state, string[i]);
		}
		if (state >=0) {
			commonPrefix.add(state);	
			suffixIndex = i;
		}else{
			suffixIndex = i - 1;
		}	
	}

	private int firstConfluenceState() {
		for (int i = 0; i < commonPrefix.size(); i++) {
			if (dfa.isConfluence(commonPrefix.get(i))) {
				currentIndex = i;
				return commonPrefix.get(i);
			}
		}
		return -1;
	}

	private int clone(int state) {
		int newState = dfa.addNewState();	
		if(dfa.isFinal(state)){
			dfa.setFinal(newState);
		}
		Iterator<Transition> nextTransitionList = dfa.getOutTransitions(state).iterator();
		Transition nextTransitions=null;
		while(nextTransitionList.hasNext()){
			nextTransitions=nextTransitionList.next();
			dfa.addTransition(newState, nextTransitions.getSymbol(), nextTransitions.getTarget());
		}			
		return newState;			
	}

	private void addSuffix(char[] sentence) {
		List<Integer> processingState=new ArrayList<Integer>();	
		int currentState=lastState;
		int newState;		
		for (int i = suffixIndex; i < sentence.length; i++) {
			processingState.add(currentState);
			newState= dfa.addNewState();
			dfa.addTransition(currentState,sentence[i], newState);
			currentState = newState;			
		}		
		dfa.setFinal(currentState);
		
		int index=sentence.length-1;
		
		for(int i=processingState.size() - 1;i>=0;i--){
			replaceOrRegister(processingState.get(i), sentence[index--]);		
		}
	}

	private void replaceOrRegister(int state, char symbol) {
		int child=dfa.findTransition(state, symbol);
		if(isFindUnequivelantState){
			this.putStateToRegister(dfa.getStateHashCode(child), child);
			return;
		}
		
		int newChildId = getEquivalentStateId(child);
		if (newChildId >= 0) {
			dfa.deleteState(child);			
			dfa.addTransition(state,symbol, newChildId);
		}
		else {
			isFindUnequivelantState=true;
			this.putStateToRegister(dfa.getStateHashCode(child), child);
		}

	}
	private void putStateToRegister(int key,int value){
		LinkedList<Integer> col=register.get(key);
		if(col==null){
			col=new LinkedList<Integer>();
			col.add(value);
			register.put(key, col);
			return;
		}
		else{
			if(!col.contains(value)){
				col.add(value);
			}	
		}
	}
	
	private void removeStateFromRegister(int key,int value){
		LinkedList<Integer> col=register.get(key);
		if(col!=null){
			if(col.contains(value)){
				col.remove((Object)value);				
			}			
		}
	}

	private int getEquivalentStateId(int state) {				
		LinkedList<Integer> result=register.get(dfa.getStateHashCode(state));
		if(result!=null){
			for (int s : result) {
				if(dfa.isEquivalentState(state, s)){
					return s;
				}
			}
		}
		return -1;
	}

	public void unorederedAdd(char[] sentence) {
		// Find the common prefix and the suffix.
		commonPrefix(sentence);
		if (suffixIndex == sentence.length && dfa.isFinal(commonPrefix.get(commonPrefix.size() - 1)) ) {
			return;
		}

		// Find the first confluence state and append the suffix.
		firstState = firstConfluenceState();
		if (firstState < 0 ) {			
			lastState = commonPrefix.get(commonPrefix.size() - 1);				
			this.removeStateFromRegister(dfa.getStateHashCode(lastState),lastState);
		}
		else {
			lastState = clone(commonPrefix.get(commonPrefix.size() - 1));				
		}
		
		addSuffix(sentence);

		// If a confluence state is found, all states from the first confluence
		// state to the end of the common prefix path are cloned.
		Transition transition=null;
		int currentState =-1;
		if (firstState >= 0) {
			firstState = firstConfluenceState();
			for (int i = commonPrefix.size() - 2; i >= currentIndex; i--) {
				currentState = clone(commonPrefix.get(i));
				transition = dfa.findFirstTransition(currentState, sentence[i]);
				dfa.removeInTransiton(transition.getTarget(),transition);
				transition.setTarget(lastState);				
				dfa.setTransition(currentState, transition.getSymbol(), lastState);
				dfa.setIntransition(lastState,transition);
				replaceOrRegister(currentState, sentence[i]);
				lastState = currentState;

			}						
			//register.remove(dfa.getStateHashCode(commonPrefix.get(currentIndex - 1)),commonPrefix.get(currentIndex - 1));			
			this.removeStateFromRegister(dfa.getStateHashCode(commonPrefix.get(currentIndex - 1)),commonPrefix.get(currentIndex - 1));
			transition = dfa.findFirstTransition(commonPrefix.get(currentIndex - 1), sentence[currentIndex - 1]);
			dfa.removeInTransiton(transition.getTarget(),transition);
			transition.setTarget(lastState);			
			dfa.setTransition(transition.getSource(), transition.getSymbol(), lastState);
			dfa.setIntransition(lastState,transition);
			
			
			// TODO: These two lines are a trick for right hash
			
			
//			register.put(dfa.getStateHashCode(commonPrefix.get(currentIndex - 1)), commonPrefix.get(currentIndex - 1));			
//			register.put(dfa.getStateHashCode(lastState), lastState);
			this.putStateToRegister(dfa.getStateHashCode(commonPrefix.get(currentIndex - 1)), commonPrefix.get(currentIndex - 1));
			this.putStateToRegister(dfa.getStateHashCode(lastState), lastState);
		}
		else {
			currentIndex= commonPrefix.size() - 1;
		}

		// Check if there are changes of right languages recursively to the
		// initial state.
		boolean changed = true;
		//int oldState ;
		while (changed & currentIndex > 0) {
			currentIndex--;
			currentState = commonPrefix.get(currentIndex);
			//oldState = lastState;			
			this.removeStateFromRegister(dfa.getStateHashCode(lastState),lastState);
			replaceOrRegister(currentState, sentence[currentIndex]);
	//		changed = oldState != lastState;
			changed=!isFindUnequivelantState;
		}
		isFindUnequivelantState=false;
	}	

	public void orederedAdd(char[] sentence) {

	}

	public static DFA fromFile(String path) throws IOException {
		return fromFile(new File(path));
	}
	
	public static DFA fromFile(File file) throws IOException {
		final MinimalDFABuilder builder = new MinimalDFABuilder();
		Files.readLines(file, Charsets.UTF_8, new LineProcessor<Void>() {

			@Override
			public Void getResult() {
				return null;
			}

			@Override
			public boolean processLine(String line) throws IOException {
				builder.unorederedAdd(line.toCharArray());
				return true;
			}
		});
		return builder.build();
	}
	
	public static void main(String[] args) throws IOException {
		if (args.length < 1) {
			System.out.println("Usage: builder input");
			System.exit(-1);
		}
		DFA dfa = fromFile(args[0]);
		DfaXmlWriter writer = null;
		try {
			writer = new DfaXmlWriter(new OutputStreamWriter(System.out));
			writer.write(dfa);
		} finally {
			if (writer != null) {
				writer.close();
			}
		}
	}
}
