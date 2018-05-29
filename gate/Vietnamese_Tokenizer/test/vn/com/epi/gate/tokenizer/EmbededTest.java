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
package vn.com.epi.gate.tokenizer;

import gate.Annotation;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.Gate;
import gate.ProcessingResource;
import gate.creole.ExecutionException;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.util.ExtensionFileFilter;
import gate.util.GateException;
import gate.util.OffsetComparator;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author Nguyen Vi Duong (vi.duong.bk@gmail.com)
 * 
 */
public class EmbededTest {

	public static final String PATH = "../vnSentimentAnalysis";

	private static ProcessingResource tokenizerPlugin = null;
	private static SerialAnalyserController controller;

	@BeforeClass
	public static void initClass() throws GateException, IOException {
		Gate.init();
		Gate.getCreoleRegister().registerDirectories(
				new File(Gate.getPluginsHome(), "Tools").toURI().toURL());
		Gate.getCreoleRegister().registerComponent(TokenizerPlugin.class);
		FeatureMap featureMap = Factory.newFeatureMap();
		featureMap.put("bigramUrl", new File(PATH
				+ "/Vietnamese Tokenizer/resources/bigram/bigram.xml").toURI()
				.toURL());
		featureMap.put("unigramUrl", new File(PATH
				+ "/Vietnamese Tokenizer/resources/bigram/unigram.xml").toURI()
				.toURL());
		featureMap.put("dfaUrl", new File(PATH
				+ "/Vietnamese Tokenizer/resources/automata/dfaLexicon.xml")
				.toURI().toURL());
		featureMap.put("normalizationRuleUrl", new File(PATH
				+ "/Vietnamese Tokenizer/resources/normalization/rules.txt")
				.toURI().toURL());
		tokenizerPlugin = (ProcessingResource) Factory.createResource(
				TokenizerPlugin.class.getCanonicalName(), featureMap);
		controller = (SerialAnalyserController) Factory
				.createResource("gate.creole.SerialAnalyserController");
		controller.add(tokenizerPlugin);
	}

	@Test
	public void simple() throws ResourceInstantiationException, IOException,
			ExecutionException {
		Corpus corpus = createCorpus("corpuses/simple");
		controller.setCorpus(corpus);
		controller.init();
		controller.execute();
		Document doc = (Document) corpus.get(0);
		List<Annotation> annotList = new ArrayList<Annotation>(
				doc.getAnnotations());
		Collections.sort(annotList, new OffsetComparator());
		Annotation firstAnnot = annotList.get(0);
		Assert.assertEquals("Việt", firstAnnot.getFeatures().get("string")
				.toString());
		Assert.assertEquals(0, firstAnnot.getStartNode().getOffset()
				.longValue());
		Assert.assertEquals(4, firstAnnot.getEndNode().getOffset().longValue());
		Assert.assertTrue(doc.getAnnotations().get("SpaceToken").size() > 0);
		for (Annotation annotation : annotList) {
			System.out.print(annotation.getFeatures().get("string"));
		}
	}

	private Corpus createCorpus(String dirPath)
			throws ResourceInstantiationException, IOException,
			MalformedURLException {
		Corpus corpus = Factory.newCorpus("Simple corpus");
		File dir = new File(dirPath);
		ExtensionFileFilter filter = new ExtensionFileFilter("XML files", "xml");
		corpus.populate(dir.toURI().toURL(), filter, null, false);
		return corpus;
	}

	@AfterClass
	public static void afterClass() {

	}

}
