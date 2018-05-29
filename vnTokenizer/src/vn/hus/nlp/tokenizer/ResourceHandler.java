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
/* -- vnTokenizer 2.0 --
 *
 * Copyright information:
 *
 * LE Hong Phuong, NGUYEN Thi Minh Huyen,
 * Faculty of Mathematics Mechanics and Informatics, 
 * Hanoi University of Sciences, Vietnam.
 *
 * Copyright (c) 2003
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms are permitted
 * provided that the above copyright notice and this paragraph are
 * duplicated in all such forms and that any documentation,
 * advertising materials, and other materials related to such
 * distribution and use acknowledge that the software was developed
 * by the author.  The name of the author may not be used to
 * endorse or promote products derived from this software without
 * specific prior written permission.
 * 
 * 
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND WITHOUT ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, WITHOUT LIMITATION, THE IMPLIED
 * WARRANTIES OF MERCHANTIBILITY AND FITNESS FOR A PARTICULAR PURPOSE.
 * 
 * 
 * Last update : 04/2005
 * 
 */

package vn.hus.nlp.tokenizer;

import java.util.ResourceBundle;

/**
 * 
 * This class represents a resource handler of entire module. It is used to
 * facilitate manipulations of a ressource bundler.
 * 
 */
public final class ResourceHandler {

	/**
	 * Get a resource value
	 * 
	 * @param key
	 *            a key of resource
	 * @return value of resource
	 */
	public static String get(String key) {
		return resource.getString(key);
	}

	/**
	 * The ressource bundle of the package
	 */
	static final ResourceBundle resource = ResourceBundle.getBundle("tokenizer");

}
