package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DoubleDecliningBalanceDepreciationValuationPolicy implements ValuationPolicy {

	private BigDecimal depreciationRate;

	DoubleDecliningBalanceDepreciationValuationPolicy(BigDecimal depreciationRate) {
		this.depreciationRate = depreciationRate;
	}

	@Override
	public BigDecimal calculateValue(Bike bike, LocalDate reservartionStartDate) {
		assert bike != null && reservartionStartDate != null;

		LocalDate productionDate = bike.getProductionDate();
		if (productionDate.isAfter(reservartionStartDate)) {
			return null;
		}
		BigDecimal bikeAge = BigDecimal.valueOf(new DateRange(productionDate, reservartionStartDate).toYears());
		BigDecimal replacementValue = bike.getReplacementValue();
		BigDecimal depreciationRateMulTwo = depreciationRate.multiply(BigDecimal.valueOf(2.0));

		// Implementing the formula for deposit value*(1-2*depreciation)^age
		BigDecimal result = replacementValue.multiply(
				(BigDecimal.valueOf(1.0).subtract(depreciationRateMulTwo)
						.pow(bikeAge.intValue()))
			);

		return result;
	}

}