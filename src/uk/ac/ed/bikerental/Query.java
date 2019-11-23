package uk.ac.ed.bikerental;

import java.time.LocalDate;
import java.util.Map;

public class Query {
	
	private Map<String, Integer> bikes;
	private LocalDate startDate;
	private LocalDate endDate;
	private Location location;
	
	public Query(Map<String, Integer> bikes, LocalDate startDate, LocalDate endDate, Location location) {
		this.bikes=bikes;
		this.startDate=startDate;
		this.endDate=endDate;
		this.location=location;
	}

}

