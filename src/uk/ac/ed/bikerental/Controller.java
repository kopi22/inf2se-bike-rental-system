package uk.ac.ed.bikerental;

import java.util.Collection;

public class Controller {

    BikeProviderManager bikeProviderManager;

    public Collection<Quote> getQuotes(Query query) {
        return bikeProviderManager.getQuotes(query.getBikes(),
            new DateRange(query.getStartDate(), query.getEndDate()),
            query.getLocation());
    }
}
