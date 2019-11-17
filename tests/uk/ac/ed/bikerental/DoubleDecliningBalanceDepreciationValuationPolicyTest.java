package uk.ac.ed.bikerental;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.junit.jupiter.api.Test;

class DoubleDecliningBalanceDepreciationValuationPolicyTest {
	
	@Test
	void testRentedBeforeProduction() {
		LocalDate date = LocalDate.of(2010, 1, 1);
		Bike bike = new Bike(null, 1, BigDecimal.valueOf(900.0), null, LocalDate.of(2018, 4, 5));
		DoubleDecliningBalanceDepreciationValuationPolicy valuationPolicy = new DoubleDecliningBalanceDepreciationValuationPolicy();
		BigDecimal value = valuationPolicy.calculateValue(bike, date);
		
		assertEquals(BigDecimal.valueOf(-1).stripTrailingZeros(), value.stripTrailingZeros());
	}

//	@Test
//	void test() {
//		DoubleDecliningBalanceDepreciationValuationPolicy valuationPolicy = new DoubleDecliningBalanceDepreciationValuationPolicy();
//		Bike testBike = new Bike();
//		LocalDate testLocalDate = new
//		
//		BigDecimal resultBigDecimal = valuationPolicy.calculateValue(bike, date);
//	}

}
