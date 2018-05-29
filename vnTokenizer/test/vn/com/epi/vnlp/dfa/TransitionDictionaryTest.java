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

import java.util.Random;

import org.junit.Test;

import vn.com.epi.vnlp.dfa.MultiRangeTranslationMap;
import vn.com.epi.vnlp.dfa.TransitionMap;

public class TransitionDictionaryTest {

	@Test
	public void randomMainChars() {
		TransitionMap map = new MultiRangeTranslationMap(0);
		Random r = new Random();

		long initStart = System.nanoTime();
		for (char ch = 'a'; ch <= 'z'; ch++) {
			map.setAt(ch, r.nextInt());
		}
		long initStop = System.nanoTime();
		System.out.println("Init time: " + (initStop-initStart) * 1.0e-9);
		
		long lookupStart = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			for (char ch = 'a'; ch <= 'z'; ch++) {
				map.getAt(ch);
			}
		}
		long lookupStop = System.nanoTime();
		System.out.println("Lookup time: " + (lookupStop-lookupStart) * 1.0e-9);
	}

	@Test
	public void testAllChars() {
		TransitionMap map = new MultiRangeTranslationMap(0);
		Random r = new Random();
		
		long initStart = System.nanoTime();
		for (int i = 500; i < 600; i++) {
			map.setAt((char)i, r.nextInt());
		}
		long initStop = System.nanoTime();
		System.out.println("Init time: " + (initStop-initStart) * 1.0e-9);
		
		long lookupStart = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			map.getAt((char)i);
		}
		long lookupStop = System.nanoTime();
		System.out.println("Lookup time: " + (lookupStop-lookupStart) * 1.0e-9);
	}
}
