package edu.cmpe202.marketplace.database.schema;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

import edu.cmpe202.marketplace.database.InMemoryDB;
import edu.cmpe202.marketplace.io.CheckOutFile;
import edu.cmpe202.marketplace.io.ErrorFile;
import edu.cmpe202.marketplace.io.ResultFile;

public class Order {

	private ArrayList<OrderEntry> order;
	private InMemoryDB inMemoryDB;
	private static HashMap<String, Integer> maxQuanityPerCategory = new HashMap<String, Integer>() {
		private static final long serialVersionUID = 1L;

		{
			put("Essential", 5);
			put("Luxury", 3);
			put("Misc", 6);
		}
	};;

	public Order(ArrayList<OrderEntry> order) {
		this.order = order;
		this.inMemoryDB = InMemoryDB.getInstance();
	}

	/*
	 * Returns null if no category category is in-valid else returns category name
	 * that is in-valid
	 */
	private String getInvalidCategoryQuantity() throws Exception {
		HashMap<String, Integer> quanityPerCategory = new HashMap<String, Integer>();
		for (OrderEntry orderEntry : order) {
			String category = inMemoryDB.getCategoryForProduct(orderEntry.getProductName());
			int quantity = orderEntry.getQuantity();
			if (quanityPerCategory.containsKey(category)) {
				quantity += quanityPerCategory.get(category);
			}
			quanityPerCategory.put(category, quantity);
		}

		for (String category : quanityPerCategory.keySet()) {
			if (quanityPerCategory.get(category) > maxQuanityPerCategory.get(category)) {
				return category;
			}
		}
		return null;
	}

	/*
	 * Returns null if no product quantity is in-valid else returns product name
	 * that is in-valid
	 */
	private String getInvalidProductQuantity() throws Exception {
		HashMap<String, Integer> quanityPerProduct = new HashMap<String, Integer>();
		for (OrderEntry orderEntry : order) {
			String productName = orderEntry.getProductName();
			int quantity = orderEntry.getQuantity();
			if (quanityPerProduct.containsKey(productName)) {
				quantity += quanityPerProduct.get(productName);
			}
			quanityPerProduct.put(productName, quantity);
		}

		for (String productName : quanityPerProduct.keySet()) {
			if (quanityPerProduct.get(productName) > inMemoryDB.getQuantityForProduct(productName)) {
				return productName;
			}
		}
		return null;
	}

	public boolean processOrder(Path outputPath) throws Exception {
		String invalidCategory = getInvalidCategoryQuantity();
		String invalidProduct = getInvalidProductQuantity();
		boolean success = false;
		float totalAmount = 0;

		if (invalidCategory == null && invalidProduct == null) {
			// This is a valid order
			success = true;
			for (OrderEntry orderEntry : order) {
				totalAmount += orderEntry.getQuantity() * inMemoryDB.getPriceForProduct(orderEntry.getProductName());
				inMemoryDB.updateQauntityForProduct(orderEntry);
				inMemoryDB.addCard(orderEntry.getCardNumber());

			}
		}
		printMessage(outputPath, success, totalAmount, invalidProduct, invalidCategory);
		return success;
	}

	public void printMessage(Path outputPath, boolean success, float totalAmount, String invalidProduct,
			String invalidCategory) throws IOException {
		ResultFile file = getResultFile(outputPath, success);
		if (success) {
			printSuccessMessage(file, totalAmount);
		} else {
			printErrorMessage(file, invalidProduct, invalidCategory);
		}
	}

	private void printSuccessMessage(ResultFile file, float totalAmount) throws IOException {
		ArrayList<String> message = new ArrayList<String>();
		for (OrderEntry orderEntry : order) {
			message.add(orderEntry.toString());
		}
		message.add("Total Amount Paid: " + totalAmount);
		file.writeToFile(message);
	}

	private void printErrorMessage(ResultFile file, String invalidProduct, String invalidCategory) throws IOException {
		ArrayList<String> message = new ArrayList<String>();
		message.add("Please correct quantities.");
		if (invalidProduct != null) {
			message.add("Product exceeding quantity: " + invalidProduct);
		}

		if (invalidCategory != null) {
			message.add("Category exceeding quantity: " + invalidCategory);
		}
		file.writeToFile(message);
	}

	private ResultFile getResultFile(Path path, boolean success) {
		if (success) {
			return new CheckOutFile(path);
		} else {
			return new ErrorFile(path);
		}
	}
}
