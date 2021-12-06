package edu.cmpe202.marketplace;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Paths;

import edu.cmpe202.marketplace.database.InMemoryDB;
import edu.cmpe202.marketplace.database.schema.Order;
import edu.cmpe202.marketplace.io.CsvReader;

public class Main {

	InMemoryDB inMemoryDB;

	public Main() {
		inMemoryDB = InMemoryDB.getInstance();
	}

	public void initializeDatabase(String filePath) throws Exception {
		CsvReader csvReader = new CsvReader(filePath);
		csvReader.readCsv(true /* headerPresent */);
		inMemoryDB.populateInventory(csvReader.getCsvContent());
		inMemoryDB.printInventory();
	}

	public void processOrder(String filePath) throws Exception {
		CsvReader csvReader = new CsvReader(filePath);
		csvReader.readCsv(true /* headerPresent */);
		Order order = inMemoryDB.createOrder(csvReader.getCsvContent());
		boolean success = order.processOrder(Paths.get(filePath));
		if (success) {
			System.out.println("Printing all cards");
			inMemoryDB.printAllCards(Paths.get(filePath));
		} else {
			System.out.println("Order failed. Not adding cards.");
		}
		inMemoryDB.printInventory();
	}

	private static String getInventoryFileName(String[] args) {
		StringBuilder stringBuilder = new StringBuilder();
		if (args.length == 0) {
			System.out.println("No invetory filename found. Exiting.");
			System.exit(0);
		}

		// If filename has spaces
		for (String arg : args) {
			if (!stringBuilder.isEmpty()) {
				stringBuilder.append(" ");
			}
			stringBuilder.append(arg);
		}
		return stringBuilder.toString();
	}
	
	private String getOrderFileFromUser() throws IOException {
		System.out.println( "Enter file path for a new order or press Enter to exit" );
		BufferedReader reader = new BufferedReader( new InputStreamReader( System.in ) );
		return reader.readLine();
	}
	
	
	public static void main(String[] args) throws Exception {
		Main main = new Main();
		main.initializeDatabase(getInventoryFileName(args));
		while (true) {
			String orderFilePath = main.getOrderFileFromUser();
			if(orderFilePath.equals("")) {
				System.out.println("Thank you for shopping with us. Exiting now.");
				System.exit(0);
			} else {
				main.processOrder(orderFilePath);
			}
		}
	}
}
