package uk.ac.ed.bikerental;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class SystemTests {

    // attributes used for testing
    Controller controller;
    BikeProviderManager bikeProviderManager;
    UserManager userManager;
    BikeType bikeType1, bikeType2, bikeType3;
    BikeProvider bikeProvider1, bikeProvider2;
    Bike bike1, bike2, bike3;
    Map<String, Integer> bikesInQueryMap = new HashMap<>();
    Query query1;
    User user1, user2;

    @BeforeEach
    void setUp() throws Exception {
        // Setup mock delivery service before each tests
        DeliveryServiceFactory.setupMockDeliveryService();


        BikeType.addType("Bike1", new BigDecimal("1000"));
        BikeType.addType("Bike2", new BigDecimal("1400"));
        BikeType.addType("Bike3", new BigDecimal("800"));

        bikeType1 = BikeType.getBikeType("Bike1");
        bikeType2 = BikeType.getBikeType("Bike2");
        bikeType3 = BikeType.getBikeType("Bike3");

        bikeProviderManager = new BikeProviderManager();
        userManager = new UserManager();


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
            new LinearDepreciationValuationPolicy(new BigDecimal("0.1")));
        bikeProvider2 = new BikeProvider("SportyBikes", new Location("EH189TZ", "Hughs"), "0918822108", null,
            new DoubleDecliningBalanceDepreciationValuationPolicy(new BigDecimal("0.1")));
        bike1 = new Bike(bikeType1, bikeProvider1.getId(), LocalDate.of(2015, 3, 27));
        bike2 = new Bike(bikeType2, bikeProvider1.getId(), LocalDate.of(2012, 6, 2));
        bikeProvider1.addBike(bike1);
        bikeProvider1.addBike(bike2);
        bikeProvider1.setRentalPrice(bikeType1, BigDecimal.valueOf(100));
        bikeProvider1.setRentalPrice(bikeType2, BigDecimal.valueOf(200));

        bike3 = new Bike(bikeType3, bikeProvider2.getId(), LocalDate.of(2014, 1,2));
        bikeProvider2.addBike(bike3);
        bikeProvider2.setRentalPrice(bikeType3, BigDecimal.valueOf(300));
        bikeProviderManager.addBikeProvider(bikeProvider1);
        bikeProviderManager.addBikeProvider(bikeProvider2);

        query1 = new Query(new HashMap<>(),
            LocalDate.of(2019, 6, 28),
            LocalDate.of(2019, 7, 28),
            new Location("EH254GU", "St Leonards")
        );

        user1 = new User("henryk@gmail.com",
            "Henryk",
            "Hobohobo",
            new Location("EH165AJ", "Akababa"),
            "09875456796");
        user2 = new User("Alica@gmail.com",
            "Alica",
            "Persey",
            new Location("EH876TZ", "Mayeston"),
            "0905666080");

        userManager.addUser(user1);
        userManager.addUser(user2);

        DateRange bookedRange = new DateRange(LocalDate.of(2015, 1, 19), LocalDate.of(2015, 2, 19));
        Bike bikeNotAvailable = new Bike(bikeType1, bikeProviderA.getId(), LocalDate.of(2015, 10, 19));
        bikeProviderA.addBike(bikeNotAvailable);
        bikeNotAvailable.book(bookedRange);

        controller = new Controller(DeliveryServiceFactory.getDeliveryService(), bikeProviderManager, userManager);
    }

    @Nested
    @DisplayName("Tests for Get Quotes Use Case")
    class GetQuotesUseCase {

        @Test
        void testOutsideRange() {
            Collection<Quote> quotes = controller.getQuotes(new Query(new HashMap<>() {{
                    put("Bike2", 1);
                }},
                LocalDate.of(2020, 6, 1),
                LocalDate.of(2020, 6, 3),
                new Location("G12312", ""))
            );

            assertTrue(quotes.isEmpty());
        }

        @Test
        void testInsideRangeOneBikeType() {
            Collection<Quote> quotes = controller.getQuotes(new Query(new HashMap<>() {{
                    put("Bike3", 3);
                }},
                LocalDate.of(2020, 6, 1),
                LocalDate.of(2020, 6, 4),
                new Location("EH165AY", ""))
            );

            assertEquals(1, quotes.size());
            assertEquals(BigDecimal.valueOf(270.0).stripTrailingZeros(),
                ((Quote) (quotes.toArray()[0])).getPrice().stripTrailingZeros());
            assertEquals(BigDecimal.valueOf(240.0).stripTrailingZeros(),
                ((Quote) (quotes.toArray()[0])).getDeposit().stripTrailingZeros());
        }

        @Test
        void testInsideRangeTwoBikeTypes() {
            Collection<Quote> quotes = controller.getQuotes(new Query(new HashMap<>() {{
                    put("Bike2", 1);
                    put("Bike3", 2);
                }},
                LocalDate.of(2020, 6, 1),
                LocalDate.of(2020, 6, 3),
                new Location("EH165AY", ""))
            );

            assertEquals(1, quotes.size());
            assertEquals(BigDecimal.valueOf(200.0).stripTrailingZeros(),
                ((Quote) (quotes.toArray()[0])).getPrice().stripTrailingZeros());
            assertEquals(BigDecimal.valueOf(300.0).stripTrailingZeros(),
                ((Quote) (quotes.toArray()[0])).getDeposit().stripTrailingZeros());
        }

        @Test
        void noAvailableQuote() {
            Collection<Quote> quotes = controller.getQuotes(new Query(new HashMap<>() {{
                    put("Bike2", 4);
                    put("Bike3", 2);
                }},
                LocalDate.of(2020, 6, 1),
                LocalDate.of(2020, 6, 3),
                new Location("EH165AY", ""))
            );

            assertEquals(0, quotes.size());
        }
    }

    @Nested
    @DisplayName("Testing Getting Quotes with Different Valuation Policies")
    class QuotesWithCustomValuationPolicies {

        Controller controller;
        BikeProviderManager bikeProviderManager;
        UserManager userManager;

        @BeforeEach
        void setUp() {
            bikeProviderManager = new BikeProviderManager();
            userManager = new UserManager();

            bikeProviderManager.addBikeProvider(bikeProvider1);
            bikeProviderManager.addBikeProvider(bikeProvider2);

            controller = new Controller(
                DeliveryServiceFactory.getDeliveryService(),
                bikeProviderManager,
                userManager
            );
        }

        @Test
        void testQuotesWithLinearValuationPolicy() {
            Collection<Quote> quotes = controller.getQuotes(new Query(new HashMap<>() {{
                    put("Bike1", 1);
                }},
                LocalDate.of(2020, 6, 1),
                LocalDate.of(2020, 6, 4),
                new Location("EH165AY", ""))
            );

            assertEquals(1, quotes.size());
            assertEquals(BigDecimal.valueOf(500).stripTrailingZeros(),
                ((Quote) (quotes.toArray()[0])).getDeposit().stripTrailingZeros());
        }

        @Test
        void testQuotesWithDoubleDecliningValuationPolicy() {
            Collection<Quote> quotes = controller.getQuotes(new Query(new HashMap<>() {{
                    put("Bike3", 1);
                }},
                    LocalDate.of(2020, 6, 1),
                    LocalDate.of(2020, 6, 4),
                    new Location("EH165AY", ""))
            );

            assertEquals(1, quotes.size());
            assertEquals(new BigDecimal("209.7152").stripTrailingZeros(),
                ((Quote) (quotes.toArray()[0])).getDeposit().stripTrailingZeros());
        }
    }

    @Nested
    @DisplayName("Tests for Make Booking Use Case")
    class MakeBookingUseCase {

        Collection<Integer> bikeIDs;
        Quote quote1;

        @BeforeEach
        void beforeEach() {
            bikeIDs = new ArrayList<>() {{
                add(bike1.getBikeId());
            }};
            quote1 = new Quote(
                bikeProvider1.getId(),
                bike1.getReplacementValue(),
                new BigDecimal(120),
                new DateRange(query1.getStartDate(), query1.getEndDate()),
                bikeIDs);
        }

        @Test
        void makeBookingTest() {
            // data for creating booking
            BookingDetails details1 = new BookingDetails(user1.getAddressOfCustomer(), bikeProvider1.getId(), true);

            Booking actual = controller.makeBooking(user1.getUserId(), quote1, details1);
            Booking expected = new Booking(controller, bikeProvider1.getId(), bikeIDs,
                new DateRange(query1.getStartDate(), query1.getEndDate()), bikeProvider1.getId(),
                user1.getAddressOfCustomer(), new BigDecimal(120), bike1.getReplacementValue());
            assertEquals(expected.getBikeProviderID(), actual.getBikeProviderID());
            assertEquals(expected.getReturnShopID(), actual.getReturnShopID());
            assertEquals(expected.getAmountPaid(), actual.getAmountPaid());
            assertEquals(expected.getDeposit(), actual.getDeposit());
            assertEquals(expected.getDeliveryAddress(), actual.getDeliveryAddress());
            assertEquals(expected.getDateRange(), actual.getDateRange());
            assertEquals(DepositStatus.NOT_PAID, actual.getDepositStatus());
            assertEquals(BookingStatus.BOOKED, actual.getBookingStatus());
            assertArrayEquals(expected.getOrderedBikesIDs().toArray(), actual.getOrderedBikesIDs().toArray());
        }

        @Test
        void makeBookingWithoutConsentTest() {
            // data for creating booking
            BookingDetails details1 = new BookingDetails(user1.getAddressOfCustomer(), bikeProvider1.getId(), false);

            Booking booking = controller.makeBooking(user1.getUserId(), quote1, details1);
            assertNull(booking);
        }

        @Test
        void dontLetBookSameQuoteTwice() {
            BookingDetails details1 = new BookingDetails(user1.getAddressOfCustomer(), bikeProvider1.getId(), false);

            controller.makeBooking(user1.getUserId(), quote1, details1);
            assertNull(controller.makeBooking(user1.getUserId(), quote1, details1));
        }
    }

    @Nested
    @DisplayName("Tests for Return Bike Use Case")
    class ReturnBikeUseCase {

        Quote quote;
        BikeProvider bikeProviderA, bikeProviderB, bikeProviderC;
        Bike bike1, bike2;
        User user1;

        @BeforeEach
        void beforeEach() throws Exception {
            user1 = new User("", "", "", new Location("EH17865", ""), "");
            userManager.addUser(user1);

            bikeProviderA = new BikeProvider("", new Location("EH165AJ", ""), "",
                new HashMap<>(), BigDecimal.valueOf(0.1));
            bikeProviderA.setRentalPrice(BikeType.getBikeType("Bike1"), BigDecimal.valueOf(50));
            bikeProviderA.setRentalPrice(BikeType.getBikeType("Bike2"), BigDecimal.valueOf(70));
            bike1 = new Bike(BikeType.getBikeType("Bike1"), bikeProviderA.getId(), LocalDate.of(2007, 3, 27));
            bike2 = new Bike(BikeType.getBikeType("Bike2"), bikeProviderA.getId(), LocalDate.of(2009, 3, 27));
            bikeProviderA.addBike(bike1);
            bikeProviderA.addBike(bike2);

            bikeProviderB = new BikeProvider("", new Location("EH165AJ", ""), "",
                new HashMap<>(), BigDecimal.valueOf(0.15));

            bikeProviderC = new BikeProvider("", new Location("EH165AJ", ""), "",
                new HashMap<>(), BigDecimal.valueOf(0.2));

            bikeProviderA.addPartner(bikeProviderB.getId());
            bikeProviderB.addPartner(bikeProviderA.getId());

            quote = new Quote(bikeProviderA.getId(), BigDecimal.TEN, BigDecimal.ONE,
                new DateRange(LocalDate.now(), LocalDate.now().plusDays(10)),
                new HashSet<>() {{
                    add(bike1.getBikeId());
                    add(bike2.getBikeId());
                }});

            bikeProviderManager.addBikeProvider(bikeProviderA);
            bikeProviderManager.addBikeProvider(bikeProviderB);
            bikeProviderManager.addBikeProvider(bikeProviderC);
        }


        @Test
        void returnBikeToOriginalProvider() {
            Booking booking = controller.makeBooking(user1.getUserId(), quote, new BookingDetails(
                null, bikeProviderA.getId(), true));

            bike1.setStatus(BikeStatus.RENTED);
            bike2.setStatus(BikeStatus.RENTED);
            controller.returnOrder(booking.getOrderID());
            assertEquals(BikeStatus.IN_STORE, bike1.getStatus());
            assertEquals(BikeStatus.IN_STORE, bike2.getStatus());
            assertEquals(DepositStatus.RETURNED, booking.getDepositStatus());
            assertEquals(BookingStatus.COMPLETED, booking.getBookingStatus());
        }

        @Test
        void returnBikeToPartner() {
            Booking booking = controller.makeBooking(user1.getUserId(), quote, new BookingDetails(
                null, bikeProviderB.getId(), true));

            bike1.setStatus(BikeStatus.RENTED);
            bike2.setStatus(BikeStatus.RENTED);
            controller.returnOrder(booking.getOrderID());
            assertEquals(BikeStatus.RETURNED_PARTNER, bike1.getStatus());
            assertEquals(BikeStatus.RETURNED_PARTNER, bike2.getStatus());
            assertEquals(DepositStatus.RETURNED, booking.getDepositStatus());
            assertEquals(BookingStatus.COMPLETED_PARTNER, booking.getBookingStatus());
        }

        @Test
        void returnBikeToNotPartner() {
            Booking booking = controller.makeBooking(user1.getUserId(), quote, new BookingDetails(
                null, bikeProviderC.getId(), true));
            bike1.setStatus(BikeStatus.RENTED);
            bike2.setStatus(BikeStatus.RENTED);
            assertThrows(CannotReturnToNotPartnerException.class, () -> controller.returnOrder(booking.getOrderID()));
        }
    }

}
