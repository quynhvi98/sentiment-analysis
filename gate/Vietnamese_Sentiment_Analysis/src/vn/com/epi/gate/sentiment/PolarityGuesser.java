package vn.com.epi.gate.sentiment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Posted from May 26, 2018 3:49 PM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public class PolarityGuesser {

	private Map<String, FrequencyMap<String, String>> opinionMap = new HashMap<String, FrequencyMap<String, String>>();

	private int sumOfCount = 0;

	public void loadModel(String path) throws IOException {
		try {
			loadModel(new File(path).toURI().toURL());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
	}

	public void loadModel(URL url) throws IOException, URISyntaxException {

		if (url.toString().endsWith(".lst")) {
			loadModel(new File(url.toURI()));
		} else if (url.toString().endsWith(".def")) {
			Set<String> listFile = loadDefFile(new BufferedReader(new InputStreamReader(url.openStream(), "UTF-8")));
			File parentFile = new File(url.toURI());
			for (String fileName : listFile) {
				File tempFile = new File(parentFile.getParent(), fileName.trim());
				loadModel(tempFile);
			}
		}
	}

	private void loadModel(File input) throws IOException {
		Reader reader = new InputStreamReader(new FileInputStream(input), "UTF-8");
		BufferedReader in = new BufferedReader(reader);
		try {
			String line;
			while ((line = in.readLine()) != null) {
				parseLine(line);
			}
		}finally {
			if (in != null) {
				try {
					in.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Set<String> loadDefFile(BufferedReader in) throws IOException {
		Set<String> output = new HashSet();
		try {
			String line;
			while ((line = in.readLine()) != null) {
				output.add(line);
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return output;
	}

	private static final Pattern COUNT_PATTERN = Pattern.compile("\tcount\\s*=\\s*(\\d+)($|\\s)");

	private void parseLine(String line) {
		String[] chunks = line.split("\t");
		if (chunks.length < 3) {
			return;
		}
		int count = 1;
		Matcher countMatcher = COUNT_PATTERN.matcher(line);
		if (countMatcher.find()) {
			count = Integer.parseInt(countMatcher.group(1));
		}
		sumOfCount += count;
		addOpinion(chunks[0], chunks[1], chunks[2], count);
	}

	private void addOpinion(String opinionExpression, String feature, String polarity, int count) {
		FrequencyMap<String, String> frequencyMap = opinionMap.get(opinionExpression);
		if (frequencyMap == null) {
			frequencyMap = new FrequencyMap<String, String>();
			opinionMap.put(opinionExpression, frequencyMap);
		}
		frequencyMap.put(feature, polarity, count);
	}

	public String guess(String opinionExpression, String feature) {
		FrequencyMap<String, String> featureMap = opinionMap.get(opinionExpression);
		if (featureMap == null) {
			return null;
		}
		return featureMap.get(feature);
	}

	public int getSumOfCount() {
		return sumOfCount;
	}

	public void setSumOfCount(int sumOfCount) {
		this.sumOfCount = sumOfCount;
	}
}
