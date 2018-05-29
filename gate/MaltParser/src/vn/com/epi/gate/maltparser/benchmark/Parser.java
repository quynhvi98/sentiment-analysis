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
package vn.com.epi.gate.maltparser.benchmark;

import org.maltparser.MaltParserService;
import org.maltparser.core.exception.MaltChainedException;

/**
 * @author Nguyen Vi Duong (vi.duong.bk@gmail.com)
 */
public class Parser {

    private MaltParserService service = null;

    public Parser(String modelName) throws MaltChainedException {
        service = new MaltParserService();
        service.initializeParserModel("-c " + modelName
                + " -m parse -w . -lfi parser.log");
        service.terminateParserModel();
    }

    public String[] parser(String[] tokens) throws MaltChainedException {
        String[] outputTokens = null;
        if (service != null) {
            outputTokens = service.parseTokens(tokens);
            return outputTokens;
        } else {
            return null;
        }
    }

}
