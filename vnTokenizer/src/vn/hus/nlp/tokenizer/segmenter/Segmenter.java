/**
 * (C) Le Hong Phuong, phuonglh@gmail.com
 *  Vietnam National University, Hanoi, Vietnam.
 */
package vn.hus.nlp.tokenizer.segmenter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;

import org.antlr.runtime.CommonToken;
import org.xml.sax.SAXException;

import vn.com.epi.vnlp.dfa.TermData;
import vn.com.epi.vnlp.tokenizer.dictionary.Dictionary;
import vn.hus.nlp.graph.simple.SimpleGraph;
import vn.hus.nlp.tokenizer.Token;
import vn.hus.nlp.tokenizer.VLexer;

/**
 * @author Le Hong Phuong, phuonglh@gmail.com
 *         <p>
 *         vn.hus.nlp.segmenter
 *         <p>
 *         Nov 12, 2007, 8:11:26 PM
 *         <p>
 *         Segmenter of Vietnamese. It splits a chain of Vietnamese syllables
 *         (so called a phrase) into words. Before performing the segmentation,
 *         it does some necessary preprocessing:
 *         <ul>
 *         <li>If the first character of the phrase is an uppercase, it is
 *         changed to lower case.</li>
 *         <li>Normalize the phrase so that the accents of syllables are in
 *         their right places, for example, the syllable <tt>hòa</tt> is
 *         converted to <tt>hoà</tt></li>.
 *         </ul>
 */
public class Segmenter {

	private StringNormalizer normalizer;

	/**
	 * The DFA representing Vietnamese lexicon (the internal lexicon).
	 */
	private Dictionary dict;

	private boolean nameConnectingEnabled = true;

	private int maxSyllablesInAVietnameseWord = 6;

	private static double MAX_EDGE_WEIGHT = 100;

	/**
	 * Default constructor.
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 */
	public Segmenter(Dictionary dict, StringNormalizer normalizer) throws IOException {
		this.dict = dict;
		this.normalizer = normalizer;
	}

	private TermData[][] edgeData;

	/**
	 * Try to connect an unconnected graph. If a graph is unconnected, we find
	 * all of its isolated vertices and add a "fake" transition to them. A
	 * vertex is called isolated if it has not any intransition.
	 * 
	 * @param graph
	 *            a graph
	 */
	private void connect(SimpleGraph graph) {
		// get all isolated vertices - vertices that do not have any
		// intransitions.
		int[] isolatedVertices = graph.getIsolatedVertices();

		// There is a trick here: vertex 0 is always isolated in our linear
		// graph since
		// it is the initial vertex and does not have any intransition.
		// We need to check whether it has an outtransition or not (its degree
		// is not zero),
		// if no, we connect it to the nearest vertex - vertex 1 - to get an
		// edge with weight 1.0;
		// if yes, we do nothing. Note that since the graph represents an array
		// of non-null syllables,
		// so the number of vertices of the graph is at least 2 and it does
		// contain vertex 1.
		boolean zeroVertex = false;
		for (int i = 0; i < isolatedVertices.length; i++) {
			int u = isolatedVertices[i];
			if (u == 0) {
				zeroVertex = true;
				/*
				 * GraphDegree graphDegree = new GraphDegree(graph); if
				 * (graphDegree.degree(0) == 0) { graph.insert(new
				 * Edge(0,1,MAX_EDGE_WEIGHT)); }
				 */
				// we always add a new edge (0,1) regardless of vertex 0 is
				// of degree 0 or higher.
				graph.insertEdge(0, 1, MAX_EDGE_WEIGHT);
			}
			else {
				if (u != 1) {
					// u is an internal isolated vertex, u > 0. We simply add an
					// edge (u-1,u)
					// also with the maximum weight 1.0
					graph.insertEdge(u - 1, u, MAX_EDGE_WEIGHT);
				}
				else { // u == 1
					if (!zeroVertex) { // insert edge (0,1) only when there does
										// not this edge
						graph.insertEdge(u - 1, u, MAX_EDGE_WEIGHT);
					}
				}
			}
		}
	}

	private Token[] buildSegmentation(CommonToken[] syllables, LinkedList<Integer> path) {
		StringBuilder sb = new StringBuilder(100);
		Token[] segmentation = new Token[path.size() - 1];
		int vertex = 0;
		int ii = 0;
		for (int nextVertex : path) {
			if (nextVertex == vertex) {
				continue;
			}
			Token word;
			if (nextVertex == vertex + 1) {
				word = new Token(Token.TERM, syllables[vertex].getText());
				word.setStartOffset(syllables[vertex].getStartIndex());
				word.setEndOffset(syllables[nextVertex - 1].getStopIndex() + 1);
				word.setCaption(syllables[vertex].getType());
			} else {
				int caption = 0;
				sb.delete(0, sb.length());
				for (int j = vertex; j < nextVertex; j++) {
					sb.append(syllables[j].getText()).append(' ');
					caption = makeCaption(caption, syllables[j].getType());
				}
				word = new Token(Token.TERM, sb.substring(0, sb.length() - 1));
				word.setStartOffset(syllables[vertex].getStartIndex());
				word.setEndOffset(syllables[nextVertex - 1].getStopIndex() + 1);
				word.setCaption(caption);
			}
			TermData data = edgeData[vertex][nextVertex];
			if (data != null) {
				word.setStopWord(data.isStopWord());
				word.setName(data.isName());
			}
			segmentation[ii++] = word;
			vertex = nextVertex;
		}
		return segmentation;
	}
	
