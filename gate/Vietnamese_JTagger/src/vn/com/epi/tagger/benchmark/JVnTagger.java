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

import gate.creole.ResourceInstantiationException;
import jvntagger.MaxentTagger;

/**
 * @author Nguyen Vi Duong (vi.duong.bk@gmail.com)
 *
 */
public class JVnTagger {

	private MaxentTagger tagger;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}


	public void init(String url) throws ResourceInstantiationException {
		try {
			tagger = new MaxentTagger(url);
		} catch (Exception e) {
			throw new ResourceInstantiationException(e);
		}
	}

	public String tag(String sentence) {
		return tagger.tagging(sentence);
	}
	
	
}
