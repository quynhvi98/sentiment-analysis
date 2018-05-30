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
package vn.com.epi.gate.crfner.converter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Posted from May 26, 2018 4:31 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public class ExtractUnnamedEntity {

	private static final String url = "G:/ColVNNER/VietnameseNerCorpusAll(4).tsv";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// int i = 9;
		// int j = 2;
		// i = j + (i++);
		// System.out.println("i = " + i);
		// System.out.println("j = " + j);
		FileProcess input = new FileProcess(url);
		FileProcess output = new FileProcess("G:/ColVNNER/Unnamed.lst");
		try {
			output.save(extract(input.read()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done");

	}

	private static ArrayList<String> extract(ArrayList<String> content) {

		String productLable = "Product";
		String locationLable = "Location";
		String organizationLable = "Organization";
		String personLable = "Person";

		ArrayList<String> output = new ArrayList();
		for (int i = 0; i < content.size(); i++) {
			int startToken, endToken;
			if (content.get(i).endsWith(productLable)) {
				startToken = i;
				do {
					endToken = i++;
				} while (i < content.size()
						&& content.get(i).endsWith(productLable));

				StringBuilder connected = new StringBuilder();
				for (int j = startToken; j <= endToken; j++) {
					connected.append(content.get(j).split("\t")[0] + " ");
				}
				if (!(isDigit(connected.toString()) || isUpperCase(connected
						.toString()))) {
					if (!output.contains(connected.toString().trim())) {
						output.add(connected.toString().trim());
					}
				}

			}
			// else if (content.get(i).endsWith(locationLable)) {
			// startToken = i;
			// do {
			// endToken = i;
			// } while (i < content.size() &&
			// content.get(i++).endsWith(locationLable) );
			//
			// StringBuilder connected = new StringBuilder();
			// for(int j = startToken; j <= endToken; j++){
			// connected.append(content.get(j).split("\t")[0]);
			// }
			//
			// output.add(connected.toString().trim());
			//
			// } else if (content.get(i).endsWith(personLable)) {
			// startToken = i;
			// do {
			// endToken = i;
			// } while (i < content.size() &&
			// content.get(i++).endsWith(personLable) );
			//
			// StringBuilder connected = new StringBuilder();
			// for(int j = startToken; j <= endToken; j++){
			// connected.append(content.get(j).split("\t")[0]);
			// }
			//
			// output.add(connected.toString().trim());
			//
			// } else if (content.get(i).endsWith(organizationLable)) {
			// startToken = i;
			// do {
			// endToken = i;
			// } while (i < content.size() &&
			// content.get(i++).endsWith(organizationLable) );
			//
			// StringBuilder connected = new StringBuilder();
			// for(int j = startToken; j <= endToken; j++){
			// connected.append(content.get(j).split("\t")[0]);
			// }
			//
			// if(isDigit(connected.toString()) ||
			// isUpperCase(connected.toString())){
			// for(int j = startToken; j <= endToken; j++){
			// output.add(content.get(j));
			// }
			// }else{
			// for(int j = startToken; j <= endToken; j++){
			// output.add(content.get(j).split("\t")[0]+ "\tO");
			// }
			// }
			//
			// }else{
			// output.add(content.get(i));
			// }
		}
		return output;
	}

	private static boolean isUpperCase(String input) {
		boolean upperFound = false;
		for (char c : input.toCharArray()) {
			if (Character.isUpperCase(c)) {
				upperFound = true;
				break;
			}
		}
		return upperFound;
	}

	private static boolean isDigit(String input) {
		boolean digitFound = false;
		for (char c : input.toCharArray()) {
			if (Character.isDigit(c)) {
				digitFound = true;
				break;
			}
		}
		return digitFound;
	}

}
