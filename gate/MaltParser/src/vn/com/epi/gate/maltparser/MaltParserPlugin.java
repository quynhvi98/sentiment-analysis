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
package vn.com.epi.gate.maltparser;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Resource;
import gate.creole.AbstractLanguageAnalyser;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.metadata.CreoleResource;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.maltparser.core.exception.MaltChainedException;

/**
 * @author Nguyen Vi Duong (vi.duong.bk@gmail.com)
 */
@CreoleResource(name = "maltparser PR", comment = "processing resource")
public class MaltParserPlugin extends AbstractLanguageAnalyser {

    private static final long serialVersionUID = 1L;

    private ParserService service = null;
    private URL dirOfMCO;
    private String nameOfMCO;

    public Resource init() throws ResourceInstantiationException {
        try {
            service = new ParserService();
            File mcoFile = new File(dirOfMCO.getPath().replace("%20", " "));
            service.initializeParserModel("-w\t"
                    + (new File(mcoFile.toURI()).getAbsolutePath()).replace(
                    "\\", "/") + "\t-c\t" + nameOfMCO + "\t-m\tparse");
            service.terminateParserModel();
        } catch (MaltChainedException e) {
            throw new ResourceInstantiationException(e);
        }
        return this;
    }

    public void execute() throws ExecutionException {
        AnnotationSet sentences = document.getAnnotations().get("Sentence");
        for (Annotation sentence : sentences) {
            List<Annotation> tokens = getTokens(sentence);
            int limit = tokens.size();
            String[] input = new String[limit];
            int i = 0;
            for (Annotation token : tokens) {
                input[i++] = convertToConll(i, token);
            }
            String[] output;
            try {
                i = 0;
                output = service.parseTokens(input);
                for (Annotation token : tokens) {
                    String[] col = output[i++].split("\t");
                    token.getFeatures().put("head", col[6]);
                    token.getFeatures().put("relation", col[7]);
                }
            } catch (MaltChainedException e) {
                e.printStackTrace();
            }
        }
    }

    private List<Annotation> getTokens(Annotation sent) {
        List<Annotation> tokens = new ArrayList<Annotation>(document
                .getAnnotations().get("Token", sent.getStartNode().getOffset(),
                        sent.getEndNode().getOffset()));
        Collections.sort(tokens, gate.Utils.OFFSET_COMPARATOR);
        return tokens;
    }

    private String convertToConll(int index, Annotation token) {
        StringBuilder stringConll = new StringBuilder();
        stringConll.append(index);
        stringConll.append("\t");
        stringConll.append(gate.Utils.stringFor(document, token));
        stringConll.append("\t_\t");
        stringConll.append(token.getFeatures().get("category"));
        stringConll.append("\t");
        stringConll.append(token.getFeatures().get("category"));
        stringConll.append("\t_");
        return stringConll.toString();
    }

    /**
     * @return the nameOfMCO
     */
    public String getNameOfMCO() {
        return nameOfMCO;
    }

    /**
     * @param nameOfMCO the nameOfMCO to set
     */
    public void setNameOfMCO(String nameOfMCO) {
        this.nameOfMCO = nameOfMCO;
    }

    /**
     * @return the dirOfMCO
     */
    public URL getDirOfMCO() {
        return dirOfMCO;
    }

    /**
     * @param dirOfMCO the dirOfMCO to set
     */
    public void setDirOfMCO(URL dirOfMCO) {
        this.dirOfMCO = dirOfMCO;
    }

}
