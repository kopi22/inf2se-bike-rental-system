package uk.ac.ed.bikerental;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class BikeProviderManager {

    private Map<Integer, BikeProvider> bikeProvidersMap;

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

        Collection<Quote> quotes = bikeProviders.stream().map(bikeProvider ->
            bikeProvider.getQuote(bikes, dateRange)
        ).collect(Collectors.toSet());

        return quotes;
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
}
