package vn.com.epi.gate.maltparser.benchmark;

import org.maltparser.MaltParserService;

public class TrainingExperiment {
    public boolean train(int mode, String trainingDataFile, String modelName) {
        try {
            if (mode == 0) {
                System.out.println("Trains the parser model " + trainingDataFile + " and uses parsing mode 2planar");
                new MaltParserService(1).runExperiment("-c " + modelName
                        + " -i " + trainingDataFile
                        + " -m learn -ne true -nr true -a 2planar -pcov true -2pr true");
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
                System.out.println("Trains the parser model " + trainingDataFile + " and uses parsing mode Nivre");
                new MaltParserService(7).runExperiment("-c "
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
