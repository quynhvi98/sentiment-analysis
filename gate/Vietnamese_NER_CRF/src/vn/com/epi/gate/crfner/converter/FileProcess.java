package vn.com.epi.gate.crfner.converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * Posted from May 26, 2018 4:40 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
public class FileProcess {
	private ArrayList<String> content;
	private File file;

	public FileProcess() {
		// empty constructor
	}

	public FileProcess(String path) {
		this.file = new File(path);
	}

	public FileProcess(File file) {
		this.file = file;
	}

	public FileProcess(File file, ArrayList<String> content) {
		this.file = file;
		this.content = content;
	}

	public ArrayList<String> read() throws IOException {
		ArrayList<String> str = new ArrayList();
		String strLine;
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(this.file), "UTF-8"));
		while ((strLine = br.readLine()) != null) {
			if(!strLine.equals(" ")){
				str.add(strLine);
			}
		}

		br.close();
		return this.content = str;

	}
	
	public String[] readAsArray() throws IOException {
		ArrayList<String> str = new ArrayList();
		String strLine;
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(this.file), "UTF-8"));
		while ((strLine = br.readLine()) != null) {
			if(!strLine.equals(" ")){
				str.add(strLine);
			}
		}

		br.close();
		String[] t = new String[0];
		return (String[]) str.toArray(t);

	}

	public void save(ArrayList<String> text) throws IOException {
		this.content = text;
		BufferedWriter fos = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(this.file), "UTF-8"));
		StringBuilder allString = new StringBuilder();
		for (String string : text) {
			allString.append(string + "\n");
		}
		fos.write(allString.toString());
		fos.close();
	}
	
	public void save(ArrayList<String> text, boolean isDic) throws IOException {
		this.content = text;
		BufferedWriter fos = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(this.file), "UTF-8"));
		StringBuilder allString = new StringBuilder();
		if(isDic){
			allString.append(text.size() + "\n");
			for (int i = 1; i < text.size(); i++) {
				allString.append(text.get(i) + "\n");
			}			
		}
		fos.write(allString.toString());
		fos.close();
	}

	public void save(ArrayList<String> text, int count) throws IOException {
		this.content = text;
		BufferedWriter fos = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(this.file), "UTF-8"));
		StringBuilder allString = new StringBuilder();
		allString.append(count + "\n");
		for (int i = 1; i < text.size(); i++) {
			allString.append(text.get(i) + "\n");
		}
		fos.write(allString.toString());
		fos.close();
	}
	public void deleteAll() throws IOException {
		FileOutputStream eraser = null;
		eraser = new FileOutputStream(this.file);
		eraser.write((new String()).getBytes());
		eraser.close();
	}

	public ArrayList<String> getContent() {
		return content;
	}

	public void setContent(ArrayList<String> content) {
		this.content = content;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
