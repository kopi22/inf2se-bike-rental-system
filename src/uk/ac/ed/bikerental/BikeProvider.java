package uk.ac.ed.bikerental;

import java.lang.ModuleLayer.Controller;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class BikeProvider {
	
	String bikeProviderName;
	Controller controller;
	int bikeProviderId;
	String address;
	int phoneNumber;
	Map<String, String> openingHours;
	Collection<Bike> bikes;
	Collection<Integer> partnerIDs;
	Map<String, Double> pricingScheme;
	BigDecimal depositRate;
	PricingPolicy pricingPolicy;
	DepositPolicy depositPolicy;
	
	public Quote getQuote(Map<Integer, Integer> bikes, DateRange dateRange) {
		return null;
		
	}
	
	public boolean checkAvailability(Collection<Bike> bikes,DateRange dateRange ) {
		return false;
	}
	
	public void returnBike(Bike bike,DateRange dateRange) {
		
	}
	
	public boolean bookBikes (Collection<Bike> bikes, DateRange dateRange) {
		return false;
	}
	
	public void bikesReturnedToShop(Collection<Bike> bikes) {
		
	}
	
	public void bikesReturnedToPartner(Collection<Bike> bikes, Location partnerLocation) {
		
	}
	
	
	

}