	private LinkedList<LinkedList<Integer>> allShortestPaths;

	private SimpleGraph graph;

	public ArrayList<Token[]> segment(CommonToken[] phrase) {
		int nV = phrase.length + 1;
		graph = new SimpleGraph(nV, true);
		edgeData = new TermData[nV][nV];
		
		// get syllables of the phrase
		String[] syllables = new String[phrase.length];
		for (int i = 0; i < phrase.length; i++) {
			syllables[i] = normalizer.normalizeSyllable(phrase[i].getText())
					.toLowerCase();
		}
		
		int nV1 = graph.getNumberOfVertices();
		for (int i = 0; i < nV1 - 1; i++) {
			if (nameConnectingEnabled && phrase[i].getType() == VLexer.WORD_UPPER) {
				connectNames(phrase, i);
			}
			if (i == 0 || phrase[i].getType() == VLexer.WORD_LOWER) {
				int k = 0;
				while (k < nV1 - 1 - i && 
						(maxSyllablesInAVietnameseWord > 0 && k <= maxSyllablesInAVietnameseWord) &&
						(k == 0 || phrase[i+k].getType() == VLexer.WORD_LOWER)) {
					boolean accepted = (k == 0 ? 
							dict.acceptFirst(syllables[i + k]) : 
								dict.acceptNext(syllables[i + k]));
					if (accepted) {
						double weight = (double) 1 / (k + 1);
						weight = Math.floor(weight * 100);
						graph.insertEdge(i, i + k + 1, weight);
						edgeData[i][i + k + 1] = (TermData) dict.getData();
					}
					if (!dict.hasNext()) {
						break;
					}
					k++;
				}				
			}
		}
		
		// get the end vertex of the linear graph
		// test the connectivity between the start vertex and the end vertex of
		// the graph.
		// try to connect it if it is not connected and log the abnormal phrase
		// out
		if (!graph.isConnected(0, nV - 1)) {
			// logger.log(Level.INFO, phrase);
			// logger.log(Level.INFO,
			// "The graph of this phrase is not connected. Try to connect it.");
			connect(graph);
		}
		// get all shortest paths from vertex 0 to the end vertex
		allShortestPaths = graph.getAllShortestPaths(0, nV - 1);
		// System.out.println("There are " + allShortestPaths.length +
		// " segmentation(s) for the phrase."); // DEBUG
		// build segmentations corresponding to the shortest paths
		ArrayList<Token[]> result = new ArrayList<Token[]>();
		for (LinkedList<Integer> path : allShortestPaths) {
			Token[] segmentation = buildSegmentation(phrase, path);
			result.add(segmentation);
		}
		return result;
	}

	private void connectNames(CommonToken[] phrase, int i) {
		for (int j = i; j < phrase.length
				&& phrase[j].getType() == VLexer.WORD_UPPER; j++) {
			graph.insertEdge(i, j + 1, 1.0 / (j - i + 1) + MAX_EDGE_WEIGHT);
			edgeData[i][j + 1] = TermData.valueOf(false, true);
		}
	}
	
	public static int makeCaption(int first, int second) {
		if (first == 0) {
			return second;
		}
		else if (first == second) {
			return first;
		}
		else if (first ==  VLexer.WORD_UPPER && second == VLexer.WORD_LOWER) {
			return VLexer.WORD_UPPER;
		}
		else {
			return VLexer.WORD_OTHER;
		}
	}

	public boolean isNameConnectingEnabled() {
		return nameConnectingEnabled;
	}

	public void setNameConnectingEnabled(boolean nameConnectingEnabled) {
		this.nameConnectingEnabled = nameConnectingEnabled;
	}

	/**
	 * @return the maxSyllablesInAVietnameseWord
	 */
	public int getMaxSyllablesInAVietnameseWord() {
		return maxSyllablesInAVietnameseWord;
	}
	
	/**
	 * A hard limit for the max length of examined words. Set it to <= 0 to disable.
	 * @param value the maxSyllablesInAVietnameseWord to set
	 */
	public void setMaxSyllablesInAVietnameseWord(int value) {
		this.maxSyllablesInAVietnameseWord = value;
	}
	
}
