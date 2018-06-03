package vn.com.epi.treebank.converter;

import java.io.IOException;
import java.io.Reader;
import java.io.StreamTokenizer;

import edu.stanford.parser.nlp.process.TokenizerAdapter;

public class VietTreebankTokenizer extends TokenizerAdapter {

	private boolean isLoadToken = true;
	private boolean isLabel = false;
	private int nextTok = 0;

	private static class VietTreebankStreamTokenizer extends StreamTokenizer {

		/**
		 * Create a StreamTokenizer for PennTreebank trees. This sets up all the
		 * character meanings for treebank trees
		 * 
		 * @param r
		 *            The reader steam
		 */
		private VietTreebankStreamTokenizer(Reader r) {
			super(r);
			// start with new tokenizer syntax -- everything ordinary
			resetSyntax();
			// treat parens as symbols themselves -- done by reset
			// ordinaryChar(')');
			// ordinaryChar('(');

			// treat chars in words as words, like a-zA-Z
			// treat all the typewriter keyboard symbols as parts of words
			// You need to look at an ASCII table to understand this!
			wordChars('!', '\''); // non-space non-ctrl symbols before '('
			wordChars('*', '/'); // after ')' till before numbers
			wordChars('0', '9'); // numbers
			wordChars(':', '@'); // symbols between numbers, letters
			wordChars('A', 'Z'); // uppercase letters
			wordChars('[', '`'); // symbols between ucase and lcase
			wordChars('a', 'z'); // lowercase letters
			wordChars('{', '~'); // symbols before DEL
			wordChars(128, 255); // C.Thompson, added 11/02

			// take the normal white space charaters, including tab, return,
			// space
			whitespaceChars(0, ' ');
		}
	}

	public VietTreebankTokenizer(Reader r) {
		super(new VietTreebankStreamTokenizer(r));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.stanford.nlp.process.TokenizerAdapter#getNext()
	 */
	@Override
	public String getNext() {

		try {
			if (isLoadToken) {
				nextTok = st.nextToken();
			}
			switch (nextTok) {
			case java.io.StreamTokenizer.TT_EOL:
				isLoadToken = true;
				return eolString;
			case java.io.StreamTokenizer.TT_EOF:
				isLoadToken = true;
				return null;
			case java.io.StreamTokenizer.TT_WORD: {
				if (isLabel) {
					isLabel = false;
					isLoadToken = true;
					StringBuilder output = new StringBuilder();
					output.append(st.sval);
					return output.toString();
				} else {
					StringBuilder output = new StringBuilder();

					do {
						output.append(st.sval + " ");
						nextTok = st.nextToken();
						isLoadToken = false;
					} while (nextTok == java.io.StreamTokenizer.TT_WORD);
					return output.toString().trim();
				}
			}
			case java.io.StreamTokenizer.TT_NUMBER:
				isLoadToken = true;
				return Double.toString(st.nval);
			default:
				char[] t = { (char) nextTok }; // (array initialization)
				isLoadToken = true;
				if (nextTok == '(') {
					isLabel = true;
				}
				return new String(t);
			}
		} catch (IOException ioe) {
			// do nothing, return null
			return null;
		}

	}

}
