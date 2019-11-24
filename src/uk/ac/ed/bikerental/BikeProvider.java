package uk.ac.ed.bikerental;

import java.lang.ModuleLayer.Controller;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;


public class BikeProvider {
    private static int nextId = 1;

    private String bikeProviderName;
    private Controller controller;
    private int id;
    private Location address;
    private String phoneNumber;
    private Map<String, String> openingHours;
    private Map<Integer, Bike> bikes;
    private Collection<Integer> partnerIds;
    private Collection<Integer> bookingsIds;
    private PricingPolicy pricingPolicy;
    private ValuationPolicy depositPolicy;

    public BikeProvider(String bikeProviderName, Location address, String phoneNumber,
        Map<String, String> openingHours, PricingPolicy pricingPolicy, ValuationPolicy depositPolicy) {
        this.bikeProviderName = bikeProviderName;
        this.id = nextId++;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.openingHours = openingHours;
        this.bikes = new HashMap<>();
        this.partnerIds = new HashSet<>();
        this.pricingPolicy = pricingPolicy;
        this.depositPolicy = depositPolicy;
    }

    public BikeProvider(String bikeProviderName, Location address, String phoneNumber,
        Map<String, String> openingHours, PricingPolicy pricingPolicy, BigDecimal depositRate) {
        this(bikeProviderName, address, phoneNumber, openingHours, pricingPolicy,
                new ValuationPolicyDefaultImpl(depositRate));
    }

    public BikeProvider(String bikeProviderName, Location address, String phoneNumber,
        Map<String, String> openingHours, ValuationPolicy valuationPolicy) {
        this(bikeProviderName, address, phoneNumber, openingHours, new PricingPolicyDefaultImpl(),
            valuationPolicy);
    }

    public BikeProvider(String bikeProviderName, Location address, String phoneNumber,
        Map<String, String> openingHours, BigDecimal depositRate) {
        this(bikeProviderName, address, phoneNumber, openingHours, new PricingPolicyDefaultImpl(),
            new ValuationPolicyDefaultImpl(depositRate));
    }

    public Quote getQuote(Map<String, Integer> requestedbikes, DateRange dateRange) {
        int noBikesToFind = requestedbikes.values().stream().mapToInt(Integer::intValue).sum();
        if (noBikesToFind > bikes.size()) {
            return null;
        }
        Iterator<Bike> bikeIterator = bikes.values().iterator();

        Map<String, Integer> bikesToFind = new HashMap<>();
        for (String bikeType : requestedbikes.keySet()) {
            bikesToFind.put(bikeType, requestedbikes.get(bikeType));
        }

        Collection<Integer> bikesIds = new HashSet<>();
        BigDecimal totalDeposit = BigDecimal.valueOf(0.0);

        for ( ; noBikesToFind > 0; noBikesToFind--) {
            while(true) {
                if (!bikeIterator.hasNext()) {
                    return null;
                }
                Bike bike = bikeIterator.next();
                String bikeType = bike.getType();
                if (bikesToFind.getOrDefault(bikeType, 0) > 0 && bike.isAvailable(dateRange)) {
                    bikesIds.add(bike.getBikeId());
                    totalDeposit = totalDeposit.add(depositPolicy.calculateValue(bike, dateRange.getStart()));
                    bikesToFind.replace(bikeType, bikesToFind.get(bikeType) - 1);
                    break;
                }
            }
        }
        BigDecimal totalPrice = pricingPolicy.calculatePrice(getBikesByIds(bikesIds), dateRange);

        return new Quote(id, totalDeposit, totalPrice, dateRange, bikesIds);
    }

    private Collection<Bike> getBikesByIds(Collection<Integer> bikeIds) {
        return bikeIds.stream().map(bikeId -> bikes.get(bikeId)).collect(Collectors.toSet());
    }


    public boolean bookBikes (Collection<Integer> bikesIds, DateRange dateRange) {
        Collection<Bike> bikesToBook = bikesIds.stream().map(bikeId -> bikes.get(bikeId))
            .collect(Collectors.toSet());

        if (!bikesToBook.stream().allMatch((bike -> bike.isAvailable(dateRange)))) {
            return false;
        }

        bikesToBook.forEach(bike -> bike.book(dateRange));

        return true;
    }

    public void bikesReturnedToShop(Collection<Bike> bikes) {

    }

    public void bikesReturnedToPartner(Collection<Bike> bikes, Location partnerLocation) {

    }

    public Location getAddress() {
        return address;
    }

    public void updateBikesStatuses(Collection<Integer> orderedBikesIDs, BookingStatus bookingStatus) {
        BikeStatus nextBikeStatus;
        switch (bookingStatus) {
            case IN_DELIVERY:
                nextBikeStatus = BikeStatus.IN_DELIVERY;
                break;
            case IN_PROGRESS:
                nextBikeStatus = BikeStatus.RENTED;
                break;
            case COMPLETED_PARTNER:
                nextBikeStatus = BikeStatus.RETURNED_PARTNER;
                break;
            case IN_TRANSITION:
                nextBikeStatus = BikeStatus.IN_TRANSITION;
                break;
            case COMPLETED:
                nextBikeStatus = BikeStatus.IN_STORE;
                break;
            default:
                throw new UnsupportedStatusChangeException(bookingStatus.toString());
        }
        orderedBikesIDs.stream().map(bikeId -> bikes.get(bikeId)).forEach(bike -> bike.setStatus(nextBikeStatus));
    }

    public void addBike(Bike bike) {
        bikes.put(bike.getBikeId(), bike);
    }

    public int getId() {
        return id;
    }

    public void setRentalPriec(BikeType bikeType, BigDecimal price) {
        pricingPolicy.setDailyRentalPrice(bikeType, price);
    }
}
