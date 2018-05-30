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
package vn.com.epi.gate.maltparser.test;

import gate.CorpusController;
import gate.Factory;
import gate.Gate;
import gate.creole.ExecutionException;
import gate.util.GateException;
import gate.util.persistence.PersistenceManager;

import java.io.File;
import java.io.IOException;

import vn.com.epi.gate.maltparser.MaltParserPlugin;

/**
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public class MaltParserPluginTest {

    private static CorpusController application;

    public static void main(String[] args) throws IOException, GateException {
        Gate.init();
        application = (CorpusController) PersistenceManager
                .loadObjectFromFile(new File("dist/gate/parsing.gapp"));

//		MaltParserPlugin maltParserPlugin = new MaltParserPlugin();
//		File file = new File("E:/test1.xml");
//		maltParserPlugin.setDocument(Factory.newDocument(file.toURI().toURL(), "utf-8"));
//		try {
//			maltParserPlugin.execute();
//		} catch (ExecutionException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

    }
}
