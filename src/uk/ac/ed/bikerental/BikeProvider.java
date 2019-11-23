package uk.ac.ed.bikerental;

import java.lang.ModuleLayer.Controller;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;


public class BikeProvider {
	
	private String bikeProviderName;
	private Controller controller;
	private int bikeProviderId;
	private String address;
	private int phoneNumber;
	private Map<String, String> openingHours;
	private Collection<Bike> bikes;
	private Collection<Integer> partnerIDs;
	private Map<String, Double> pricingScheme;
	private BigDecimal depositRate;
	private PricingPolicy pricingPolicy;
	private DepositPolicy depositPolicy;
	
	public Quote getQuote(Map<Integer, Integer> bikes, DateRange dateRange) {
		return null;
		
	}
	
	public boolean checkAvailability(Collection<Bike> bikes,DateRange dateRange ) {
		return false;
	}
	
	public void returnBike(Bike bike) {
		
	}
	
	public boolean bookBikes (Collection<Bike> bikes, DateRange dateRange) {
		return false;
	}
	
	public void bikesReturnedToShop(Collection<Bike> bikes) {
		
	}
	
	public void bikesReturnedToPartner(Collection<Bike> bikes, Location partnerLocation) {
		
	}
	
	
	

}
