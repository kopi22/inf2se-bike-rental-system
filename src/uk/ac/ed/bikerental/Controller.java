package uk.ac.ed.bikerental;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Controller {

    private DeliveryService deliveryService;
    private final BikeProviderManager bikeProviderManager;
    private final UserManager userManager;
    private final Map<Integer, Booking> bookings;

    public Controller(DeliveryService deliveryService,
        BikeProviderManager bikeProviderManager,
        UserManager userManager) {
        this.deliveryService = deliveryService;
        this.bikeProviderManager = bikeProviderManager;
        this.userManager = userManager;
        this.bookings = new HashMap<>();
    }

    public Collection<Quote> getQuotes(Query query) {
        assert query != null;
        // we assume that the rent period is at least one day
        assert query.getStartDate().isBefore(query.getEndDate());
        assert query.getBikes().size() > 0;

        return bikeProviderManager.getQuotes(
            query.getBikes(),
            new DateRange(query.getStartDate(), query.getEndDate()),
            query.getLocation()
        );
    }

    public Booking makeBooking(int userId, Quote quote, BookingDetails bookingDetails) {
        assert quote != null;
        assert bookingDetails != null;

        if (!bookingDetails.isConsentConfirmation()) {
            return null;
        }

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

        Location bikeProviderLocation = bikeProviderManager.getBikeProviderLocation(quote.getBikeProviderID());
        if (bookingDetails.getDeliveryAddress() != null) {
            deliveryService.scheduleDelivery(booking, bikeProviderLocation, bookingDetails.getDeliveryAddress(), quote.getDateRange().getStart());
        }

        if (bookingDetails.getReturnShopID() != quote.getBikeProviderID()) {
            Location returnShopLocation = bikeProviderManager.getBikeProviderLocation(bookingDetails.getReturnShopID());
            deliveryService.scheduleDelivery(booking, returnShopLocation, bikeProviderLocation, quote.getDateRange().getStart());
        }

        bookings.put(booking.getOrderID(), booking);
        userManager.addBooking(userId, booking.getOrderID());

        return booking;
    }

    public void updateBikesStatuses(int bikeProviderID, Collection<Integer> orderedBikesIDs, BookingStatus bookingStatus) {
        bikeProviderManager.updateBikesStatuses(bikeProviderID, orderedBikesIDs, bookingStatus);
    }

    public void returnOrder(int orderID) {
        Booking booking = bookings.get(orderID);

        // bikes status processing
        Collection<Integer> bikeIDs= booking.getOrderedBikesIDs();
        int returnShopID = booking.getReturnShopID();
        int ownerShopID = booking.getBikeProviderID();
        bikeProviderManager.returnBikes(returnShopID, bikeIDs, ownerShopID);

        // return deposit
        booking.returnDeposit();

        // booking update
        booking.setBookingStatus(
            returnShopID == ownerShopID ? BookingStatus.COMPLETED : BookingStatus.COMPLETED_PARTNER
        );
    }
}
