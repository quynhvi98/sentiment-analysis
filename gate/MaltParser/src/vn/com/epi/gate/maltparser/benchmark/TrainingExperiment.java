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

/**
 * @author Nguyen Vi Duong (vi.duong.bk@gmail.com)
 */
public class TrainingExperiment {

    public boolean train(int mode, String trainingDataFile, String modelName) {
        try {
            if (mode == 0) {
                System.out.println("Trains the parser model "
                        + trainingDataFile + " and uses parsing mode 2planar");

                new MaltParserService(1).runExperiment("-c " + modelName
                        + " -i " + trainingDataFile
                        + " -m learn -ne true -nr true -a 2planar -pcov true -2pr true");
//				new MaltParserService(0).runExperiment("-c " + "VTB"
//						+ " -i " + trainingDataFile
//						+ " -m learn -ne true -nr true -a 2planar -pcov true -2pr true");
                System.out.println("Done");
            } else if (mode == 1) {
                System.out.println("Trains the parser model "
                        + trainingDataFile
                        + " and uses parsing mode Nivre Standard");

                new MaltParserService(1).runExperiment("-c " + modelName
                        + " -i " + trainingDataFile
                        + " -m learn -ne true -nr true -a nivrestandard");
                System.out.println("Done");
            } else if (mode == 2) {
                System.out.println("Trains the parser model "
                        + trainingDataFile
                        + " and uses parsing mode Covproj with head + path");

                new MaltParserService(2)
                        .runExperiment("-c "
                                + modelName
                                + " -i "
                                + trainingDataFile
                                + " -m learn -ne true -nr true -a covproj -pp head+path -pcr head");
                System.out.println("Done");
            } else if (mode == 3) {
                System.out.println("Trains the parser model "
                        + trainingDataFile
                        + " and uses parsing mode Covnonproj");

                new MaltParserService(3).runExperiment("-c " + modelName
                        + " -i " + trainingDataFile
                        + " -m learn -ne true -nr true -a covnonproj");
                System.out.println("Done");
            } else if (mode == 4) {
                System.out.println("Trains the parser model "
                        + trainingDataFile
                        + " and uses parsing mode Stack Projective");

                new MaltParserService(4)
                        .runExperiment("-c "
                                + modelName
                                + " -i "
                                + trainingDataFile
                                + " -m learn -ne true -nr true -a stackproj -pp head+path -pcr head");
                System.out.println("Done");
            } else if (mode == 5) {
                System.out.println("Trains the parser model "
                        + trainingDataFile
                        + " and uses parsing mode Stack Eager");

                new MaltParserService(5)
                        .runExperiment("-c "
                                + modelName
                                + " -i "
                                + trainingDataFile
                                + " -m learn -ne true -nr true -a stackeager -pp head+path -pcr head");
                System.out.println("Done");
            } else if (mode == 6) {
                System.out.println("Trains the parser model "
                        + trainingDataFile
                        + " and uses parsing mode Stack Lazy");

                new MaltParserService(6).runExperiment("-c " + modelName + " -i "
                        + trainingDataFile
                        + " -m learn -ne true -nr true -a stacklazy");
                System.out.println("Done");
            } else {
                System.out.println("Trains the parser model "
                        + trainingDataFile + " and uses parsing mode Nivre");
                new MaltParserService(7)
                        .runExperiment("-c "
                                + modelName
                                + " -i "
                                + trainingDataFile
                                + " -m learn -ne true -nr true -a stackproj -pp head+path -pcr head");
                System.out.println("Done");
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

}
