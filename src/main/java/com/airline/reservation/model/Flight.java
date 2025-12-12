package com.airline.reservation.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Flight {
    private int id;
    private String flightNumber;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double price;

    // Formatter for display purposes
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Flight() {}

    public Flight(int id, String flightNumber, String origin, String destination,
                  LocalDateTime departureTime, LocalDateTime arrivalTime, double price) {
        this.id = id;
        this.flightNumber = flightNumber;
        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.price = price;
    }

    // Getters & Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getOrigin() { return origin; }
    public void setOrigin(String origin) { this.origin = origin; }

    public String getDestination() { return destination; }
    public void setDestination(String destination) { this.destination = destination; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    // Helper methods for TableView display
    public String getFormattedDepartureTime() {
        return departureTime != null ? departureTime.format(formatter) : "";
    }

    public String getFormattedArrivalTime() {
        return arrivalTime != null ? arrivalTime.format(formatter) : "";
    }
}
