package com.airline.reservation.model;

import java.time.LocalDate;

public class BookingSummary {
    private int id;
    private String passengerName;
    private String flightNumber;
    private String ticketClass;
    private LocalDate travelDate;
    private String seatNumber;
    private double totalPrice;
    private String status;

    public BookingSummary() {}

    // getters / setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getPassengerName() { return passengerName; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }

    public String getFlightNumber() { return flightNumber; }
    public void setFlightNumber(String flightNumber) { this.flightNumber = flightNumber; }

    public String getTicketClass() { return ticketClass; }
    public void setTicketClass(String ticketClass) { this.ticketClass = ticketClass; }

    public LocalDate getTravelDate() { return travelDate; }
    public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }

    public String getSeatNumber() { return seatNumber; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
