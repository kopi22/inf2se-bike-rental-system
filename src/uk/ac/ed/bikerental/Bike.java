package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Bike {
    static private int nextID = 1;

    private BikeType type;
    private int bikeId;
    private int ownerId;


    private BikeStatus status;
    private List<Location> storeLocations;
    private List<DateRange> reservationDates;
    private LocalDate productionDate;


    public Bike(BikeType type, int ownerId, LocalDate productionDate) {
        bikeId = nextID++;
        this.type = type;
        this.ownerId = ownerId;
        this.status = BikeStatus.IN_STORE;
        this.productionDate = productionDate;
        this.reservationDates = new ArrayList<>();
        this.storeLocations = new ArrayList<>();
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
        reservationDates.add(dateRange);
    }

    public boolean isAvailable(DateRange dateRange) {
        return reservationDates.stream().noneMatch(reservationDateRange -> reservationDateRange.overlaps(dateRange));
    }

    public void returnedToShop() {

    }

    public void returnedToPartner(Location locationOfPartner) {

    }

    public int getBikeId() {
        return bikeId;
    }

    public void setStatus(BikeStatus status) {
        this.status = status;
    }
}