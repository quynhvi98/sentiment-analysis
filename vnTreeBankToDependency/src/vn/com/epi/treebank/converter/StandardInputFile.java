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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;

/**
 * @author Nguyen Vi Duong (vi.duong.bk@gmail.com)
 * 
 */
public class StandardInputFile {

	private static String URL_OF_TREEBANK;
	private static String URL_OUTPUT;

	public static void main(String[] args) throws IOException {

		if (args.length == 4) {
			if (args[0].equals("-i") && args[2].endsWith("-o")) {
				URL_OF_TREEBANK = args[1];
				URL_OUTPUT = args[3];

				File[] files = new File(URL_OF_TREEBANK).listFiles();

				int limit = files.length;
				for (int i = 0; i < limit; i++) {
					if (files[i].toString().endsWith(".prd")) {
						Writer writer = new BufferedWriter(
								new OutputStreamWriter(new FileOutputStream(
										URL_OUTPUT + files[i].getName()
												+ ".out"), "UTF-8"));
						BufferedReader reader = new BufferedReader(
								new InputStreamReader(new FileInputStream(
										files[i]), "UTF-8"));
						try {
							String text;
							while ((text = reader.readLine()) != null) {
								String output1 = text
										.replace("*0*-1", "*0*")
										.replace("*T*-1", "*T*")
										.replace("*E*-1", "*E*")
										.replace("*E*", "*E")
										.replace("*E", "(-NONE- *E)")
										.replace("*T*", "*T")
										.replace("*T", "(-NONE- *T)")
										.replace("*0*", "*0")
										.replace("*0", "(-NONE- *0)");
								String output2 = output1
										.replace("(S-Q", "(SQ")
										.replace("(S1", "(S-1")
										.replace("(S2", "(S-2")
										.replace("(S3", "(S-3")
										.replace("(S--", "(S-");
								String output3 = output2.replace("(N- ",
										"(N-");
								writer.append(output3 + "\n");
							}
						} catch (IOException e) {
							e.printStackTrace();
						} finally {
    						writer.close();
    						reader.close();
						}
					}
				}
			} else {
				System.out
						.println("Option <-i> : url of treebank, <-o> : output. \nExample: -i D:/data/source -o D:/data/");
			}
		} else {
			System.out
					.println("Please insert Url of treebank! \nOption <-i> : url of treebank, <-o> : output. \nExample: -i D:/data/source -o D:/data/");
		}
	}
}
