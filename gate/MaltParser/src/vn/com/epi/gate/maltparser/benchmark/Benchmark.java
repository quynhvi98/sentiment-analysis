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

import java.util.ArrayList;

import org.maltparser.core.exception.MaltChainedException;

/**
 * @author Nguyen Vi Duong (vi.duong.bk@gmail.com)
 */
public class Benchmark {

    private static final int LENG_CONLL_BENCHMARK = 6;
    private static final String NAME_OF_TRAINING_FILE = "VTB_Train.conll";
    private static final String NAME_OF_TEST_FILE = "VTB_Test.conll";
    private static StringBuffer resuilt;
    private static DataSplit dataSplit;
    private static String URL_OF_TREEBANK;
    private static double noSentence = 0;
    private static double noCorrectSentence = 0;
    private static double noRelation = 0;
    private static double noCorrectRelation = 0;
    private static double noDirection = 0;
    private static double noCorrectDirection = 0;

    /**
     * @param args
     */
    public static void main(String[] args) {
        if (args.length == 2) {
            if (args[0].equals("-i")) {
                URL_OF_TREEBANK = args[1];
                resuilt = new StringBuffer();
                dataSplit = new DataSplit();
                benchMark();
                System.out.println(resuilt.toString());
            } else {
                System.out
                        .println("Option should starts with <-i>. Example: -i data/Viet_treebank.conll");
            }

        } else {
            System.out
                    .println("Please insert Url of treebank, option should starts with <-i>. Example: -i data/Viet_treebank.conll");
        }

    }

    private static void benchMark() {
        dataSplit.splitData(URL_OF_TREEBANK, NAME_OF_TRAINING_FILE,
                NAME_OF_TEST_FILE, 0.7);
        ArrayList<String[]> testData = dataSplit.getTestData();
        noSentence = testData.size();
        TrainingExperiment trainingExperiment = new TrainingExperiment();
        for (int parserMode = 0; parserMode < 8; parserMode++) {
            noCorrectSentence = noRelation = noCorrectRelation = 0;
            try {
                if (trainingExperiment.train(parserMode, NAME_OF_TRAINING_FILE,
                        "mode" + parserMode)) {
                    System.out.println("Create Parser...");
                    Parser parser = new Parser("mode" + parserMode);
                    System.out.println("Parser ready!");
                    for (int i = 0; i < testData.size(); i++) {
                        if (checkCorrectSentence(
                                parser.parser(cutToken(testData.get(i))),
                                testData.get(i))) {
                            noCorrectSentence++;
                        }
                    }

                } else {
                    break;
                }
            } catch (MaltChainedException e) {
                e.printStackTrace();
                System.out.println("Create parser false!");
            }

            System.out.println("Correct sentence: " + noCorrectSentence);
            System.out.println("All sentence: " + noSentence);
            System.out.println("Correct relation: " + noCorrectRelation);
            System.out.println("All relation: " + noRelation);
            resuilt.append("Mode: " + parserMode + "\t Number of sentence: "
                    + noSentence + "\t Correct sentence: " + noCorrectSentence
                    + " (" + (int) ((noCorrectSentence / noSentence) * 100)
                    + "%)\t Number of relation: " + noRelation
                    + "\t Correct relation: " + noCorrectRelation + " ("
                    + (int) ((noCorrectRelation / noRelation) * 100) + "%)\n"
                    + "\t Correct direction: " + noCorrectDirection + " ("
                    + (int) ((noCorrectDirection / noDirection) * 100) + "%)\n");
        }
    }

    private static boolean checkCorrectSentence(String[] test, String[] learn) {
        if (test.length == learn.length) {
            boolean testRelation = true;
            int size = test.length;
            for (int i = 0; i < size; i++) {
                String[] testTab = test[i].split("\t");
                String[] learnTab = learn[i].split("\t");
                noRelation++;
                noDirection++;
                if (testTab[6].equalsIgnoreCase(learnTab[6])) {
                    noCorrectDirection++;
                    if (testTab[7].equalsIgnoreCase(learnTab[7])) {
                        noCorrectRelation++;
                    }
                } else {
                    testRelation = false;
                }
            }
            if (testRelation) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static String[] cutToken(String[] input) {
        String[] output = new String[input.length];

        for (int i = 0; i < input.length; i++) {
            String[] tabConll = input[i].split("\t");
            StringBuffer outConll = new StringBuffer();
            for (int j = 0; j < LENG_CONLL_BENCHMARK; j++) {
                if (j == LENG_CONLL_BENCHMARK - 1) {
                    // no append TAB to end of token
                    outConll.append(tabConll[j]);
                } else {
                    outConll.append(tabConll[j] + "\t");
                }
            }
            output[i] = outConll.toString();
        }
        return output;
    }

}
