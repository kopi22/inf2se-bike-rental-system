package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class Bike {
    static private int nextID = 1;

    private BikeType type;
    private int bikeId;
    private int ownerId;


    private BikeStatus status;
    private List<Integer> locationHistory; // ids of bikeShops in which bike was present
    private TreeMap<LocalDate, LocalDate> reservationDates;
    private LocalDate productionDate;


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
        LocalDate previousBookingEnd = reservationDates.lowerEntry(bookingStart).getValue();

        boolean endsBeforeNextBooking =
            nextBookingStart == null || bookingEnd.isBefore(nextBookingStart);
        boolean startAfterPreviousBooking =
            previousBookingEnd == null || bookingStart.isAfter(previousBookingEnd);

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
    
   /* //bike returned to a partner
    public void returnedToPartner(Location partnerAddress) {
    	this.setStatus(BikeStatus.RETURNED_PARTNER);
    	
    }*/
}