package uk.ac.ed.bikerental;

import java.lang.ModuleLayer.Controller;
import java.util.Collection;

public class Customer {
	private int customerID;
	private Controller controller;
	private String email;
	private String firstName;
	private String secondName;
	private Location addressOfCustomer;
	private String phoneNumber;
	public int getCustomerID() {
		return customerID;
	}


	public String getEmail() {
		return email;
	}


	public String getFirstName() {
		return firstName;
	}


	public String getSecondName() {
		return secondName;
	}


	public Location getAddressOfCustomer() {
		return addressOfCustomer;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public Collection<Integer> getBookingIDs() {
		return bookingIDs;
	}


	private Collection<Integer> bookingIDs;
	
	public Customer(int customerID,String email,String firstName,String secondName,Location addressOfCustomer, String phoneNumber, Collection<Integer> bookingIDs) {
		this.customerID=customerID;
		this.email=email;
		this.firstName=firstName;
		this.secondName=secondName;
		this.addressOfCustomer=addressOfCustomer;
		this.phoneNumber=phoneNumber;
		this.bookingIDs=bookingIDs;
	}
	

	public boolean addBooking(int bookingID) {
		if (bookingID==0) {
			return false;
		}
		
		bookingIDs.add(bookingID);
		return true;
		
	}

}
