package com.airline.reservation.model;

public class Reservation {
    private int id;
    private User user;
    private Flight flight;
    private int seatNumber;
    private String pnr;

    public Reservation() {}

    public Reservation(int id, User user, Flight flight, int seatNumber, String pnr) {
        this.id = id;
        this.user = user;
        this.flight = flight;
        this.seatNumber = seatNumber;
        this.pnr = pnr;
    }

    public int getId() { return id; }
    public User getUser() { return user; }
    public Flight getFlight() { return flight; }
    public int getSeatNumber() { return seatNumber; }
    public String getPnr() { return pnr; }

    public void setId(int id) { this.id = id; }
    public void setUser(User user) { this.user = user; }
    public void setFlight(Flight flight) { this.flight = flight; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
    public void setPnr(String pnr) { this.pnr = pnr; }
}

