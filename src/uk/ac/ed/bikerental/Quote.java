package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.Collection;

public class Quote {
	
	private final int bikeProviderID;
	private final BigDecimal deposit;
	private final BigDecimal price;
	private final DateRange dateRange;
	private final Collection<Integer> bikeIDs;
	
	public Quote(int bikeProviderID, BigDecimal deposit, BigDecimal price,DateRange dateRange, Collection<Integer> bikeIDs ) {
		this.bikeProviderID=bikeProviderID;
		this.deposit=deposit;
		this.price=price;
		this.dateRange=dateRange;
		this.bikeIDs=bikeIDs;
		
	}

	public int getBikeProviderID() {
		return bikeProviderID;
	}

	public BigDecimal getDeposit() {
		return deposit;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public DateRange getDateRange() {
		return dateRange;
	}

	public Collection<Integer> getBikeIDs() {
		return bikeIDs;
	}
}
