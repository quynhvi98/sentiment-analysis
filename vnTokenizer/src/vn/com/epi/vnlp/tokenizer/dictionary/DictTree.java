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
package vn.com.epi.vnlp.tokenizer.dictionary;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import vn.com.epi.vnlp.dfa.DFA;
import vn.com.epi.vnlp.dfa.io.DfaSaxReader;

import gnu.trove.map.TCharObjectMap;
import gnu.trove.map.hash.TCharObjectHashMap;

/**
 * @author Chu Thanh Quang
 *
 */
public class DictTree {
	
	public static final int IS_WORD = 0;
	public static final int IS_NOT_WORD = -1;
	public static final int IS_WORD_PART = 1;
	private static final char[] vowels_a = {'a','à','á','ả','ạ','ã','â','ầ','ấ','ẩ','ậ','ẫ','ă','ằ','ắ','ẳ','ặ','ẵ'};
	private static final char[] vowels_A = {'A','À','Á','Ả','Ạ','Ã','Â','Ầ','Ấ','Ẩ','Ậ','Ẫ','Ă','Ằ','Ắ','Ẳ','Ặ','Ẵ'};
	private static final char[] vowels_e = {'e','è','é','ẻ','ẹ','ẽ','ê','ề','ế','ể','ệ','ễ'};
	private static final char[] vowels_E = {'E','È','É','Ẻ','Ẹ','Ẽ','Ê','Ề','Ế','Ể','Ệ','Ễ'};
	private static final char[] vowels_i = {'i','ì','í','ỉ','ị','ĩ'};
	private static final char[] vowels_I = {'I','Ì','Í','Ỉ','Ị','Ĩ'};
	private static final char[] vowels_o = {'o','ò','ó','ỏ','ọ','õ','ô','ồ','ố','ổ','ộ','ỗ','ơ','ờ','ớ','ở','ợ','ỡ'};
	private static final char[] vowels_O = {'O','Ò','Ó','Ỏ','Ọ','Õ','Ô','Ồ','Ố','Ổ','Ộ','Ỗ','Ơ','Ờ','Ớ','Ở','Ợ','Ỡ'};
	private static final char[] vowels_u = {'u','ù','ú','ủ','ụ','ũ','ư','ừ','ứ','ử','ự','ữ'};
	private static final char[] vowels_U = {'U','Ù','Ú','Ủ','Ụ','Ũ','Ư','Ừ','Ứ','Ử','Ự','Ữ'};
	private static final char[] vowels_y = {'y','ỳ','ý','ỷ','ỵ','ỹ'};
	private static final char[] vowels_Y = {'Y','Ỳ','Ý','Ỷ','Ỵ','Ỹ'};
	private static final char[] vowels_d = {'d','đ'};
	private static final char[] vowels_D = {'D','Đ'};
	
	private static TCharObjectMap<char[]> vowels = new  TCharObjectHashMap<char[]>();
	
	static{
		vowels.put('a', vowels_a);
		vowels.put('A', vowels_A);
		vowels.put('e', vowels_e);
		vowels.put('E', vowels_E);
		vowels.put('i', vowels_i);
		vowels.put('I', vowels_I);
		vowels.put('o', vowels_o);
		vowels.put('O', vowels_O);
		vowels.put('u', vowels_u);
		vowels.put('U', vowels_U);
		vowels.put('y', vowels_y);
		vowels.put('Y', vowels_Y);
		vowels.put('d', vowels_d);
		vowels.put('D', vowels_D);
	}
	
	private class DictNode{
		
		private DictNode parent;
		
		private TCharObjectMap<DictNode> children;
		
		private boolean isLeaf;
		
		public DictNode(){
			isLeaf = false;
		}
		
		public DictNode(DictNode myParent){
			this();
			parent = myParent;
		}
		
		public boolean isRoot(){
			return parent == null;
		}
		
		public boolean isLeaf(){
			return isLeaf;
		}
		
		public void addString(String string){
			if (string.isEmpty()){
				isLeaf = true;
				return;
			}
			char ch = string.charAt(0);
			if (children == null)
				children = new TCharObjectHashMap<DictNode>();
			DictNode node = children.get(ch);
			if (node == null){
				node = new DictNode(this);
				children.put(ch, node);
			}
			node.addString(string.substring(1, string.length()));
		}
		
