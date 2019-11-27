package uk.ac.ed.bikerental;

import java.lang.ModuleLayer.Controller;
import java.util.Collection;
import java.util.HashSet;

public class User {
	private int customerID;
	private Controller controller;
	private String email;
	private String firstName;
	private String secondName;
	private Location addressOfCustomer;
	private String phoneNumber;

	private Collection<Integer> bookingIDs;

	public User(int customerID,String email,String firstName,String secondName,Location addressOfCustomer, String phoneNumber) {
		this.customerID = customerID;
		this.email = email;
		this.firstName = firstName;
		this.secondName = secondName;
		this.addressOfCustomer = addressOfCustomer;
		this.phoneNumber = phoneNumber;
		this.bookingIDs = new HashSet<>();
	}

	public int getUserId() {
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
	

	public void addBooking(int bookingID) {
		assert bookingID > 0;

		bookingIDs.add(bookingID);
	}

}
