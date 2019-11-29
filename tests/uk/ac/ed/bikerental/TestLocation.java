package uk.ac.ed.bikerental;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

class TestLocation {
  Location locationA, locationB, locationC, locationD, locationE, locationF;

  //setup environment
  @BeforeEach
  void setUp() throws Exception {
    locationA = new Location("EH165AJ", "");
    locationB = new Location("EH185A1", "");
    locationC = new Location("EH265AJ", "");

    locationD = new Location("G12123", "");
    locationE = new Location("G99987", "");
    locationF = new Location("H87609", "");
  }

  //test whether isNearTo function outputs expected results when 2 locations are near
  @Test
  void isNearTo() {
    // Tests symmetry
    assertTrue(locationA.isNearTo(locationB));

    // Tests transitivity
    assertTrue(locationB.isNearTo(locationC));
    assertTrue(locationA.isNearTo(locationC));

    // Tests reflexivity
    assertTrue(locationA.isNearTo(locationA));
  }

  // test whether isNearTo function outputs expected results when 2 locations are not near
  @Test
  void isNotNearTo() {
    assertFalse(locationA.isNearTo(locationD));
    assertFalse(locationD.isNearTo(locationA));

    assertFalse(locationE.isNearTo(locationD));
    assertFalse(locationD.isNearTo(locationE));

    assertFalse(locationB.isNearTo(locationF));
    assertFalse(locationF.isNearTo(locationB));
  }
}
