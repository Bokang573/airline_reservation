package com.airline.reservation.model;

public class Passenger {

    private int id;
    private String fullName;
    private String email;
    private String phone;
    private String passport;
    private String nationality;

    // Constructor used when reading from DB (includes id)
    public Passenger(int id, String fullName, String email, String phone, String passport, String nationality) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.passport = passport;
        this.nationality = nationality;
    }

    // Constructor used when inserting into DB (no id)
    public Passenger(String fullName, String email, String phone, String passport, String nationality) {
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.passport = passport;
        this.nationality = nationality;
    }

    // GETTERS
    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassport() {
        return passport;
    }

    public String getNationality() {
        return nationality;
    }

    // SETTERS
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }
}
