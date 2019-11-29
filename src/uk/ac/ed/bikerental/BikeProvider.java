package uk.ac.ed.bikerental;

import java.lang.ModuleLayer.Controller;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BikeProvider {
    private static int nextId = 1;

    private String bikeProviderName;
    private Controller controller;
    private final int id;
    private Location address;
    private String phoneNumber;
    private Map<String, String> openingHours;
    private final Map<Integer, Bike> bikes;
    private final Collection<Integer> partnerIds;
    private Collection<Integer> bookingsIds;
    private PricingPolicy pricingPolicy;
    private ValuationPolicy depositPolicy;

    // if pricing/deposit policy is not provided, then default is used
    public BikeProvider(String bikeProviderName, Location address, String phoneNumber,
        Map<String, String> openingHours, PricingPolicy pricingPolicy, ValuationPolicy depositPolicy) {
        assert Objects.nonNull(pricingPolicy) &&  Objects.nonNull(depositPolicy);

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

    public Quote getQuote(Map<String, Integer> requestedBikes, DateRange dateRange) {
        int numBikesToFind = requestedBikes.values().stream().mapToInt(Integer::intValue).sum();
        if (numBikesToFind > bikes.size()) {
            return null;
        }

        Iterator<Bike> bikeIterator = bikes.values().iterator();

        // copy the map so we can work on it without modifying the original
        Map<String, Integer> bikesToFind = new HashMap<>();
        for (String bikeType : requestedBikes.keySet()) {
            bikesToFind.put(bikeType, requestedBikes.get(bikeType));
        }

        Collection<Integer> bikesIds = new HashSet<>();
        // for number of bikesToFind
        for ( ; numBikesToFind > 0; numBikesToFind--) {
            // while(true) since we want to iterate over the bikes which are not available
            while(true) {
                if (!bikeIterator.hasNext()) {
                    return null;
                }
                Bike bike = bikeIterator.next();
                String bikeType = bike.getType();
                if (bikesToFind.getOrDefault(bikeType, 0) > 0 && bike.isAvailable(dateRange)) {
                    // add bikeId for the bikes in the quote
                    bikesIds.add(bike.getBikeId());
                    // decrease number of desired bikes of given type
                    bikesToFind.replace(bikeType, bikesToFind.get(bikeType) - 1);
                    // break to decrease numBikesToFind
                    break;
                }
            }
        }
        BigDecimal totalPrice = pricingPolicy.calculatePrice(getBikesByIds(bikesIds), dateRange);
        BigDecimal totalDeposit = bikesIds.stream()
            .map(bikes::get)
            .map(bike -> depositPolicy.calculateValue(bike, dateRange.getStart()))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new Quote(id, totalDeposit, totalPrice, dateRange, bikesIds);
    }

    private Collection<Bike> getBikesByIds(Collection<Integer> bikeIds) {
        return bikeIds.stream()
            .map(bikes::get)
            .collect(Collectors.toSet());
    }


    public boolean bookBikes (Collection<Integer> bikesIds, DateRange dateRange) {
        Collection<Bike> bikesToBook = bikesIds.stream()
                                           .map(bikes::get)
                                           .collect(Collectors.toSet());

        if (!bikesToBook.stream().allMatch((bike -> bike.isAvailable(dateRange)))) {
            return false;
        }

        bikesToBook.forEach(bike -> bike.book(dateRange));

        return true;
    }

    public Location getAddress() {
        return address;
    }

    public void updateBikesStatuses(Collection<Integer> orderedBikesIDs, BookingStatus bookingStatus) {
        BikeStatus nextBikeStatus;
        // update the status of the bike based on the booking status
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
        orderedBikesIDs.stream().map(bikes::get).forEach(bike -> bike.setStatus(nextBikeStatus));
    }

    public void addBike(Bike bike) {
        bikes.put(bike.getBikeId(), bike);
    }

    public int getId() {
        return id;
    }

    public void setRentalPrice(BikeType bikeType, BigDecimal price) {
        pricingPolicy.setDailyRentalPrice(bikeType, price);
    }
    
    //return bikes to stock
    public void returnBikes(Collection<Integer> bikeIDs, int returnShopId) {
        // check if return shop is partnered with bikeProvider
        if (returnShopId != id && !partnerIds.contains(returnShopId)) {
            throw new CannotReturnToNotPartnerException(
                "BikeProvider (id " + id +
                    ") is not partnered with BikeProvider (id " + returnShopId + ")"
            );
        }
        bikeIDs.stream()
            .map(bikes::get)
            .forEach(bike -> bike.returnToShop(returnShopId));
    }

    public void addPartner(int partnerId) {
        partnerIds.add(partnerId);
    }

    public void addBooking(int orderID) {
        bookingsIds.add(orderID);
    }
}
