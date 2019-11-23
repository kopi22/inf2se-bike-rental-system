package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.Collection;

public class Booking {
	private int bikeProviderID;
	private int orderID;
	private Collection<Integer> orderedBikesIDs;
	private DateRange dateRange;
	private int returnShopID;
	private Location deliveryAddress;
	private Payment payment;
	private BigDecimal deposit;
	private DepositStatus depositStatus;
	
	
	
	
	

}
