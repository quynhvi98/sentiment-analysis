package vn.com.epi.vnlp.tokenizer.dictionary;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import vn.com.epi.vnlp.dfa.MinimalDFABuilder;
import vn.com.epi.vnlp.dfa.io.DfaSaxReader;
import vn.hus.nlp.model.AbstractResolver;
import vn.hus.nlp.model.BigramResolver;
import vn.hus.nlp.tokenizer.AutomataTokenizer;
import vn.hus.nlp.tokenizer.Tokenizer;
import vn.hus.nlp.tokenizer.TokenizerUtils;
import vn.hus.nlp.tokenizer.segmenter.Segmenter;
import vn.hus.nlp.tokenizer.segmenter.StringNormalizer;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.common.io.LineProcessor;

public class DictionaryBenchmark {

	private StopWatch stopWatch;
	private StringNormalizer normalizer;
	private AbstractResolver resolver;
	private List<String> sentences;
	private List<String> words;

	public static void main(String[] args) throws IOException {
		DictionaryFactory[] factories = {
				new DfaOnlineDictionaryFactory(),
				new DfaDictionaryFactory(),
				new HashDictionaryFactory()
		};
		String corpusDir = args[0];
		new DictionaryBenchmark().run(factories, corpusDir);
	}

	private void run(DictionaryFactory[] factories, String corpusDir)
			throws IOException {
		Logger.getRootLogger().setLevel(Level.ERROR);
		stopWatch = new StopWatch();
		initCorpus(corpusDir);
		initTokenizerComponents("resources");
		System.out.println("------------");
		for (int i = 0; i < factories.length; i++) {
			try {
				System.out.println("Testing " + factories[i].getName());
				Dictionary dict = initialize(factories, i);
				acceptWords(dict);
				tokenize(dict);
			} catch (Exception ex) {
				System.out.println("Error: " + ex.getMessage() + " (turn on System.err for details)");
				ex.printStackTrace();
			}
			System.out.println("------------");
		}
	}

	/**
	 * @param corpusDir 
	 * @throws IOException 
	 * 
	 */
	private void initCorpus(String corpusDir) throws IOException {
		sentences = readCorpusSentences(corpusDir);
		System.out.println("Total sentences: " + sentences.size());
		words = readCorpusWords(corpusDir);
		System.out.println("Total words: " + words.size());
	}

	/**
	 * @param resourceDir 
	 * @throws IOException 
	 * 
	 */
	private void initTokenizerComponents(String resourceDir) throws IOException {
		normalizer = new StringNormalizer(new FileReader(
				new File(resourceDir, "normalization/rules.txt")));
		resolver = new BigramResolver(
				new File(resourceDir, "bigram/bigram.xml"),
				new File(resourceDir, "bigram/unigram.xml"));
	}

	/**
	 * Test dictionary in a tokenizer for sure
	 * @param dict
	 * @throws IOException 
	 */
	private void tokenize(Dictionary dict) throws IOException {
		stopWatch.reset();
		for (int i = 0; i < sentences.size(); i++) {
			if (i == 0) {
				stopWatch.start();
			} else {
				stopWatch.resume();
			}
			Tokenizer tokenizer = new AutomataTokenizer(sentences.get(i),
					new Segmenter(dict, normalizer), resolver);
			TokenizerUtils.toList(tokenizer);
			stopWatch.suspend();
		}
		System.out.printf("Tokenizing time: %.2f\n", stopWatch.getSeconds());
	}

	private void acceptWords(Dictionary dict)
			throws IOException {
		stopWatch.reset();
		for (int k = 0; k < words.size(); k++) {
			String word = words.get(k);
			String[] syllables = word.split(" ");
			if (k == 0) {
				stopWatch.start();
			} else {
				stopWatch.resume();
			}
			for (int j = 0; j < syllables.length; j++) {
				if (j == 0) {
					dict.acceptFirst(syllables[j]);
				} else {
					dict.acceptNext(syllables[j]);
				}
			}
			stopWatch.suspend();
		}
		System.out.printf("Word accepting time: %.2f\n", stopWatch.getSeconds());
	}

	private Dictionary initialize(DictionaryFactory[] factories, int i)
			throws IOException {
		stopWatch.reset();
		stopWatch.start();
		long memoryBefore = Runtime.getRuntime().freeMemory();
		Dictionary dict = factories[i].create();
		long memoryAfter = Runtime.getRuntime().freeMemory();
		stopWatch.stop();
		System.out.printf("Init time: %.2f\n", stopWatch.getSeconds());
		System.out.println("Memory usage (noisy approximation): " + 
				(memoryBefore - memoryAfter) / 1024 + "KB");
		return dict;
	}
	
	private static List<String> readCorpusWords(String dir) throws IOException {
		final List<String> words = new ArrayList<String>();
		for (File file : new File(dir).listFiles()) {
			Files.readLines(file, Charsets.UTF_8, new LineProcessor<Void>() {

				@Override
				public Void getResult() {
					return null;
				}

				@Override
				public boolean processLine(String line) throws IOException {
					if (line.startsWith("<")) {
						return true;
					}
					String[] wordsInLine = line.split(" ");
					for (int i = 0; i < wordsInLine.length; i++) {
						wordsInLine[i] = wordsInLine[i].replace("_", " ");
						words.add(wordsInLine[i]);
					}
					return true;
				}
			});
		}
		return words;
	}
	
	private static List<String> readCorpusSentences(String dir) throws IOException {
		final List<String> sentences = new ArrayList<String>();
		for (File file : new File(dir).listFiles()) {
			Files.readLines(file, Charsets.UTF_8, new LineProcessor<Void>() {

				@Override
				public Void getResult() {
					return null;
				}

				@Override
				public boolean processLine(String line) throws IOException {
					if (line.startsWith("<")) {
						return true;
					}
					sentences.add(line.trim().replace('_', ' '));
					return true;
				}
			});
		}
		return sentences;
	}

	private static interface DictionaryFactory {
		String getName();
		Dictionary create() throws IOException;
	}
	
	private static class DfaOnlineDictionaryFactory implements DictionaryFactory {

		
		@Override
		public Dictionary create() throws IOException {
			return new DfaDictionary(
					MinimalDFABuilder.fromFile("data/words.txt"));
		}

		@Override
		public String getName() {
			return "DFA online";
		}
		
	}
	
	private static class DfaDictionaryFactory implements DictionaryFactory {

		@Override
		public Dictionary create() throws IOException {
			return new DfaDictionary(
					DfaSaxReader.readFile("data/dfa.xml"));
		}

		@Override
		public String getName() {
			return "DFA";
		}
		
	}
	
	private static class HashDictionaryFactory implements DictionaryFactory {

		@Override
		public Dictionary create() throws IOException {
			return HashDictionary.fromFile("data/words.txt");
		}

		@Override
		public String getName() {
			return "HashMap";
		}
		
	}

	//TODO: prefer replace Apache commons by new library Guava
	private static class StopWatch extends org.apache.commons.lang3.time.StopWatch {

		public double getSeconds() {
			return getTime() / 1000.0;
		}

	}
	
}
