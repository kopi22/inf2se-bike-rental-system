package uk.ac.ed.bikerental;

import java.util.Collection;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SystemTests {

    // attributes used for testing
    Controller controller;
    BikeProvider bikeProvider1;
    BikeProvider bikeProvider2;
    Bike bike1;
    Bike bike2;
    Map<String, Integer> bikesInQueryMap = new HashMap<>();
    Query query1;
    User user1;
    User user2;

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

        BikeProvider bikeProviderA = new BikeProvider("", new Location("EH165AJ", ""), "",
            new HashMap<>(), BigDecimal.valueOf(0.1));
        bikeProviderManager.addBikeProvider(bikeProviderA);
        bikeProviderA.addBike(new Bike(bikeType1, bikeProviderA.getId(), LocalDate.of(2015, 5, 19)));
        bikeProviderA.addBike(new Bike(bikeType2, bikeProviderA.getId(), LocalDate.of(2013, 5, 19)));
        bikeProviderA.addBike(new Bike(bikeType2, bikeProviderA.getId(), LocalDate.of(2011, 5, 19)));
        bikeProviderA.addBike(new Bike(bikeType3, bikeProviderA.getId(), LocalDate.of(2018, 5, 19)));
        bikeProviderA.addBike(new Bike(bikeType3, bikeProviderA.getId(), LocalDate.of(2017, 5, 19)));
        bikeProviderA.addBike(new Bike(bikeType3, bikeProviderA.getId(), LocalDate.of(2014, 5, 19)));
        bikeProviderA.setRentalPrice(bikeType1, BigDecimal.valueOf(50.0));
        bikeProviderA.setRentalPrice(bikeType2, BigDecimal.valueOf(40.0));
        bikeProviderA.setRentalPrice(bikeType3, BigDecimal.valueOf(30.0));


        bikeProvider1 = new BikeProvider("CoolBikes", new Location("EH165AJ", "Lawsons"), "0914320650", null,
            new BigDecimal(0.87));
        bikeProvider2 = new BikeProvider("SportyBikes", new Location("EH189TZ", "Hughs"), "0918822108", null,
            new BigDecimal(0.95));
        bike1 = new Bike(BikeType.getBikeType("Bike1"), bikeProvider1.getId(), LocalDate.of(2007, 3, 27));
        bike2 = new Bike(BikeType.getBikeType("Bike2"), bikeProvider1.getId(), LocalDate.of(2012, 6, 2));
        bikeProvider1.addBike(bike1);
        bikeProvider1.addBike(bike2);
        bikeProvider1.setRentalPrice(bikeType1, BigDecimal.valueOf(100));
        bikeProvider1.setRentalPrice(bikeType2, BigDecimal.valueOf(200));
        bikeProviderManager.addBikeProvider(bikeProvider1);
        bikeProviderManager.addBikeProvider(bikeProvider2);

        query1 = new Query(new HashMap<>(), LocalDate.of(2019, 6, 28), LocalDate.of(2019, 7, 28),
            new Location("EH254GU", "St Leonards"));
        user1 = new User(1, "henryk@gmail.com", "Henryk", "Hobohobo", new Location("EH165AJ", "Akababa"),
            "09875456796");
        user2 = new User(2, "Alica@gmail.com", "Alica", "Persey", new Location("EH876TZ", "Mayeston"),
            "0905666080");
        UserManager userManager = new UserManager();
        userManager.addUser(user1);
        userManager.addUser(user2);

        DateRange bookedRange = new DateRange(LocalDate.of(2015, 1, 19), LocalDate.of(2015, 2, 19));
        Bike bikeNotAvailable = new Bike(bikeType1, bikeProviderA.getId(), LocalDate.of(2015, 10, 19));
        bikeProviderA.addBike(bikeNotAvailable);
        bikeNotAvailable.book(bookedRange);

        controller = new Controller(DeliveryServiceFactory.getDeliveryService(), bikeProviderManager, userManager);
    }

    // TODO: Write system tests covering the three main use cases

    @Test
    void testOutsideRange() {
        Collection<Quote> quotes = controller.getQuotes(new Query(new HashMap<>() {
            {
                put("Bike2", 1);
            }
        }, LocalDate.of(2010, 6, 1), LocalDate.of(2010, 6, 3), new Location("G12312", "")));
        assertTrue(quotes.isEmpty());
    }

    @Test
    void testInsideRangeOneBikeType() {
        Collection<Quote> quotes = controller.getQuotes(new Query(new HashMap<>() {
            {
                put("Bike2", 1);
            }
        }, LocalDate.of(2010, 6, 1), LocalDate.of(2010, 6, 3), new Location("EH165AY", "")));
        assertEquals(2, quotes.size());
        assertEquals(BigDecimal.valueOf(80.0).stripTrailingZeros(),
            ((Quote) (quotes.toArray()[0])).getPrice().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(140.0).stripTrailingZeros(),
            ((Quote) (quotes.toArray()[0])).getDeposit().stripTrailingZeros());
    }

    @Test
    void testInsideRangeOneTwoTypes() {
        Collection<Quote> quotes = controller.getQuotes(new Query(new HashMap<>() {
            {
                put("Bike2", 1);
                put("Bike3", 2);
            }
        }, LocalDate.of(2010, 6, 1), LocalDate.of(2010, 6, 3), new Location("EH165AY", "")));
        assertEquals(1, quotes.size());
        assertEquals(BigDecimal.valueOf(200.0).stripTrailingZeros(),
            ((Quote) (quotes.toArray()[0])).getPrice().stripTrailingZeros());
        assertEquals(BigDecimal.valueOf(300.0).stripTrailingZeros(),
            ((Quote) (quotes.toArray()[0])).getDeposit().stripTrailingZeros());
    }

    @Test
    void makeBookingTest() {
        BookingDetails details1 = new BookingDetails(user1.getAddressOfCustomer(), bikeProvider1.getId(), true);
        Collection<Integer> bikeIDs = new ArrayList<>();
        bikeIDs.add(bike1.getBikeId());
        Quote quote1 = new Quote(bikeProvider1.getId(), bike1.getReplacementValue(), new BigDecimal(120),
            new DateRange(query1.getStartDate(), query1.getEndDate()), bikeIDs);

        // data for creating booking
        Booking actual = controller.makeBooking(user1.getUserId(), quote1, details1);
        Booking expected = new Booking(controller, bikeProvider1.getId(), bikeIDs,
            new DateRange(query1.getStartDate(), query1.getEndDate()), bikeProvider1.getId(),
            user1.getAddressOfCustomer(), new BigDecimal(120), bike1.getReplacementValue());
        assertEquals(expected.getBikeProviderID(), actual.getBikeProviderID());
        assertEquals(expected.getReturnShopID(), actual.getReturnShopID());
        assertEquals(expected.getAmountPaid(), actual.getAmountPaid());
        assertEquals(expected.getDeposit(), actual.getDeposit());
        assertEquals(expected.getDeliveryAddress(), actual.getDeliveryAddress());
    }

    @Test
    void makeBookingWithoutConsentTest() {
        BookingDetails details1 = new BookingDetails(user1.getAddressOfCustomer(), bikeProvider1.getId(), false);
        Collection<Integer> bikeIDs = new ArrayList<>();
        bikeIDs.add(bike1.getBikeId());
        Quote quote1 = new Quote(bikeProvider1.getId(), bike1.getReplacementValue(), new BigDecimal(120),
            new DateRange(query1.getStartDate(), query1.getEndDate()), bikeIDs);

        // data for creating booking
        Booking booking = controller.makeBooking(user1.getUserId(), quote1, details1);
        assertNull(booking);
    }

    @Test
    void returnBikeToOriginalProvider() {
        bike1.setStatus(BikeStatus.RENTED);
        bike1.returnToShop(bikeProvider1.getId());
        assertEquals(BikeStatus.IN_STORE, bike1.getStatus());
    }

    @Test
    void returnBikeToPartner() {
        bike1.setStatus(BikeStatus.RENTED);
        bikeProvider1.addPartner(bikeProvider2.getId());
        bike1.returnToShop(bikeProvider2.getId());
        assertEquals(BikeStatus.RETURNED_PARTNER, bike1.getStatus());
    }

}
