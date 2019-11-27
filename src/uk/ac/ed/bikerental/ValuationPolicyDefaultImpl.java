package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ValuationPolicyDefaultImpl implements ValuationPolicy {

    BigDecimal depositRate;

    public ValuationPolicyDefaultImpl(BigDecimal depositRate) {
        this.depositRate = depositRate;
    }

    @Override
    public BigDecimal calculateValue(Bike bike, LocalDate date) {
        assert bike != null;
        return bike.getReplacementValue().multiply(depositRate);
    }
}
