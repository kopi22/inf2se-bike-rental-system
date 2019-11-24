package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.Collection;

public class Quote {
	
	private int bikeProviderID;
	private BigDecimal deposit;
	private BigDecimal price;
	private DateRange dateRange;
	private Collection<Integer> bikeIDs;
	
	public Quote(int bikeProviderID, BigDecimal deposit, BigDecimal price,DateRange dateRange, Collection<Integer> bikeIDs ) {
		this.bikeProviderID=bikeProviderID;
		this.deposit=deposit;
		this.price=price;
		this.dateRange=dateRange;
		this.bikeIDs=bikeIDs;
		
	}
	

}
