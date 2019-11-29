package uk.ac.ed.bikerental;

import java.lang.ModuleLayer.Controller;
import java.util.Collection;
import java.util.HashSet;

public class User {
	
	static private int nextId = 1;
	private int customerID;
	private Controller controller;
	private String email;
	private String firstName;
	private String secondName;
	private Location addressOfCustomer;
	private String phoneNumber;

	private Collection<Integer> bookingIDs;

	public User(String email,String firstName,String secondName,Location addressOfCustomer, String phoneNumber) {
		this.customerID = nextId++;
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
	

	//add booking to the list of bookingIDs
	public void addBooking(int bookingID) {
		assert bookingID > 0;

		bookingIDs.add(bookingID);
	}

}
