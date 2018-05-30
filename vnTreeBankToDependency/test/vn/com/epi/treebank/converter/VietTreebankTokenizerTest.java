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

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.junit.Test;

import com.google.common.base.Charsets;
import com.google.common.io.Closeables;
import com.google.common.io.Files;

/**
 * @author Nguyen Vi Duong (vi.duong.bk@gmail.com)
 * 
 */
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
