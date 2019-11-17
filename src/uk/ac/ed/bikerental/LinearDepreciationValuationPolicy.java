package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LinearDepreciationValuationPolicy implements ValuationPolicy {

	@Override
	public BigDecimal calculateValue(Bike bike, LocalDate date) {
		
		BigDecimal original_bike_valueBigDecimal = bike.replacement_value;
		
		return null;
	}

}
