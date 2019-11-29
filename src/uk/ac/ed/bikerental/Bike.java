package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Bike {
    static private int nextID = 1;

    private final BikeType type;
    private final int bikeId;
    private int ownerId;


    private BikeStatus status;
    private final List<Integer> locationHistory; // ids of bikeShops in which bike was present
    private final TreeMap<LocalDate, LocalDate> reservationDates;
    private final LocalDate productionDate;


    public Bike(BikeType type, int ownerId, LocalDate productionDate) {
        bikeId = nextID++;
        this.type = type;
        this.ownerId = ownerId;
        this.status = BikeStatus.IN_STORE;
        this.productionDate = productionDate;
        this.reservationDates = new TreeMap<>();
        this.locationHistory = new ArrayList<>();
    }

    public BikeStatus getStatus() {
		return status;
	}

	  public String getType() {
        return type.getTypeName();
    }

    public BigDecimal getReplacementValue() {
        return type.getReplacementValue();
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public void book(DateRange dateRange) {
        reservationDates.put(dateRange.getStart(), dateRange.getEnd());
    }

    public boolean isAvailable(DateRange dateRange) {
        LocalDate bookingStart =  dateRange.getStart();
        LocalDate bookingEnd = dateRange.getEnd();
        LocalDate nextBookingStart = reservationDates.ceilingKey(bookingStart);
        Entry<LocalDate, LocalDate> previousBookingEnd = reservationDates.lowerEntry(bookingStart);

        boolean endsBeforeNextBooking =
            nextBookingStart == null || bookingEnd.isBefore(nextBookingStart);
        boolean startAfterPreviousBooking =
            previousBookingEnd == null || bookingStart.isAfter(previousBookingEnd.getValue());

        return startAfterPreviousBooking && endsBeforeNextBooking;
    }

    public int getBikeId() {
        return bikeId;
    }

    public void setStatus(BikeStatus status) {
        this.status = status;
    }

    public void returnToShop(int returnShopId) {
        locationHistory.add(returnShopId);
        if (returnShopId == ownerId) {
            status = BikeStatus.IN_STORE;
        } else {
            status = BikeStatus.RETURNED_PARTNER;
        }
    }
}