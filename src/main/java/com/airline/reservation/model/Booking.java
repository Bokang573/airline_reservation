package com.airline.reservation.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Booking {

    private int id;
    private int flightId;
    private int passengerId;
    private String seatNumber;
    private String ticketClass;
    private LocalDate travelDate;
    private double totalPrice;
    private LocalDateTime createdAt;   // <-- LocalDateTime
    private String status;

    // getters (omitted for brevity)...
    public int getId() { return id; }
    public int getFlightId() { return flightId; }
    public int getPassengerId() { return passengerId; }
    public String getSeatNumber() { return seatNumber; }
    public String getTicketClass() { return ticketClass; }
    public LocalDate getTravelDate() { return travelDate; }
    public double getTotalPrice() { return totalPrice; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }

    // setters
    public void setId(int id) { this.id = id; }
    public void setFlightId(int flightId) { this.flightId = flightId; }
    public void setPassengerId(int passengerId) { this.passengerId = passengerId; }
    public void setSeatNumber(String seatNumber) { this.seatNumber = seatNumber; }
    public void setTicketClass(String ticketClass) { this.ticketClass = ticketClass; }
    public void setTravelDate(LocalDate travelDate) { this.travelDate = travelDate; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

    // Accept java.time.LocalDateTime
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setStatus(String status) { this.status = status; }
}
