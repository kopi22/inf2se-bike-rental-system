package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;

public class Booking implements Deliverable {
    private static int nextId = 1;

    private Controller controller;

    private int bikeProviderID;
    private int orderID;
    private Collection<Integer> orderedBikesIDs;
    private DateRange dateRange;
    private int returnShopID;
    private Location deliveryAddress;
    private BigDecimal amountPaid;
    private LocalDateTime timestamp;
    private BigDecimal deposit;
    private DepositStatus depositStatus;
    private BookingStatus bookingStatus;

    public Booking(Controller controller, int bikeProviderID, Collection<Integer> orderedBikesIDs,
        DateRange dateRange, int returnShopID, Location deliveryAddress, BigDecimal amountPaid,
        BigDecimal deposit) {
        this.controller = controller;
        this.orderID = nextId++;
        this.bikeProviderID = bikeProviderID;
        this.orderedBikesIDs = orderedBikesIDs;
        this.dateRange = dateRange;
        this.returnShopID = returnShopID;
        this.deliveryAddress = deliveryAddress;
        this.amountPaid = amountPaid;
        this.deposit = deposit;
        this.depositStatus = DepositStatus.NOT_PAID;
        this.bookingStatus = BookingStatus.BOOKED;
    }

    @Override
    public void onPickup() {
        switch (bookingStatus) {
            case BOOKED:
                bookingStatus = BookingStatus.IN_DELIVERY;
                break;
            case COMPLETED_PARTNER:
                bookingStatus = BookingStatus.IN_TRANSITION;
                break;
            default:
                throw new UnsupportedStatusChangeException(bookingStatus.toString());
        }
        controller.updateBikesStatuses(bikeProviderID, orderedBikesIDs, bookingStatus);
    }

    @Override
    public void onDropoff() {
        switch (bookingStatus) {
            case IN_DELIVERY:
                bookingStatus = BookingStatus.IN_PROGRESS;
                depositStatus = DepositStatus.PAID;
                break;
            case IN_TRANSITION:
                bookingStatus = BookingStatus.COMPLETED;
                break;
            default:
                throw new UnsupportedStatusChangeException(bookingStatus.toString());
        }
        controller.updateBikesStatuses(bikeProviderID, orderedBikesIDs, bookingStatus);
    }
}
