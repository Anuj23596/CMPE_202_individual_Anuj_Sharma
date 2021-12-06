package edu.cmpe202.marketplace.io;

import java.io.IOException;
import java.util.ArrayList;

public interface ResultFile {

	public void writeToFile(ArrayList<String> orderOutput) throws IOException;

}
