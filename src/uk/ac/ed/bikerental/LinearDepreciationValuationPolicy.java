package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;

public class LinearDepreciationValuationPolicy implements ValuationPolicy {

	private BigDecimal depreciationRate;

	public LinearDepreciationValuationPolicy(BigDecimal depreciationRate) {
		this.depreciationRate = depreciationRate;
	}

	// returns the price
	@Override
	public BigDecimal calculateValue(Bike bike, LocalDate reservationsStartDate) {
		assert bike != null && reservationsStartDate != null;

		LocalDate productionDate = bike.getProductionDate();

		if (productionDate.isAfter(reservationsStartDate)) {
			return null;
		}

		BigDecimal bikeAge = BigDecimal.valueOf(new DateRange(productionDate, reservationsStartDate).toYears());
		BigDecimal replacementValue = bike.getReplacementValue();

		// Implementing the formula for discount (age*depreciation*value)
		BigDecimal discount = bikeAge.multiply(depreciationRate).multiply(replacementValue);

		return replacementValue.subtract(discount);
	}

}