		public void delete(){
			if (children.size() == 1){
				children.clear();
				children = null;
				if (!isRoot()) 
					parent.delete();
			}
		}
		
		public void removeString(String string){
			if (string.isEmpty()){
				isLeaf = false;
				if (children == null && !isRoot()) 
					parent.delete();
				return;
			}
			
			char ch = string.charAt(0);
			if (children == null)
				return;
			
			DictNode node = children.get(ch);
			if (node == null)
				return;
			
			node.removeString(string.substring(1, string.length()));
		}
		
		public int accept(String word){
			if (word.isEmpty()){
				if (isLeaf)
					return IS_WORD;
				else
					return IS_WORD_PART;
			}
			else {
				if (children == null)
					return IS_NOT_WORD;
				char ch = word.charAt(0);
				DictNode node = children.get(ch);
				if (node == null) 
					return IS_NOT_WORD;
				else
					return node.accept(word.substring(1, word.length()));
			}
		}
		
		public void getWords(List<String> result, String string, String prefix, boolean lengthfit){
			//System.out.println("String=" + string + "; prefix=" + prefix + "; leaf=" + isLeaf + "; NullChild=" + (children==null));
			if (children == null){
				if (prefix.isEmpty())
					result.add(string.toString());
				return;
			}		
			if (prefix.isEmpty()){
				if (lengthfit){
					if (isLeaf)
						result.add(string.toString());
				}else
					for (char ch:children.keys()){
						DictNode  node = children.get(ch);
						node.getWords(result, string + ch, prefix, lengthfit);
					}
				return;
			}
			char ch = prefix.charAt(0);
			if (vowels.containsKey(ch)){
				char[] vVowels = vowels.get(ch);
				for (char c:vVowels){
					DictNode  node = children.get(c);
					if (node != null){
						
						node.getWords(result, string + c, prefix.substring(1, prefix.length()), lengthfit);
						
					}
				}
			}
			else{
				DictNode node = children.get(ch);
				if (node != null){
					
					node.getWords(result, string+ ch, prefix.substring(1, prefix.length()), lengthfit);
					
				}
			}
		}
		
	}
	
	private DictNode root;
	
	public DictTree(){
		root = new DictNode();
	}
	
	public DictTree(DFA dfa, boolean splitIntoSyllable){
		this();
		Map<String, Integer> terms = dfa.getTermList();
		for (String term : terms.keySet()){
			if (splitIntoSyllable){
				for (String syllable: term.split(" "))
					addWord(syllable);
			}else{
				addWord(term);
			}
		}
	}
	
	public void addWord(String word){
		root.addString(word);
	}
	
	public void removeWord(String word){
		root.removeString(word);
	}
	
	public List<String> getWords(String prefix, boolean lengthfit){
		List<String> result = new ArrayList<String>();
		root.getWords(result, new String(""), prefix, lengthfit);
		return result;
	}
	
	public int accept(String word){
		return root.accept(word);
	}
	
	public static void main(final String[] args) throws IOException{
		
		DFA dfa = new DfaSaxReader(new FileReader(
				"resources/automata/dfaLexicon.xml")).read();
		DictTree tree = new DictTree(dfa, true);
		
		/*tree.addWord("bán hàng");
		tree.addWord("bấn loạn");
		tree.addWord("bánh bao");
		tree.addWord("bia hơi");
		tree.addWord("bia tươi");
		tree.addWord("bìa sách");
		tree.addWord("cưa gỗ");
		tree.addWord("cửa quấn");*/
		tree.addWord("đi");
		tree.addWord("díc");
		tree.addWord("dịc");
		tree.addWord("đía");
		tree.addWord("địa");
		tree.addWord("dích");
		tree.addWord("dịch");
		tree.addWord("đích");
		tree.addWord("địch");
		
		List<String> words = tree.getWords("di", true);
		for (String word:words){
			System.out.println(word);
		}
		
		System.out.println(words.size());
		
	}

}
