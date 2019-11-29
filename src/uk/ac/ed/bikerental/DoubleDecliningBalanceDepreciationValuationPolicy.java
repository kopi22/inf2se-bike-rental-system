package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DoubleDecliningBalanceDepreciationValuationPolicy implements ValuationPolicy {

    private final BigDecimal depreciationRate;

    public DoubleDecliningBalanceDepreciationValuationPolicy(BigDecimal depreciationRate) {
        this.depreciationRate = depreciationRate;
    }

    // returns null if cannot calculate the price
    @Override
    public BigDecimal calculateValue(Bike bike, LocalDate reservationStartDate) {
        assert bike != null && reservationStartDate != null;

        LocalDate productionDate = bike.getProductionDate();
        if (productionDate.isAfter(reservationStartDate)) {
            return null;
        }
        BigDecimal bikeAge = BigDecimal.valueOf(new DateRange(productionDate, reservationStartDate).toYears());
        BigDecimal replacementValue = bike.getReplacementValue();
        BigDecimal depreciationRateMulTwo = depreciationRate.multiply(BigDecimal.valueOf(2));

        // Implementing the formula for deposit value*(1-2*depreciation)^age
        BigDecimal result = replacementValue.multiply((BigDecimal.ONE.subtract(depreciationRateMulTwo)
                .pow(bikeAge.intValue())));

        return result.compareTo(BigDecimal.ZERO) > 0 ? result : BigDecimal.ZERO;
    }

}