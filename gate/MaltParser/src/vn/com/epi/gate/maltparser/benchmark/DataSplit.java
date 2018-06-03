package vn.com.epi.gate.maltparser.benchmark;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;

public class DataSplit {

    private Random generator = new Random(readSystemCurrentTime());
    private ArrayList<String[]> trainSentences = new ArrayList<String[]>();
    private ArrayList<String[]> testSentences = new ArrayList<String[]>();

    private boolean readAndSplitSentence(File inputFile, double rate) {
        ArrayList<String> sentenceArray = new ArrayList<String>();
        String strLine;
        BufferedReader br;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(
                    inputFile), "UTF-8"));
            while ((strLine = br.readLine()) != null) {
                if (strLine.equals("")) {
                    String[] sentence = new String[sentenceArray.size()];
                    for (int i = 0; i < sentenceArray.size(); i++) {
                        sentence[i] = sentenceArray.get(i);
                    }

                    if (generator.nextDouble() < rate) {
                        trainSentences.add(sentence);
                    } else {
                        testSentences.add(sentence);
                    }
                    sentenceArray = new ArrayList<String>();
                } else {
                    sentenceArray.add(strLine);
                }
            }
            br.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean writeFile(String fileName, String text) {
        File file = new File(fileName);
        try {
            BufferedWriter fos = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(file), "UTF-8"));
            fos.write(text);
            fos.close();
            return true;
        } catch (Exception e1) {
            return false;
        }
    }

    public boolean splitData(String inputFileName, String trainFileName,
                             String testFileName, double rate) {
        boolean status = readAndSplitSentence(new File(inputFileName), rate);
        if (status) {
            StringBuffer stringBuffer = new StringBuffer();

            // write train data;
            for (int i = 0; i < trainSentences.size(); i++) {
                for (int j = 0; j < trainSentences.get(i).length; j++) {
                    stringBuffer.append(trainSentences.get(i)[j] + "\n");
                }
                stringBuffer.append("\n");
            }
            status = writeFile(trainFileName, stringBuffer.toString());
            stringBuffer = new StringBuffer();

            // write test data;
            for (int i = 0; i < testSentences.size(); i++) {
                for (int j = 0; j < testSentences.get(i).length; j++) {
                    stringBuffer.append(testSentences.get(i)[j] + "\n");
                }
                stringBuffer.append("\n");
            }
            status = writeFile(testFileName, stringBuffer.toString());
            if (status) {
                return true;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    private long readSystemCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTimeInMillis();
    }

    public ArrayList<String[]> getTestData() {
        return testSentences;
    }

    public void setTestData(ArrayList<String[]> testData) {
        testSentences = testData;
    }

    public ArrayList<String[]> getTrainSentences() {
        return trainSentences;
    }

    public void setTrainSentences(ArrayList<String[]> trainSentences) {
        this.trainSentences = trainSentences;
    }
}
