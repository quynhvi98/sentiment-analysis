package vn.com.epi.vnlp.dfa;

import java.util.Random;

import org.junit.Test;

public class AlphabetMappingTransitionMapTest {

	@Test
	public void testMainChars() {
		TransitionMap map = new AlphabetMappingTransitionMap(0);
		Random r = new Random();

		long initStart = System.nanoTime();
		for (char ch = 'a'; ch <= 'z'; ch++) {
			map.setAt(ch, r.nextInt());
		}
		long initStop = System.nanoTime();
		System.out.println("Init time: " + (initStop - initStart) * 1.0e-9);

		long lookupStart = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			for (char ch = 'a'; ch <= 'z'; ch++) {
				map.getAt(ch);
			}
		}
		long lookupStop = System.nanoTime();
		System.out.println("Lookup time: " + (lookupStop - lookupStart)
				* 1.0e-9);
	}

	@Test
	public void testAllChars() {
		TransitionMap map = new AlphabetMappingTransitionMap(0);
		Random r = new Random();

		long initStart = System.nanoTime();
		for (int i = 500; i < 600; i++) {
			map.setAt((char) i, r.nextInt());
		}
		long initStop = System.nanoTime();
		System.out.println("Init time: " + (initStop - initStart) * 1.0e-9);

		long lookupStart = System.nanoTime();
		for (int i = 0; i < 100000; i++) {
			map.getAt((char) i);
		}
		long lookupStop = System.nanoTime();
		System.out.println("Lookup time: " + (lookupStop - lookupStart)
				* 1.0e-9);
	}

}
