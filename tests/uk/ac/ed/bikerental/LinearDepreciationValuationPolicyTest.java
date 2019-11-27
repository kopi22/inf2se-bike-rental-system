package uk.ac.ed.bikerental;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LinearDepreciationValuationPolicyTest {

  private BikeType bikeType;
  private ValuationPolicy valuationPolicy;
  private Bike bike;


  @BeforeEach
  void setUp() throws Exception {
    BikeType.addType("BIKE", BigDecimal.valueOf(900.0));
    BikeType bikeType = BikeType.getBikeType("BIKE");

    valuationPolicy = new LinearDepreciationValuationPolicy(BigDecimal.valueOf(0.1));
    bike = new Bike(bikeType,
        1,
        LocalDate.of(2015, 4, 5));
  }

  @Test
  void testDepositBeforeBikeProduction() {
    LocalDate date = LocalDate.of(2010, 1, 1);
    BigDecimal value = valuationPolicy.calculateValue(bike, date);

    assertNull(value);
  }

 	@Test
	void testDepositCalculation() {
    LocalDate date = LocalDate.of(2018, 9, 1);
    BigDecimal value = valuationPolicy.calculateValue(bike, date);

    assertEquals(BigDecimal.valueOf(630.0).stripTrailingZeros(), value.stripTrailingZeros());
	}

  @Test
  void testNoBikeProvided() {
    LocalDate date = LocalDate.of(2018, 9, 1);
    assertThrows(AssertionError.class, () -> valuationPolicy.calculateValue(null, date));
  }

  @Test
  void testNoDateProvided() {
    assertThrows(AssertionError.class, () -> valuationPolicy.calculateValue(bike, null));
  }
}