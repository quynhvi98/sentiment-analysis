package vn.com.epi.treebank.converter;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Closeables;
import com.google.common.io.Files;

public class VietTreebankTokenizerTest {
	
	
	@Test
	public void test() throws IOException{
	    Reader r = null;
	    VietTreeReader vt = null;
		try {
            r = Files.newReader(new File("data/source/109898.van.prd.out"), Charsets.UTF_8);
            vt = new VietTreeReader(r);
			vt.readTree();
		} finally {
		    Closeables.closeQuietly(vt);
		    Closeables.closeQuietly(r);
		}
	}
	
}
