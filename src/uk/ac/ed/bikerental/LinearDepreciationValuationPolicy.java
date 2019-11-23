package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LinearDepreciationValuationPolicy implements ValuationPolicy {

	private BigDecimal depreciationRate;

	LinearDepreciationValuationPolicy(BigDecimal depreciationRate) {
		this.depreciationRate = depreciationRate;
	}

	@Override
	public BigDecimal calculateValue(Bike bike, LocalDate reservartionStartDate) {

		LocalDate productionDate = bike.getProductionDate();
		if (productionDate.isAfter(reservartionStartDate)) {
			return null;
		}
		BigDecimal bikeAge = BigDecimal.valueOf(new DateRange(productionDate, reservartionStartDate).toYears());
		BigDecimal replacementValue = bike.getReplacementValue();

		BigDecimal discount = bikeAge.multiply(depreciationRate).multiply(replacementValue);

		return replacementValue.subtract(discount);
	}

}
