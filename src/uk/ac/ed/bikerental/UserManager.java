package uk.ac.ed.bikerental;

import java.util.HashMap;
import java.util.Map;

public class UserManager {
    private final Map<Integer, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public void addBooking(int userId, int bookingId) {
        assert users.containsKey(userId);

        users.get(userId).addBooking(bookingId);
    }
}
