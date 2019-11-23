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
	private int phoneNumber;
	private Collection<Integer> bookingIDs;
	
	//TODO: implement addBooking
	
	public boolean addBooking(int bookingID) {
		return false;
	}

}
