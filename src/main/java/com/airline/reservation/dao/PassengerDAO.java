package com.airline.reservation.dao;

import com.airline.reservation.database.DatabaseConnector;
import com.airline.reservation.model.Passenger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PassengerDAO {

    private final Connection conn;

    public PassengerDAO() {
        conn = DatabaseConnector.connect();
    }

    // Fetch all passengers
    public List<Passenger> getAllPassengers() {
        List<Passenger> passengers = new ArrayList<>();
        String sql = "SELECT id, full_name, email, phone, passport, nationality FROM passengers";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Passenger p = new Passenger(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("passport"),
                        rs.getString("nationality")
                );
                passengers.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return passengers;
    }

    // Insert a new passenger
    public boolean insertPassenger(Passenger passenger) {
        String sql = "INSERT INTO passengers (full_name, email, phone, passport, nationality) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, passenger.getFullName());
            stmt.setString(2, passenger.getEmail());
            stmt.setString(3, passenger.getPhone());
            stmt.setString(4, passenger.getPassport());
            stmt.setString(5, passenger.getNationality());

            int rows = stmt.executeUpdate();
            return rows > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
