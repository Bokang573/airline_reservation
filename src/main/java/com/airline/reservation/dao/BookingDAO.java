package com.airline.reservation.dao;

import com.airline.reservation.model.Booking;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BookingDAO {

    private final Connection connection;

    public BookingDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Booking> getAllBookings() {
        List<Booking> list = new ArrayList<>();

        String sql = """
                SELECT id, flight_id, passenger_id, seat_number, ticket_class,
                       travel_date, total_price, created_at, status
                FROM bookings
                """;

        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Booking b = new Booking();

                b.setId(rs.getInt("id"));
                b.setFlightId(rs.getInt("flight_id"));
                b.setPassengerId(rs.getInt("passenger_id"));
                b.setSeatNumber(rs.getString("seat_number"));
                b.setTicketClass(rs.getString("ticket_class"));

                Date travel = rs.getDate("travel_date");
                b.setTravelDate(travel != null ? travel.toLocalDate() : null);

                b.setTotalPrice(rs.getDouble("total_price"));

                // SAFE: handle potential NULL created_at
                Timestamp ts = rs.getTimestamp("created_at");
                b.setCreatedAt(ts != null ? ts.toLocalDateTime() : null);

                b.setStatus(rs.getString("status"));

                list.add(b);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public boolean cancelBooking(int bookingId) {
        String sql = "UPDATE bookings SET status = 'Cancelled' WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, bookingId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
