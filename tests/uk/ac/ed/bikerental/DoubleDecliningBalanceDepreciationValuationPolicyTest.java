package uk.ac.ed.bikerental;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DoubleDecliningBalanceDepreciationValuationPolicyTest {

  private ValuationPolicy valuationPolicy;
  private Bike bike;


  @BeforeEach
  //setup environment
  void setUp() throws Exception {
    BikeType.addType("BIKE", BigDecimal.valueOf(900.0));
    BikeType bikeType = BikeType.getBikeType("BIKE");

    valuationPolicy = new DoubleDecliningBalanceDepreciationValuationPolicy(BigDecimal.valueOf(0.1));
    bike = new Bike(bikeType,
        1,
        LocalDate.of(2015, 4, 5));
  }

  @Test
  //test that calculating deposit for a bike with a date before bike's production date returns null
  void testDepositBeforeBikeProduction() {
    LocalDate date = LocalDate.of(2010, 1, 1);
    BigDecimal value = valuationPolicy.calculateValue(bike, date);

    assertNull(value);
  }

  @Test
  //test that deposit is calculated as expected by the formula
  void testDepositCalculation() {
    LocalDate date = LocalDate.of(2018, 9, 1);
    BigDecimal value = valuationPolicy.calculateValue(bike, date);

    assertEquals(BigDecimal.valueOf(460.8).stripTrailingZeros(), value.stripTrailingZeros());
  }

  @Test
  //test that if no bike is provided as an argument to calculateValue function then exception is thrown
  void testNoBikeProvided() {
    LocalDate date = LocalDate.of(2018, 9, 1);
    assertThrows(AssertionError.class, () -> valuationPolicy.calculateValue(null, date));
  }

  @Test
  //test that if no date is provided as an argument to calculateValue function then exception is thrown
  void testNoDateProvided() {
    assertThrows(AssertionError.class, () -> valuationPolicy.calculateValue(bike, null));
  }
}
