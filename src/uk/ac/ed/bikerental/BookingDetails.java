package uk.ac.ed.bikerental;

public class BookingDetails {
	
	private Location deliveryAddress;
	private int returnShopID;
	private boolean consentConfirmation;
	
	public BookingDetails(Location deliveryAddress,int returnShopID, boolean consentConfirmation ) {
		this.deliveryAddress=deliveryAddress;
		this.returnShopID=returnShopID;
		this.consentConfirmation=consentConfirmation;
	}

}
