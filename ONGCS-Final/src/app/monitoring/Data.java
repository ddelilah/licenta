package app.monitoring;

import java.io.BufferedReader;
import java.io.FileReader;

public class Data {

	public String getData() throws Exception {
		String data;
		BufferedReader br = new BufferedReader(new FileReader("script.txt"));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
			data = sb.toString();
		} finally {
			br.close();
		}
		return data;
	}

}
