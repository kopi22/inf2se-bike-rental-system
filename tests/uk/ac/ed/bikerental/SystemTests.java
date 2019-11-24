package uk.ac.ed.bikerental;

import java.util.Collection;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

public class SystemTests {
    // You can add attributes here

    Controller controller;

    @BeforeEach
    void setUp() throws Exception {
        // Setup mock delivery service before each tests
        DeliveryServiceFactory.setupMockDeliveryService();
        
        // Put your test setup here

        BikeType.addType("Bike1", BigDecimal.valueOf(1000.0));
        BikeType.addType("Bike2", BigDecimal.valueOf(1400.0));
        BikeType.addType("Bike3", BigDecimal.valueOf(800.0));

        BikeType bikeType1, bikeType2, bikeType3;
        bikeType1 = BikeType.getBikeType("Bike1");
        bikeType2 = BikeType.getBikeType("Bike2");
        bikeType3 = BikeType.getBikeType("Bike3");

        BikeProviderManager bikeProviderManager = new BikeProviderManager();

        BikeProvider bikeProviderA = new BikeProvider(
            "",
            new Location("EH165AJ", ""),
            "",
            new HashMap<String, String>(),
            BigDecimal.valueOf(0.1)
        );
        bikeProviderManager.addBikeProvider(bikeProviderA);
        bikeProviderA.addBike(new Bike (bikeType1, bikeProviderA.getId(), LocalDate.of(2015, 5, 19)));
        bikeProviderA.addBike(new Bike (bikeType2, bikeProviderA.getId(), LocalDate.of(2013, 5, 19)));
        bikeProviderA.addBike(new Bike (bikeType2, bikeProviderA.getId(), LocalDate.of(2011, 5, 19)));
        bikeProviderA.addBike(new Bike (bikeType3, bikeProviderA.getId(), LocalDate.of(2018, 5, 19)));
        bikeProviderA.addBike(new Bike (bikeType3, bikeProviderA.getId(), LocalDate.of(2017, 5, 19)));
        bikeProviderA.addBike(new Bike (bikeType3, bikeProviderA.getId(), LocalDate.of(2014, 5, 19)));
        bikeProviderA.setRentalPriec(bikeType1, BigDecimal.valueOf(50.0));
        bikeProviderA.setRentalPriec(bikeType2, BigDecimal.valueOf(40.0));
        bikeProviderA.setRentalPriec(bikeType3, BigDecimal.valueOf(30.0));

        controller = new Controller(DeliveryServiceFactory.getDeliveryService(), bikeProviderManager);
    }
    
    // TODO: Write system tests covering the three main use cases

    @Test
    void testOutsideRange() {
        Collection<Quote> quotes = controller.getQuotes(new Query(
            new HashMap<>() {{
                put("Bike2", 1);
            }},
            LocalDate.of(2010, 6, 1),
            LocalDate.of(2010, 6, 3),
            new Location("G12312", "")
        ));
        assertTrue(quotes.isEmpty());
    }
    @Test
    void testIsideRangeOneBikeType() {
        Collection<Quote> quotes = controller.getQuotes(new Query(
            new HashMap<>() {{
                put("Bike2", 1);
            }},
            LocalDate.of(2010, 6, 1),
            LocalDate.of(2010, 6, 3),
            new Location("EH165AY", "")
        ));
        assertTrue(quotes.size() == 1);
        assertEquals(
            BigDecimal.valueOf(80.0).stripTrailingZeros(), (
            (Quote)(quotes.toArray()[0])).getPrice().stripTrailingZeros()
        );
        assertEquals(
            BigDecimal.valueOf(140.0).stripTrailingZeros(), (
                (Quote)(quotes.toArray()[0])).getDeposit().stripTrailingZeros()
        );
    }
    @Test
    void testIsideRangeOneTwoTypes() {
        Collection<Quote> quotes = controller.getQuotes(new Query(
            new HashMap<>() {{
                put("Bike2", 1);
                put("Bike3", 2);
            }},
            LocalDate.of(2010, 6, 1),
            LocalDate.of(2010, 6, 3),
            new Location("EH165AY", "")
        ));
        assertTrue(quotes.size() == 1);
        assertEquals(
            BigDecimal.valueOf(200.0).stripTrailingZeros(), (
                (Quote)(quotes.toArray()[0])).getPrice().stripTrailingZeros()
        );
        assertEquals(
            BigDecimal.valueOf(300.0).stripTrailingZeros(), (
                (Quote)(quotes.toArray()[0])).getDeposit().stripTrailingZeros()
        );
    }
}
