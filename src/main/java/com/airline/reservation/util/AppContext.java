package com.airline.reservation.util;

import com.airline.reservation.model.User;
import com.airline.reservation.model.Flight;

public class AppContext {
    private static User loggedInUser;
    private static Flight selectedFlight;
    
    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }
    
    public static User getLoggedInUser() {
        return loggedInUser;
    }
    
    public static void setSelectedFlight(Flight flight) {
        selectedFlight = flight;
    }
    
    public static Flight getSelectedFlight() {
        return selectedFlight;
    }
    
    public static void clear() {
        loggedInUser = null;
        selectedFlight = null;
    }
}
