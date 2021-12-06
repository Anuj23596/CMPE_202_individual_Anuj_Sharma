package edu.cmpe202.marketplace.database.schema;

public class OrderEntry {
	String productName;
	int quantity;
	String cardNumber;
	
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	public String toString() {
		return productName + ", " + quantity + ", " + cardNumber;
	}
}
