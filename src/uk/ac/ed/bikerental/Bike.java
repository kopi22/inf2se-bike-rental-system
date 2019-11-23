package uk.ac.ed.bikerental;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Bike {
  static private int nextID = 1;

  private int bikeId;
  private BikeType type;
  private int ownerId;
  private BigDecimal originalValue;
  private BikeStatus status;
  private List<Location> storeLocations;
  private List<DateRange> reservationDates;
  private LocalDate productionDate;


  public Bike(BikeType type, int ownerId, BigDecimal originalValue, BikeStatus status, LocalDate productionDate) {
    bikeId = nextID++;
    BigDecimal depreciationRate;
    this.type = type;
    this.ownerId = ownerId;
    this.originalValue = originalValue;
    this.status = status;
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
}