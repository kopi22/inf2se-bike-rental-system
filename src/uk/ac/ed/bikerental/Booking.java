package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;


public class Booking implements Deliverable {
    private static int nextId = 1;

    private final Controller controller;

    private final int bikeProviderID;
    public BookingStatus getBookingStatus() {
		return bookingStatus;
	}

	public void setBookingStatus(BookingStatus bookingStatus) {
		this.bookingStatus = bookingStatus;
	}

	public int getBikeProviderID() {
		return bikeProviderID;
	}

	public int getOrderID() {
		return orderID;
	}

	public Collection<Integer> getOrderedBikesIDs() {
		return orderedBikesIDs;
	}

	public DateRange getDateRange() {
		return dateRange;
	}

	public int getReturnShopID() {
		return returnShopID;
	}

	public Location getDeliveryAddress() {
		return deliveryAddress;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public BigDecimal getDeposit() {
		return deposit;
	}

	public DepositStatus getDepositStatus() {
		return depositStatus;
	}

	private final int orderID;
    private final Collection<Integer> orderedBikesIDs;
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
    
    public void returnDeposit() {
    	depositStatus = DepositStatus.RETURNED;
    }
}
