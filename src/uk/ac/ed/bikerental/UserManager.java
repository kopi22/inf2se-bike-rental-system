package uk.ac.ed.bikerental;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
	
	
    private final Map<Integer, User> users = new HashMap<>(); //map of users UserID->User

    //add User to database
    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    //add Booking with provided bookingId to User with provided userId
    public void addBooking(int userId, int bookingId) {
        assert users.containsKey(userId);

        users.get(userId).addBooking(bookingId);
    }
}
