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
package vn.com.epi.treebank.converter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import edu.stanford.parser.nlp.trees.Tree;

/**
 * @author Nguyen Thi Dam (nguyendam2211@gmail.com)
 * 
 */
public class VietHeadFinderCheck extends VietHeadFinder {

	private static ArrayList<String> out = new ArrayList<String>();
	private static final long serialVersionUID = 1L;

	/**
	 * @param in
	 */
	public VietHeadFinderCheck(boolean in) {
		super(in);
		rulechanges();
	}
	
	private void rulechanges() {
//		nonTerminalInfo.put("S", new String[][]{{"left", "VP", "NP"}, {"right", "VP", "NP", "S"}});
		nonTerminalInfo.put("NP", new String[][]{{"left", "N", "NP-TMP", "M", "P", "PP", "SBAR"},{"leftdis", "N","Np", "Nc", "Nu", "Ny", "NP", "Nb"}});
		nonTerminalInfo.put("NP-ADV", new String[][] {{"left", "NP", "P"}, {"leftdis", "NP"}});
		nonTerminalInfo.put("NP-CND", new String[][] {{"leftdis", "NP"}});
		nonTerminalInfo.put("NP-DOB", new String[][] { {"left", "NP-TMP", "P", "VP-PRP"}, {"leftdis", "Ny", "Nc", "Np", "N", "NP"}});
		nonTerminalInfo.put("NP-IOB", new String[][] {{"leftdis", "NP"}});
		nonTerminalInfo.put("NP-LOC", new String[][] {{"leftdis", "Np","N", "NP"}});
		nonTerminalInfo.put("NP-MDP", new String[][] {{"leftdis", "P"}});
		nonTerminalInfo.put("NP-PRD", new String[][] {{"leftdis", "NP"}});
		nonTerminalInfo.put("NP-PRP", new String[][] {{"left", "P"}});
		nonTerminalInfo.put("NP-MNR", new String[][] {{"leftdis", "NP"}});
		nonTerminalInfo.put("NP-SUB", new String[][] { {"left", "N", "P", "PP", "VP"}, {"leftdis", "N", "Np", "NP"}});
		nonTerminalInfo.put("NP-TMP", new String[][] {{"leftdis", "N", "NP"}});
		nonTerminalInfo.put("NP-TPC", new String[][] {{"leftdis", "NP"}});
		nonTerminalInfo.put("NP-TH", new String[][] {{"leftdis", "NP"}});
		
		// sau nay bo di
		nonTerminalInfo.put("VP", new String[][] {{"left", "V", "VP", "VP-LOC", "NP", "NP-DOB","AP", "PP", "SBAR", "SBAR-DOB", "R"},{"leftdis", "V", "VP"}});
		nonTerminalInfo.put("VP-CND", new String[][] {{"leftdis", "VP"}});
		nonTerminalInfo.put("VP-DOB", new String[][] {{"leftdis", "VP"}});
		nonTerminalInfo.put("VP-DOB", new String[][] {{"leftdis", "VP"}});
		nonTerminalInfo.put("VP-IOB", new String[][] {{"leftdis", "VP"}});
		nonTerminalInfo.put("VP-LOC", new String[][] {{"leftdis", "VP"}});
		nonTerminalInfo.put("VP-PRP", new String[][] { {"left", "V", "VP", "PP"},{"leftdis", "VP"}});
		nonTerminalInfo.put("VP-SUB", new String[][] {{"leftdis", "V", "VP"}});
		nonTerminalInfo.put("VP-TPC", new String[][] {{"leftdis", "VP"}});
		nonTerminalInfo.put("VP-TMP", new String[][] {{"leftdis", "VP"}});
		nonTerminalInfo.put("VP-VOC", new String[][] {{"leftdis", "VP"}});
		nonTerminalInfo.put("VP-ADV", new String[][] {{"leftdis", "VP"}});
		nonTerminalInfo.put("VP-MNR", new String[][] {{"leftdis", "V", "VP"}});
		nonTerminalInfo.put("VP-TH", new String[][] {{"leftdis", "VP"}});
		
		nonTerminalInfo.put("AP", new String[][] {{"leftdis", "A", "AP"}});
		nonTerminalInfo.put("AP-ADV", new String[][] {{"leftdis", "AP"}});
		nonTerminalInfo.put("AP-DOB", new String[][] {{"leftdis", "AP"}});
		nonTerminalInfo.put("AP-MMR", new String[][] {{"leftdis", "A", "AP"}});
		nonTerminalInfo.put("AP-IOB", new String[][] {{"leftdis", "AP"}});
		nonTerminalInfo.put("AP-PRD", new String[][] {{"leftdis", "A", "AP"}});
		nonTerminalInfo.put("AP-PRP", new String[][] {{"leftdis", "AP"}});
		nonTerminalInfo.put("AP-SUB", new String[][] {{"leftdis", "AP"}});
		nonTerminalInfo.put("AP-TPC", new String[][] {{"leftdis", "A"}});
		nonTerminalInfo.put("AP-CND", new String[][] {{"leftdis", "AP"}});
		nonTerminalInfo.put("AP-TPC", new String[][] {{"leftdis", "AP"}});
		nonTerminalInfo.put("AP-RPD", new String[][] {{"leftdis", "AP"}});
		nonTerminalInfo.put("AP-CND", new String[][] {{"leftdis", "AP"}});
		nonTerminalInfo.put("AP-MNR", new String[][] {{"leftdis", "A", "AP"}});
		
		
		nonTerminalInfo.put("AP-TMP", new String[][] {{"leftdis", "A"}});
		nonTerminalInfo.put("RP", new String[][] {{ "leftdis", "R", "RP"}});
		nonTerminalInfo.put("RP-DOB", new String[][] {{ "leftdis", "R", "RP"}});
		nonTerminalInfo.put("RP-MDP", new String[][] {{ "leftdis", "R", "RP"}});
		nonTerminalInfo.put("QP", new String[][]{{ "leftdis", "M", "QP"}});
		nonTerminalInfo.put("QP-TMP", new String[][]{{ "leftdis", "M", "QP"}});
		
		nonTerminalInfo.put("PP", new String[][]{{"left", "E", "PP", "PP-PRP", "PP-MNR", "PP-LOC", "C"},{ "leftdis", "E", "PP"}});
		nonTerminalInfo.put("PP-TH", new String[][]{{ "leftdis", "PP"}});
		nonTerminalInfo.put("PP-TMP", new String[][]{{ "leftdis", "E", "PP"}});
		nonTerminalInfo.put("PP-SUB", new String[][]{{ "leftdis", "PP"}});
		nonTerminalInfo.put("PP-PRP", new String[][]{{ "leftdis", "E", "PP"}});
		nonTerminalInfo.put("PP-PRD", new String[][]{{ "leftdis", "PP"}});
		nonTerminalInfo.put("PP-MNR", new String[][]{{ "leftdis", "E", "PP"}});
		nonTerminalInfo.put("PP-LOC", new String[][]{{ "leftdis", "E", "PP"}});
		nonTerminalInfo.put("PP-IOB", new String[][]{{ "leftdis", "E", "PP"}});
		nonTerminalInfo.put("PP-DOB", new String[][]{{ "leftdis", "E", "PP"}});
		nonTerminalInfo.put("PP-DIR", new String[][]{{ "left", "PP", "PP-LOC"}, { "leftdis", "E", "PP"}});
		nonTerminalInfo.put("PP-TPC", new String[][]{{ "leftdis", "PP"}});
		
		// XP, PP sau se bo di
		nonTerminalInfo.put("S", new String[][]{{ "left", "VP", "NP-SUB", "VP-SUB","PP-SUB", "AP-SUB", "VP-MNR","VP-DIR", "AP", "VP-TPC", "VP-TMP", "S-SUB", "S","S-PV","S-TH", "S-TC", "S-SPL","SQ", "S-PRD", "NP", "NP-PRD", "NP-TPC", "NP-TMP", "SBAR", "SBAR-TH", "AP-PRD", "AP-ADV", "VP-TH", "XP", "PP", "I", "QP-TMP", "PP-LOC", "PP-TPC", "PP-TMP", "QP-SUB"}});
		
//		nonTerminalInfo.put("S", new String[][]{{ "left", "VP", "NP-SUB", "VP-SUB", "S"}});
		
		nonTerminalInfo.put("S-ADV", new String[][]{{ "left", "VP"}});
		nonTerminalInfo.put("S-CMD", new String[][]{{ "left", "VP", "NP-SUB", "S", "AP"}});
		
		nonTerminalInfo.put("S-CND", new String[][]{{ "left", "VP", "NP-SUB", "AP", "S"}});
		
		nonTerminalInfo.put("S-EXC", new String[][]{{ "left", "VP", "NP-SUB", "NP", "NP-VOC", "S", "AP","R", "X"}});
		
//		nonTerminalInfo.put("S-EXC", new String[][]{{ "left", "VP", "NP-SUB", "R"}});
		
		nonTerminalInfo.put("S-IOB", new String[][]{{ "left", "VP"}});
		nonTerminalInfo.put("S-PRP", new String[][]{{ "left", "VP", "NP-SUB"}});
//		nonTerminalInfo.put("S-PRD", new String[][]{{ "left", "VP", "VP-SUB", "NP-SUB", "S", "S-TC"}});
		nonTerminalInfo.put("S-PRD", new String[][]{{ "left", "VP", "VP-SUB", "NP-SUB", "S", "S-TC"}});
		nonTerminalInfo.put("S-PV", new String[][]{{ "left", "VP"}});
		nonTerminalInfo.put("S-CNC", new String[][]{{ "left", "VP", "S"}});
		nonTerminalInfo.put("S-SPL", new String[][]{{ "left", "VP", "NP", "NP-SUB", "VP-PRP", "NP-MNR", "NP-VOC", "NP-TMP", "NP-TPC", "VP-MDP", "QP", "AP", "AP-MNR", "PP", "PP-PRP", "PP-TMP", "PP-LOC", "RP", "S", "SBAR-PRP", "C", "I", "XP"}});
		nonTerminalInfo.put("S-SUB", new String[][]{{ "left", "VP", "NP-SUB", "S"}});
		nonTerminalInfo.put("S-TMP", new String[][]{{ "left", "VP"}});
//		nonTerminalInfo.put("S-TPC", new String[][]{{ "left", "VP", "NP-SUB", "S-TC"}});
		
		nonTerminalInfo.put("S-TPC", new String[][]{{ "left", "VP", "NP-SUB", "S-TC"}});
		
		nonTerminalInfo.put("S-TTL", new String[][]{{ "left", "VP", "NP-SUB", "NP", "PP", "VP-TH", "NP-TMP", "PP-ADV", "PP-TMP", "AP", "PP-LOC", "AP-MNR", "AP-ADV", "AP-TMP", "NP-LOC", "S"}});
		nonTerminalInfo.put("S-TH", new String[][]{{ "left", "VP", "NP-SUB", "VP-TPC", "VP-TH", "S", "S-TC"}});
		nonTerminalInfo.put("S-EXT", new String[][]{{ "left", "VP", "NP-SUB", "VP-TPC"}});
		
//		nonTerminalInfo.put("S-EXT", new String[][]{{ "left", "VP", "NP-SUB"}});
		nonTerminalInfo.put("S-PV", new String[][]{{ "left", "VP", "NP-SUB", "S"}});
		nonTerminalInfo.put("S-TC", new String[][]{{ "left", "VP", "NP-SUB", "VP-TH", "AP-PRD", "S", "NP-TH", "NP-TPC", "VP-TPC", "AP-TH", "S-TH", "SBAR", "SBAR-TH"}});
		nonTerminalInfo.put("S-SE", new String[][]{{ "left", "I"}});
		
		nonTerminalInfo.put("S-SQ", new String[][]{{ "left", "VP", "NP-SUB"}});
		// WHPP-PRD xoa
		nonTerminalInfo.put("SQ", new String[][]{{ "left", "VP", "NP-SUB", "VP-SUB", "NP",  "S", "WHAP", "NP-TMP", "S-TC", "WHRP", "VP-TH", "WHPP","WHPP-PRD", "AP" }});
		nonTerminalInfo.put("SQ-TTL", new String[][]{{ "left", "VP", "NP-SUB", "NP", "AP", "RP", "PP", "S"}});
		nonTerminalInfo.put("SQ-SPL", new String[][]{{ "left", "VP", "NP", "AP", "WHRP"}});
		nonTerminalInfo.put("SQ-EXT", new String[][]{{ "left", "VP"}});
		nonTerminalInfo.put("SQ-TC", new String[][]{{ "left", "VP", "VP-TH"}});
		
		nonTerminalInfo.put("SBAR-PRP", new String[][]{ {"left", "VP", "NP-SUB", "S"},{ "leftdis", "C", "E"}});
		nonTerminalInfo.put("SBAR", new String[][]{{"left", "VP", "NP-SUB", "S", "SQ"}, { "leftdis", "C", "E"}});
		nonTerminalInfo.put("SBAR-CNC", new String[][]{{ "leftdis", "C"}});
		nonTerminalInfo.put("SBAR-CND", new String[][]{{ "leftdis", "C"}});
		nonTerminalInfo.put("SBAR-DOB", new String[][]{{ "left", "C", "VP", "S", "SQ"}});
		nonTerminalInfo.put("SBAR-MNR", new String[][]{{ "leftdis", "C"}});
		nonTerminalInfo.put("SBAR-TMP", new String[][]{{ "leftdis", "VP"}});
		nonTerminalInfo.put("SBAR-IOB", new String[][]{{ "left", "S"}});
		nonTerminalInfo.put("SBAR-PRD", new String[][]{{ "left", "VP", "S"}});
		
		
		// ucp ngang hang, lam tam thoi, sau nay xoa di
		nonTerminalInfo.put("UCP", new String[][]{{ "leftdis", "VP", "NP", "AP"}});
		nonTerminalInfo.put("UCP-SUB", new String[][]{{ "leftdis", "VP", "AP"}});
		nonTerminalInfo.put("UCP-MNR", new String[][]{{ "leftdis", "VP", "NP", "AP"}});
		nonTerminalInfo.put("UCP-PRD", new String[][]{{ "leftdis", "VP", "NP", "AP"}});
		
		nonTerminalInfo.put("WHPP", new String[][]{{ "left", "SBAR"}});
		nonTerminalInfo.put("WHRP", new String[][]{{ "left", "NP", "SBAR", "X"}});
		nonTerminalInfo.put("WHNP", new String[][]{{ "left", "NP", "P", "SBAR", "X"}});
		nonTerminalInfo.put("WHXP", new String[][]{{ "left", "SBAR"}});
		
		nonTerminalInfo.put("MDP", new String[][]{{ "leftdis", "T", "I"}});
		nonTerminalInfo.put("MDP-VOC", new String[][]{{ "leftdis", "T", "I"}});
		nonTerminalInfo.put("XP-MDP", new String[][]{{ "left", "X", "XP"}});
		nonTerminalInfo.put("XP", new String[][]{{ "left", "X", "XP"}});
		nonTerminalInfo.put("XP-SUB", new String[][]{{ "left", "X", "XP"}});
	}
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * vn.com.epi.treebank.converter.VietHeadFinder#determineHead(edu.stanford
	 * .nlp.trees.Tree, edu.stanford.nlp.trees.Tree)
	 */
	@Override
	public Tree determineHead(Tree t, Tree parent) {
		// TODO Auto-generated method stub
		Tree head = super.determineHead(t, parent);
		if (head == null) {
				String localTree = t.localTree().pennString();
				try {
					if (out.contains(localTree)) {

					} else {
						BufferedWriter bufferedWriter = new BufferedWriter(
								new FileWriter(new File(localTree.split("\r\n")[0].substring(1).toLowerCase()+".txt"), true));
						out.add(localTree);
						System.err.println(t.pennString());
						bufferedWriter.write(localTree.replace("\r\n", " ")
								+ "\n " + t.pennString() + "\n\n\n");
						bufferedWriter.flush();
						bufferedWriter.close();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// TODO Auto-generated catch block
		}
//		return head == null ? t.children()[0] : head;
		return head;
//		return super.determineHead(t, parent);
	}
	
	
	/* (non-Javadoc)
	 * @see edu.stanford.nlp.trees.AbstractCollinsHeadFinder#traverseLocate(edu.stanford.nlp.trees.Tree[], java.lang.String[], boolean)
	 */
	@Override
	protected Tree traverseLocate(Tree[] daughterTrees, String[] how,
			boolean lastResort) {
		// TODO Auto-generated method stub
		lastResort = false;
		return super.traverseLocate(daughterTrees, how, lastResort);
	}
}
