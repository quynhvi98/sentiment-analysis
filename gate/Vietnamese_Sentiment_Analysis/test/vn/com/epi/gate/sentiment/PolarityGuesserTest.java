package vn.com.epi.gate.sentiment;

/**
 * Posted from May 30, 2018 10:00 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
import java.io.IOException;

import org.junit.Assert;
import org.junit.Test;

public class PolarityGuesserTest {

	public PolarityGuesser polarityGuesser;

	@Test
	public void testReadDefinitionFile() throws IOException {
		polarityGuesser = new PolarityGuesser();
		polarityGuesser.loadModel("test-data/opinion.def");
		Assert.assertEquals(polarityGuesser.getSumOfCount(), (10 + 99)*3);
	}
	
	@Test
	public void testReadLstFile() throws IOException {
		polarityGuesser = new PolarityGuesser();
		polarityGuesser.loadModel("test-data/opinion.lst");
		Assert.assertEquals(polarityGuesser.getSumOfCount(), (10 + 99));
	}
}
