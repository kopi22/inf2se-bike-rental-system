package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

// TODO: IMPLEMENT

public class BookingSystem {

    private static int nextBookingId = 1;
    private Map<Integer, Booking> bookings;
    private CustomerManager customerManager;
    private BikeProviderManager bikeProviderManager;
    private DeliveryService deliveryService;

    private Booking makeBooking(int customerId, int bikeProviderId,
        BigDecimal deposit, BigDecimal price,
        DateRange dateRange, List<Integer> bikesIds,
        Location deliveryAddress, int returnShopId
    ) {
        return null;
    }

}
