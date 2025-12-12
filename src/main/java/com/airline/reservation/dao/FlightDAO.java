package com.airline.reservation.dao;

import com.airline.reservation.model.Flight;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightDAO {

    private final String url = "jdbc:postgresql://localhost:5432/airline";
    private final String user = "postgres";
    private final String password = "airlink123";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    public List<Flight> getAllFlights() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights ORDER BY id";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Flight flight = new Flight(
                        rs.getInt("id"),
                        rs.getString("flight_number"),
                        rs.getString("origin"),
                        rs.getString("destination"),
                        rs.getTimestamp("departure_time") != null ? rs.getTimestamp("departure_time").toLocalDateTime() : null,
                        rs.getTimestamp("arrival_time") != null ? rs.getTimestamp("arrival_time").toLocalDateTime() : null,
                        rs.getDouble("price")
                );
                flights.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    public boolean insertFlight(Flight flight) {
        String sql = "INSERT INTO flights(flight_number, origin, destination, departure_time, arrival_time, price) " +
                "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, flight.getFlightNumber());
            ps.setString(2, flight.getOrigin());
            ps.setString(3, flight.getDestination());
            ps.setTimestamp(4, flight.getDepartureTime() != null ? Timestamp.valueOf(flight.getDepartureTime()) : null);
            ps.setTimestamp(5, flight.getArrivalTime() != null ? Timestamp.valueOf(flight.getArrivalTime()) : null);
            ps.setDouble(6, flight.getPrice());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateFlight(Flight flight) {
        String sql = "UPDATE flights SET flight_number=?, origin=?, destination=?, departure_time=?, arrival_time=?, price=? " +
                "WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, flight.getFlightNumber());
            ps.setString(2, flight.getOrigin());
            ps.setString(3, flight.getDestination());
            ps.setTimestamp(4, flight.getDepartureTime() != null ? Timestamp.valueOf(flight.getDepartureTime()) : null);
            ps.setTimestamp(5, flight.getArrivalTime() != null ? Timestamp.valueOf(flight.getArrivalTime()) : null);
            ps.setDouble(6, flight.getPrice());
            ps.setInt(7, flight.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteFlight(int id) {
        String sql = "DELETE FROM flights WHERE id=?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
