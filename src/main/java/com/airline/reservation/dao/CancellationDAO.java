package com.airline.reservation.dao;

import com.airline.reservation.database.DatabaseConnector;
import com.airline.reservation.model.Booking;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CancellationDAO {

    private final Connection conn;

    public CancellationDAO() {
        this.conn = DatabaseConnector.connect();
    }

    /** Fetch all bookings */
    public List<Booking> getAllBookings() {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT id, flight_id, passenger_id, seat_number, ticket_class, travel_date, total_price, status, created_at " +
                "FROM bookings ORDER BY id DESC";

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bookings.add(mapBooking(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error fetching bookings: " + e.getMessage());
        }

        return bookings;
    }

    /** Fetch single booking by ID (PNR) */
    public Booking getBookingById(String id) {
        String sql = "SELECT id, flight_id, passenger_id, seat_number, ticket_class, travel_date, total_price, status, created_at " +
                "FROM bookings WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapBooking(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching booking by ID: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Invalid booking ID format: " + id);
        }
        return null;
    }

    /** Cancel booking by ID (set status to 'Cancelled') */
    public boolean cancelBooking(String id) {
        String sql = "UPDATE bookings SET status = 'Cancelled' WHERE id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(id));
            int updated = stmt.executeUpdate();
            return updated > 0;
        } catch (SQLException e) {
            System.err.println("Error cancelling booking: " + e.getMessage());
            return false;
        } catch (NumberFormatException e) {
            System.err.println("Invalid booking ID format: " + id);
            return false;
        }
    }

    /** Calculate refund amount for a booking */
    public double calculateRefund(Booking booking) {
        if (booking == null) return 0.0;
        return booking.getTotalPrice() * 0.9;
    }

    /** Map ResultSet row to Booking object */
    private Booking mapBooking(ResultSet rs) throws SQLException {
        Booking b = new Booking();
        b.setId(rs.getInt("id"));
        b.setFlightId(rs.getInt("flight_id"));
        b.setPassengerId(rs.getInt("passenger_id"));
        b.setSeatNumber(rs.getString("seat_number"));
        b.setTicketClass(rs.getString("ticket_class"));
        b.setTravelDate(rs.getDate("travel_date") != null ? rs.getDate("travel_date").toLocalDate() : null);
        b.setTotalPrice(rs.getDouble("total_price"));
        b.setStatus(rs.getString("status"));

        //  convert Timestamp -> LocalDateTime safely
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            b.setCreatedAt(ts.toLocalDateTime());
        } else {
            b.setCreatedAt(null);
        }

        return b;
    }
}
