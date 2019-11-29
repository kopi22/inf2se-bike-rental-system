package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ValuationPolicyDefaultImpl implements ValuationPolicy {

    final BigDecimal depositRate;

    public ValuationPolicyDefaultImpl(BigDecimal depositRate) {
        this.depositRate = depositRate;
    }

    @Override
    public BigDecimal calculateValue(Bike bike, LocalDate date) {
        assert bike != null;

        LocalDate productionDate = bike.getProductionDate();
        if (productionDate.isAfter(date)) {
            return null;
        }

        return bike.getReplacementValue().multiply(depositRate);
    }
}
