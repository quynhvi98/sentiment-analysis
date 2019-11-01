package vn.com.epi.vnlp.dfa;

import java.io.*;
import java.util.ArrayList;

import com.sun.deploy.cache.Cache;
import org.junit.Assert;
import org.junit.Test;

import sun.security.pkcs11.wrapper.CK_SSL3_KEY_MAT_OUT;
import vn.com.epi.vnlp.dfa.DFA;

public class MinimalDFABuilderTest {

	@Test
	public void test() throws Exception {
		ArrayList<String> termList = new ArrayList<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new InputStreamReader(
					new DataInputStream(new FileInputStream("terms.lst"))));
			DFA dfa = new DFA();
			MinimalDFABuilder dfaBuilder = new MinimalDFABuilder();
			String strLine;
			int maxCount = 150000;
			long freeme = 0;
			int count = 0;
			freeme = Runtime.getRuntime().freeMemory();
			System.out.println(count + ":Max memory: " + freeme / 1024 / 1024 + "M");
			// Read File Line By Line
			while ((strLine = br.readLine()) != null && count < maxCount) {
				count++;
				if (count == maxCount)
					System.out.println(count + ":Max memory: " + freeme / 1024 / 1024 + "M");
				if (strLine.trim().equals(""))
					continue;
				dfaBuilder.unorederedAdd(stringToCharacter(strLine.trim()));
				termList.add(strLine.trim());
				freeme = Runtime.getRuntime().freeMemory();
				System.out.println(count + ":Max memory: " + freeme / 1024 / 1024 + "M");
			}
			dfa = dfaBuilder.build();
			dfaBuilder = null;
			System.out.print(dfa.getStateCount());
			Runtime.getRuntime().gc();
			for (int i = 0; i < termList.size(); i++) {
				Assert.assertTrue(dfa.accept(termList.get(i)));
				// if(!dfa.accept(termList.get(i))){
				// System.out.println(i);
				// //Assert.assertTrue(dfa.accept(termList.get(i)));
				// System.out.println(termList.get(i));
				// }
			}
			Assert.assertFalse(dfa.accept("xyz"));
		} catch (FileNotFoundException ex) {
			System.out.println("Please copy terms.lst into root folder");
			Assert.fail();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private char[] stringToCharacter(String s) {
		char[] chars = new char[s.length()];
		for (int i = 0; i < chars.length; i++) {
			chars[i] = s.charAt(i);
		}
		return chars;
	}
}
