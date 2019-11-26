package uk.ac.ed.bikerental;

import java.awt.print.Book;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    private DeliveryService deliveryService = DeliveryServiceFactory.getDeliveryService();
    private BikeProviderManager bikeProviderManager;
    private Map<Integer, Booking> bookings;

    public Controller(DeliveryService deliveryService,
        BikeProviderManager bikeProviderManager) {
        this.deliveryService = deliveryService;
        this.bikeProviderManager = bikeProviderManager;
        this.bookings = new HashMap<>();
    }

    public Collection<Quote> getQuotes(Query query) {
        assert query != null;
        assert query.getStartDate().isBefore(query.getEndDate()) ||
            query.getStartDate().isEqual(query.getEndDate());
        assert query.getBikes().size() > 0;

        return bikeProviderManager.getQuotes(query.getBikes(),
            new DateRange(query.getStartDate(), query.getEndDate()),
            query.getLocation());
    }

    public Booking makeBooking(int customerId, Quote quote, BookingDetails bookingDetails) {
        assert quote != null;
        assert bookingDetails != null;

        if (!bikeProviderManager.bookBikes(quote.getBikeProviderID(), quote.getBikeIDs(), quote.getDateRange())) {
            return null;
        }

        Booking booking = new Booking(
            this,
            quote.getBikeProviderID(),
            quote.getBikeIDs(),
            quote.getDateRange(),
            bookingDetails.getReturnShopID(),
            bookingDetails.getDeliveryAddress(),
            quote.getPrice(),
            quote.getDeposit()
        );
        
        if (bookingDetails.isConsentConfirmation()==false) {
        	return null;
        }

        if (bookingDetails.getDeliveryAddress() != null) {
            Location bikeProivderLocation = bikeProviderManager.getBikeProviderLocation(quote.getBikeProviderID());
            deliveryService.scheduleDelivery(booking, bikeProivderLocation, bookingDetails.getDeliveryAddress(), quote.getDateRange().getStart());
        }

        if (bookingDetails.getReturnShopID() != quote.getBikeProviderID()) {
            Location bikeProivderLocation = bikeProviderManager.getBikeProviderLocation(quote.getBikeProviderID());
            Location returnShopLocation = bikeProviderManager.getBikeProviderLocation(bookingDetails.getReturnShopID());
            deliveryService.scheduleDelivery(booking, bikeProivderLocation, bookingDetails.getDeliveryAddress(), quote.getDateRange().getStart());
        }

        // TODO: NOTIFY
        return booking;
    }

    public void updateBikesStatuses(int bikeProviderID, Collection<Integer> orderedBikesIDs, BookingStatus bookingStatus) {
        bikeProviderManager.updateBikesStatuses(bikeProviderID, orderedBikesIDs, bookingStatus);
    }
    
    public void returnOrder(int orderID) {
    	Booking booking = bookings.get(orderID);
    	
    	//return deposit
    	booking.returnDeposit();
    	
    	//bikes processing
    	int returnShopID = booking.getBikeProviderID();
    	Collection<Integer> bikeIDs= booking.getOrderedBikesIDs();
    	int ownerShorpID = booking.getReturnShopID();
    	bikeProviderManager.returnBikes(returnShopID, bikeIDs, ownerShorpID);
    }
}