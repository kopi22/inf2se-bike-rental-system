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

	public Location getDeliveryAddress() {
		return deliveryAddress;
	}

	public int getReturnShopID() {
		return returnShopID;
	}

	public boolean isConsentConfirmation() {
		return consentConfirmation;
	}
}
