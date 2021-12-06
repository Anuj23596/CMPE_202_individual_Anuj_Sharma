package edu.cmpe202.marketplace.io;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

public class CheckOutFile implements ResultFile {

	Path path;
	public CheckOutFile(Path path) {
		this.path = path;
	}

	@Override
	public void writeToFile(ArrayList<String> orderOutput) throws IOException {
		String filePath =  path.getParent().toString() + "/checkout_success_" + System.currentTimeMillis() +  ".csv";
		FileWriter successFile = new FileWriter(filePath);

		for( String line : orderOutput ) {
			successFile.write( line + "\n" );
			System.out.println(line + "\n");
		}

		successFile.close();
		System.out.println("Output written to: " + filePath);
	}
}
