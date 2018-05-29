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
package vn.com.epi.tagger.benchmark;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Nguyen Vi Duong (vi.duong.bk@gmail.com)
 *
 */
public class DataProcess {

	public ArrayList<String> readData(String url) throws IOException {
		ArrayList<String> output = new ArrayList<String>();
		
		File folder = new File(url);
		if(folder.isDirectory()){
			File[] allFiles = folder.listFiles();
			for(int i = 0; i < allFiles.length; i++){
				String strLine;
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(allFiles[i]), "UTF-8"));
				while ((strLine = br.readLine()) != null) {
					output.add(strLine);
				}
				br.close();
			}
		}else{
			String strLine;
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(folder), "UTF-8"));
			while ((strLine = br.readLine()) != null) {
				output.add(strLine);
			}
			br.close();
		}
		return output;
	}

	/**
	 * 
	 * @param input sentence content postagger
	 * @return List tag
	 * return null if sentence error
	 */
	
	public List<String> getTagger(String input) {
		// split the tagged string using the word/tag delimiter
		List<String> tag = new ArrayList<String>();
		String[] wordAndTags = input.split(" ");
		int limit = wordAndTags.length;
		for (int i = 0; i < limit; i++) {
			if(wordAndTags[i].equals("///")){
				// tag "/"
				tag.add("/");
			}else{
				String[] splitWordAndTag = wordAndTags[i].split("/");
				// the case of date with / separator, for example 20/10/1980/N
				// the word is 20/10/1980, tag is lastest /
				if(splitWordAndTag.length < 2){
					// break error sentence
					return null;
				}
				tag.add(splitWordAndTag[splitWordAndTag.length - 1]);
			}

		}
		return tag;
	}
	
	public String getSentence(String input) {
		// split the tagged string using the word/tag delimiter
		StringBuffer output = new StringBuffer();
		String[] wordAndTags = input.split(" ");
		int limit = wordAndTags.length;
		for (int i = 0; i < limit; i++) {
			if(wordAndTags[i].equals("///")){
				// tag "/"
				output.append("/");
				output.append(" ");
			}else{
				String[] splitWordAndTag = wordAndTags[i].split("/");
				if(splitWordAndTag.length < 3 && splitWordAndTag.length > 0){
					output.append(splitWordAndTag[0]);
				}else{
					for (int j = 0; j < splitWordAndTag.length - 1 ; j++) {
						output.append(splitWordAndTag[j]);
						output.append("/");
					}
				}
				output.append(" ");	
			}

		}
		
		return output.toString();
	}
	
}
