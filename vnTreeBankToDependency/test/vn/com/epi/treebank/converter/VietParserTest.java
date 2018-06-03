package vn.com.epi.treebank.converter;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;

import com.google.common.base.Charsets;
import com.google.common.io.Closeables;
import com.google.common.io.Files;

import edu.stanford.parser.nlp.trees.GrammaticalStructure;
import edu.stanford.parser.nlp.trees.Tree;
import edu.stanford.parser.nlp.trees.TreeReader;
import edu.stanford.parser.nlp.trees.TypedDependency;

public class VietParserTest {

	private static String URL_OF_TREEBANK;
	private static String CONLL_OUTPUT_FILE;

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length == 4) {
			if (args[0].equals("-i") && args[2].endsWith("-o")) {
				URL_OF_TREEBANK = args[1];
				CONLL_OUTPUT_FILE = args[3];
				File[] file = (new File(URL_OF_TREEBANK)).listFiles();
				int limit = file.length;
				Writer writer = null;
				try {
					writer = Files.newWriter(new File(CONLL_OUTPUT_FILE),
							Charsets.UTF_8);
					for (int i = 0; i < limit; i++) {
						Reader r = null;
						TreeReader tr = null;
						try {
							r = Files.newReader(file[i], Charsets.UTF_8);
							tr = new VietTreeReader(r);
							Tree t = tr.readTree();
							while (t != null) {
								VietGrammaticalStructure gs = new VietGrammaticalStructure(t);
								List<TypedDependency> tdl = gs
										.typedDependenciesCCprocessed(true);
								writer.append(
										GrammaticalStructure
												.dependenciesToString(gs, tdl,
														gs.root(), true, false))
										.append("\n");
								writer.flush();
								t = tr.readTree();
							}
						} finally {
							Closeables.closeQuietly(tr);
							Closeables.closeQuietly(r);
						}
					}
				} finally {
					Closeables.closeQuietly(writer);
				}

			} else {
				System.out
						.println("Option <-i> : url of treebank, <-o> : conll output file. \nExample: -i D:/data/source -o D:/data/Viet_treebank.conll");
			}

		} else {
			System.out
					.println("Please insert Url of treebank! \nOption <-i> : url of treebank, <-o> : conll output file. \nExample: -i D:/data/source -o D:/data/Viet_treebank.conll");
		}

	}
}
