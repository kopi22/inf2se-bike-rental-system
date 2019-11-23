package uk.ac.ed.bikerental;

import java.util.Collection;

public class Quote {
	
	private int bikeProviderID;
	private int deposit;
	private int totalPriceForQuote;
	private DateRange dateRange;
	private Collection<Integer> bikeIDs;
	
	public Quote(int bikeProviderID, int deposit, int totalPriceForQuote,DateRange dateRange, Collection<Integer> bikeIDs ) {
		this.bikeProviderID=bikeProviderID;
		this.deposit=deposit;
		this.totalPriceForQuote=totalPriceForQuote;
		this.dateRange=dateRange;
		this.bikeIDs=bikeIDs;
		
	}
	

}
