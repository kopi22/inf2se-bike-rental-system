package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ValuationPolicyDefaultImpl implements ValuationPolicy {

    final BigDecimal depositRate;

    public ValuationPolicyDefaultImpl(BigDecimal depositRate) {
        this.depositRate = depositRate;
    }

    @Override
    //calculate Deposit value for a bike provided as an argument, taking into consideration time provided as an argument
    public BigDecimal calculateValue(Bike bike, LocalDate date) {
        assert bike != null;

        LocalDate productionDate = bike.getProductionDate();
        if (productionDate.isAfter(date)) {
            return null;
        }

        return bike.getReplacementValue().multiply(depositRate);
    }
}
