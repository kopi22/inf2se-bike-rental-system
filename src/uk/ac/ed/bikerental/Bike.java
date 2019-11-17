package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Bike {
	static private int nextID = 1;
	
	int bikeId;
	BikeType type;
	int ownerId;
	BigDecimal originalValue;
	BikeStatus status;
	List<Location> storeLocations;
	List<DateRange> reservationDates;
	LocalDate productionDate;
	
	
	public Bike(BikeType type, int ownerId, BigDecimal originalValue, BikeStatus status, LocalDate productionDate) {
		bikeId = nextID++;
		BigDecimal depreciationRate;
		this.type = type;
		this.ownerId = ownerId;
		this.originalValue = originalValue;
		this.status = status;
		this.productionDate = productionDate;
		this.reservationDates = new ArrayList<DateRange>();
		this.storeLocations = new ArrayList<Location>();
		
	}
	
    public BikeType getType() {
    	return this.type;
      
    }
}