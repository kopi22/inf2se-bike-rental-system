package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class PricingPolicyDefaultImpl implements PricingPolicy {

    private Map<String, BigDecimal> pricingScheme = new HashMap<>();

    @Override
    public void setDailyRentalPrice(BikeType bikeType, BigDecimal dailyPrice) {
        pricingScheme.put(bikeType.getTypeName(), dailyPrice);
    }

    @Override
    public BigDecimal calculatePrice(Collection<Bike> bikes, DateRange duration) {
        return bikes.stream().map(bike -> pricingScheme.get(bike.getType()))
            .reduce(BigDecimal.valueOf(0), BigDecimal::add).multiply(BigDecimal.valueOf(duration.toDays()));
    }
}
