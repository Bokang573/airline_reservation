package com.airline.reservation.model;

public class Ticket {
    private String pnr;
    private String passengerName;
    private String passengerEmail;
    private Flight flight;
    private int seatNumber;

    public Ticket() {}

    public Ticket(String pnr, String passengerName, String passengerEmail,
                  Flight flight, int seatNumber) {
        this.pnr = pnr;
        this.passengerName = passengerName;
        this.passengerEmail = passengerEmail;
        this.flight = flight;
        this.seatNumber = seatNumber;
    }

    public String getPnr() { return pnr; }
    public String getPassengerName() { return passengerName; }
    public String getPassengerEmail() { return passengerEmail; }
    public Flight getFlight() { return flight; }
    public int getSeatNumber() { return seatNumber; }

    public void setPnr(String pnr) { this.pnr = pnr; }
    public void setPassengerName(String passengerName) { this.passengerName = passengerName; }
    public void setPassengerEmail(String passengerEmail) { this.passengerEmail = passengerEmail; }
    public void setFlight(Flight flight) { this.flight = flight; }
    public void setSeatNumber(int seatNumber) { this.seatNumber = seatNumber; }
}

