package uk.ac.ed.bikerental;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TestDateRange {
    private DateRange dateRange1, dateRange2, dateRange3;

    @BeforeEach
    void setUp() throws Exception {
        // Setup resources before each test
        this.dateRange1 = new DateRange(LocalDate.of(2019, 1, 7),
                LocalDate.of(2019, 1, 10));
        this.dateRange2 = new DateRange(LocalDate.of(2019, 1, 5),
                LocalDate.of(2019, 1, 23));
        this.dateRange3 = new DateRange(LocalDate.of(2015, 1, 7),
                LocalDate.of(2018, 1, 10));
    }

    // Sample JUnit tests checking toYears works
    @Test
    void testToYears1() {
        assertEquals(0, this.dateRange1.toYears());
    }

    @Test
    void testToYears3() {
        assertEquals(3, this.dateRange3.toYears());
    }

    @Test
    void testOverlapsTrue() {
    	oneSidedOverlap();
    	containedOverlap();
    }

    @Test
	void testOverlapsFalse() {
		LocalDate startA = LocalDate.of(2019, 3, 27);
		LocalDate endA = LocalDate.of(2019, 4, 27);
		DateRange rangeA = new DateRange(startA, endA);
		
		LocalDate startB = LocalDate.of(2019, 4, 28);
		LocalDate endB = LocalDate.of(2019, 6, 27);
		DateRange rangeB = new DateRange(startB, endB);
		
		LocalDate startC = LocalDate.of(2006, 4, 28);
		LocalDate endC = LocalDate.of(2006, 6, 27);
		DateRange rangeC = new DateRange(startC, endC);
		
		assertFalse(rangeA.overlaps(rangeB));
		assertFalse(rangeB.overlaps(rangeA));
		assertFalse(rangeA.overlaps(rangeC));
		assertFalse(rangeB.overlaps(rangeC));
		assertFalse(rangeC.overlaps(rangeB));
		assertFalse(rangeC.overlaps(rangeA));
		
	}
	
	
	void containedOverlap() {
		LocalDate startA = LocalDate.of(2018, 1, 1);
		LocalDate endA = LocalDate.of(2019, 1, 1);
		DateRange rangeA = new DateRange(startA, endA);
		
		LocalDate startB = LocalDate.of(2018, 4, 28);
		LocalDate endB = LocalDate.of(2018, 6, 27);
		DateRange rangeB = new DateRange(startB, endB);
		
		// LocalDate startC = LocalDate.of(2018, 5, 1);
		// LocalDate endC = LocalDate.of(2018, 5, 30);
		// DateRange rangeC = new DateRange(startC, endC);
		
		assertTrue(rangeA.overlaps(rangeB));
		assertTrue(rangeB.overlaps(rangeA));
		assertTrue(rangeA.overlaps(rangeA));
	}
	
	
	void oneSidedOverlap() {
		LocalDate startA = LocalDate.of(2019, 3, 27);
		LocalDate endA = LocalDate.of(2019, 4, 27);
		DateRange rangeA = new DateRange(startA, endA);
		
		LocalDate startB = LocalDate.of(2019, 4, 27);
		LocalDate endB = LocalDate.of(2019, 6, 27);
		DateRange rangeB = new DateRange(startB, endB);
		
		LocalDate startC = LocalDate.of(2019, 4, 26);
		LocalDate endC = LocalDate.of(2019, 6, 27);
		DateRange rangeC = new DateRange(startC, endC);
		
		LocalDate startD = LocalDate.of(2019, 2, 28);
		LocalDate endD = LocalDate.of(2019, 3, 27);
		DateRange rangeD = new DateRange(startD, endD);
		
		LocalDate startE = LocalDate.of(2019, 2, 28);
		LocalDate endE = LocalDate.of(2019, 3, 28);
		DateRange rangeE = new DateRange(startE, endE);
		
		assertTrue(rangeA.overlaps(rangeB));
		assertTrue(rangeB.overlaps(rangeA));
		assertTrue(rangeA.overlaps(rangeC));
		assertTrue(rangeC.overlaps(rangeA));
		assertTrue(rangeA.overlaps(rangeD));
		assertTrue(rangeD.overlaps(rangeA));
		assertTrue(rangeA.overlaps(rangeE));
		assertTrue(rangeE.overlaps(rangeA));
		
		
		
	}
}
