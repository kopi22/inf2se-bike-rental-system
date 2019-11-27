package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PricingPolicyDefaultImpl implements PricingPolicy {

    private Map<String, BigDecimal> pricingScheme = new HashMap<>();

    @Override
    public void setDailyRentalPrice(BikeType bikeType, BigDecimal dailyPrice) {
        assert dailyPrice != null;
        pricingScheme.put(bikeType.getTypeName(), dailyPrice);
    }

    @Override
    public BigDecimal calculatePrice(Collection<Bike> bikes, DateRange duration) {
        System.out.println(bikes.size());
        return bikes.stream().map(bike -> pricingScheme.get(bike.getType()))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .multiply(BigDecimal.valueOf(duration.toDays()));
    }
}
