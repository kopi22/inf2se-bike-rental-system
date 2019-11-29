package uk.ac.ed.bikerental;

public class BookingDetails {
	
	private final Location deliveryAddress;
	private final int returnShopID;
	private final boolean consentConfirmation;
	
	public BookingDetails(Location deliveryAddress, int returnShopID, boolean consentConfirmation) {
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
