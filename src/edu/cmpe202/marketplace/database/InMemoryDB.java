package edu.cmpe202.marketplace.database;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import edu.cmpe202.marketplace.database.schema.InventoryEntry;
import edu.cmpe202.marketplace.database.schema.Order;
import edu.cmpe202.marketplace.database.schema.OrderEntry;
import edu.cmpe202.marketplace.io.CardsFile;

public class InMemoryDB {
	private static InMemoryDB INSTANCE;
	public HashMap<String, InventoryEntry> inventoryDb = new HashMap<>(); 
	private HashSet<String> cards = new HashSet<String>();
	
	public static InMemoryDB getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new InMemoryDB();
		}
		return INSTANCE;
	}
	
	public void populateInventory(ArrayList<String> entries) throws Exception {
		
		for (String entry: entries) {
			String[] e = entry.split(",");
			if (e.length != 4) {
				throw new Exception("Inventory entry is invalid");
			}
			InventoryEntry inventoryEntry = new InventoryEntry();
			inventoryEntry.setCategory(e[0]);
			inventoryEntry.setProductName(e[1]);
			inventoryEntry.setQuantity(Integer.parseInt(e[2]));
			inventoryEntry.setPrice(Float.parseFloat(e[3]));
			inventoryDb.put(inventoryEntry.getProductName(), inventoryEntry);
		}
	}
	
	public Order createOrder(ArrayList<String> entries) throws Exception {
		ArrayList<OrderEntry> orderEntries = new ArrayList<>();
		for (String entry: entries) {
			String[] e = entry.split(",");
			if (e.length != 3) {
				throw new Exception("Order entry is invalid");
			}
			OrderEntry orderEntry = new OrderEntry();
			orderEntry.setProductName(e[0]);
			orderEntry.setQuantity(Integer.parseInt(e[1]));
			orderEntry.setCardNumber(e[2]);
			orderEntries.add(orderEntry);
		}
		return new Order(orderEntries);
	}
	
	public String getCategoryForProduct(String productName) throws Exception {
		if (!inventoryDb.containsKey(productName)) {
			throw new Exception("Product Name is un-recognized");
		}
		return inventoryDb.get(productName).getCategory();
	}
	
	public float getPriceForProduct(String productName) throws Exception {
		if (!inventoryDb.containsKey(productName)) {
			throw new Exception("Product Name is un-recognized");
		}
		return inventoryDb.get(productName).getPrice();
	}
	
	public int getQuantityForProduct(String productName) throws Exception {
		if (!inventoryDb.containsKey(productName)) {
			throw new Exception("Product Name is un-recognized");
		}
		return inventoryDb.get(productName).getQuantity();
	}
	
	public void updateQauntityForProduct(OrderEntry orderEntry) throws Exception {
		if (!inventoryDb.containsKey(orderEntry.getProductName())) {
			throw new Exception("Product Name is un-recognized");
		}
		InventoryEntry inventory = inventoryDb.get(orderEntry.getProductName());
		inventory.setQuantity(inventory.getQuantity() - orderEntry.getQuantity());
	}
	
	public void addCard(String card) {
		cards.add(card);
	}
	
	public void printAllCards(Path path) throws IOException {
		ArrayList<String> message = new ArrayList<>();
		message.add("CardNumber");
		for(String card: cards) {
			message.add(card);
		}
		new CardsFile(path).writeToFile(message);
	}
	
	public void printInventory() {
		StringBuilder inventoryString = new StringBuilder();
		for (String product: inventoryDb.keySet()) {
			inventoryString.append(product + " : " + inventoryDb.get(product).getQuantity() + ", ");
		}
		System.out.println("Current Inventory: { " + inventoryString.toString() + " }");
	}
}


