package edu.cmpe202.marketplace.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CsvReader {

	File file;
	private ArrayList<String> csvContents = new ArrayList<>();

	public CsvReader(String filePath) {
		this.file = new File(filePath);
	}

	public void readCsv(boolean headerPresent) throws IOException {

		if (!file.exists()) {
			System.out.println("File is not present");
			return;
		}

		BufferedReader bufferedReader = new BufferedReader(new FileReader(file));

		String line ;
		while ((line = bufferedReader.readLine()) != null ) {

			if(headerPresent ) { headerPresent = false; continue; }

			csvContents.add(line);
		}
		bufferedReader.close();
	}

	public ArrayList<String> getCsvContent() {
		return csvContents;
	}
}
