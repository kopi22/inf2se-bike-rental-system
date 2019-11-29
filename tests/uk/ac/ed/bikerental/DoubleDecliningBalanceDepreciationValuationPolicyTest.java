package uk.ac.ed.bikerental;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DoubleDecliningBalanceDepreciationValuationPolicyTest {

  private ValuationPolicy valuationPolicy;
  private Bike bike;


  //setup environment
  @BeforeEach
  void setUp() throws Exception {
    BikeType.addType("BIKE", BigDecimal.valueOf(900.0));
    BikeType bikeType = BikeType.getBikeType("BIKE");

    valuationPolicy = new DoubleDecliningBalanceDepreciationValuationPolicy(BigDecimal.valueOf(0.1));
    bike = new Bike(bikeType,
        1,
        LocalDate.of(2015, 4, 5));
  }

  //test whether it is possible to calculate deposit before bike was produced -> should return null
  @Test
  void testDepositBeforeBikeProduction() {
    LocalDate date = LocalDate.of(2010, 1, 1);
    BigDecimal value = valuationPolicy.calculateValue(bike, date);

    assertNull(value);
  }

  //test whether deposit is calculated correctly, compare with expected output 
  @Test
  void testDepositCalculation() {
    LocalDate date = LocalDate.of(2018, 9, 1);
    BigDecimal value = valuationPolicy.calculateValue(bike, date);

    assertEquals(BigDecimal.valueOf(460.8).stripTrailingZeros(), value.stripTrailingZeros());
  }

  // test whether the calculateValue throws an error if no Bike object is provided
  @Test
  void testNoBikeProvided() {
    LocalDate date = LocalDate.of(2018, 9, 1);
    assertThrows(AssertionError.class, () -> {valuationPolicy.calculateValue(null, date);});
  }

  @Test
  
  //test whether the calculateValue throws an error in no date is provided
  void testNoDateProvided() {
    assertThrows(AssertionError.class, () -> {valuationPolicy.calculateValue(bike, null);});
  }
}
