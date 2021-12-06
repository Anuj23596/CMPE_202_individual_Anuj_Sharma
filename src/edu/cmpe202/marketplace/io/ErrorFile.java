package edu.cmpe202.marketplace.io;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class ErrorFile implements ResultFile {

	Path path;
	public ErrorFile(Path path) {
		this.path = path;
	}

	@Override
	public void writeToFile(ArrayList<String> orderOutput) throws IOException {
		String filePath = path.getParent().toString() + "/checkout_error_" + System.currentTimeMillis() +  ".txt";
		FileWriter errorFile = new FileWriter(filePath);
		for( String line : orderOutput ) {
			errorFile.write( line + "\n" );
			System.out.println(line + "\n");
		}

		errorFile.close();
		System.out.println("Output written to: " + filePath);
	}
}
