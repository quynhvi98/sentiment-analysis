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
package vn.hus.nlp.tokenizer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;

import com.google.common.base.Charsets;
import com.google.common.io.Closeables;
import com.google.common.io.Files;

public class Program {

	private static DefaultTokenizerFactory tokenizerFactory;

	private Program() throws IOException { }	
	
    /**
     * Run tokenizer against all file in the given directory.
     * @param input
     * @param output
     * @throws IOException 
     * @throws FileNotFoundException 
     * @throws UnsupportedEncodingException 
     */
    private static void runDirectory(File input, File output)
            throws UnsupportedEncodingException, FileNotFoundException,
            IOException {
        for (File child : input.listFiles()) {
            runFile(child, new File(output, child.getName()));
        }
    }

    private static void runFile(File input, File output)
            throws UnsupportedEncodingException, FileNotFoundException,
            IOException {
        BufferedReader inputReader = null;
        BufferedWriter writer = null;
        try {
    		inputReader = Files.newReader(input, Charsets.UTF_8);
            writer = Files.newWriter(output, Charsets.UTF_8);
            runCharStreams(inputReader, writer);
        } finally {
            Closeables.closeQuietly(inputReader);
            Closeables.closeQuietly(writer);
        }
    }

    private static void runCharStreams(BufferedReader inputReader,
            Writer writer) throws IOException {
        Tokenizer tokenizer = tokenizerFactory.getTokenizer(inputReader);
        while (tokenizer.hasNext()) {
        	Token token = tokenizer.next();
        	writer.write(token.toString());
        	writer.write(" ");
        }
    }

	public static void main(String[] args) throws FileNotFoundException,
			IOException {
		CommandLine cmd = parseArgs(args);
		
		long start = System.currentTimeMillis();
		String n = cmd.getOptionValue("n", "2");
        tokenizerFactory = new DefaultTokenizerFactory("resources", Integer.parseInt(n));
        
        args = cmd.getArgs();
        if (args.length >= 1) {
            for (String input : args) {
                String output = cmd.getOptionValue("o", input + ".tokens");
                File inputFile = new File(input);
                File outputFile = new File(output);
                if (inputFile.isDirectory()) {
                    runDirectory(inputFile, outputFile);
                } else {
                    runFile(inputFile, outputFile);
                }
                System.out.printf("Result written to %s.\n", output);
            }
        } else {
            BufferedReader inp = new BufferedReader(
                    new InputStreamReader(System.in, Charsets.UTF_8));
            Writer out = new OutputStreamWriter(System.out, Charsets.UTF_8);
            runCharStreams(inp, out);
        }
        
        long stop = System.currentTimeMillis();
        System.out.println("Finished in " + (stop-start)/1000.0 + "s");
	}

    private static void printHelp(Options options) {
        System.out.println("vntokenizer [input]");
    }

    /**
     * @param args
     * @return
     */
    private static CommandLine parseArgs(String[] args) {
        Options options = new Options();
        options.addOption("o", "output", true, "Output path");
        options.addOption("n", "n-gram-size", true, "Number of words in a n-gram");
        try {
            CommandLine cmd = new PosixParser().parse(options, args);
            checkOptionValues(cmd);
            return cmd;
        } catch (ParseException e) {
            System.err.println(e.getMessage());
            printHelp(options);
            System.exit(1);
            return null;
        }
    }

    private static void checkOptionValues(CommandLine cmd) {
        if (cmd.hasOption("n")) {
            try {
                int n = Integer.parseInt(cmd.getOptionValue("n"));
                if (!(n == 1 || n == 2)) {
                    System.err.println("Currently we support only unigram and bigram (n=1,2).");
                    System.exit(1);
                }
            } catch (NumberFormatException e) {
                System.err.println("n must be a positive integer");
                System.exit(1);
            }
        }
    }

}
