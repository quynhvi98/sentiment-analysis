package vn.com.epi.orm.sa;

/**
 * Posted from May 30, 2018 9:40 AM
 *
 * @author viquy (vi.quynh.31598@gmail.com)
 */
import java.io.FileWriter;
import java.io.IOException;

public class InitResultFile {

	public static void main(String[] args) {
		try {
			FileWriter writer = new FileWriter("resultTest.csv");
			writer.append("time to load model,time to analysis,sum time,number of character,number of document,mlsecond per document");
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
