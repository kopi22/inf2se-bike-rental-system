package uk.ac.ed.bikerental;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class BikeProviderManager {

    private final Map<Integer, BikeProvider> bikeProvidersMap = new HashMap<>();

    public Collection<Quote> getQuotes(Map<String, Integer> bikes, DateRange dateRange, Location searchLocation) {
        Collection<BikeProvider> bikeProviders = getBikeProvidersByLocation(searchLocation);
        return getQuotesForDateRange(bikeProviders, bikes, dateRange);
    }

    private Collection<BikeProvider> getBikeProvidersByLocation(Location location) {
        Collection<BikeProvider> bikeProviders = bikeProvidersMap.values();
        bikeProviders.removeIf((bikeProvider -> !bikeProvider.getAddress().isNearTo(location)));

        return bikeProviders;
    }

    // modifies the supplied bikeProviders collection
    private Collection<Quote> getQuotesForDateRange(Collection<BikeProvider> bikeProviders, Map<String, Integer> bikes, DateRange dateRange) {

        return bikeProviders.stream()
            .map(bikeProvider -> bikeProvider.getQuote(bikes, dateRange))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());
    }

    public boolean bookBikes(int bikeProviderId, Collection<Integer> bikesIds, DateRange dateRange) {
        assert bikeProvidersMap.containsKey(bikeProviderId);

        return bikeProvidersMap.get(bikeProviderId).bookBikes(bikesIds, dateRange);
    }

    public void updateBikesStatuses(int bikeProviderID, Collection<Integer> orderedBikesIDs, BookingStatus bookingStatus) {
        bikeProvidersMap.get(bikeProviderID).updateBikesStatuses(orderedBikesIDs, bookingStatus);
    }

    public Location getBikeProviderLocation(int bikeProviderId) {
        return bikeProvidersMap.get(bikeProviderId).getAddress();
    }

    public void addBikeProvider(BikeProvider bikeProvider) {
        bikeProvidersMap.put(bikeProvider.getId(), bikeProvider);
    }
    
    public void returnBikes(int returnShopID, Collection<Integer> bikeIDs, int ownerID) {
        BikeProvider bikeProvider = bikeProvidersMap.get(ownerID);
        bikeProvider.returnBikes(bikeIDs, returnShopID);
    }
}
