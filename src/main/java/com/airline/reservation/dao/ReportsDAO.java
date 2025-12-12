package com.airline.reservation.dao;

import com.airline.reservation.model.BookingSummary;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ReportsDAO {

    private final String URL = "jdbc:postgresql://localhost:5432/airline";
    private final String USER = "postgres";
    private final String PASS = "airlink123";

    private Connection conn() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }

    public int countFlights() {
        return countByTable("flights");
    }

    public int countPassengers() {
        return countByTable("passengers");
    }

    public int countBookings() {
        return countByTable("bookings");
    }

    private int countByTable(String table) {
        String sql = "SELECT COUNT(*) FROM " + table;
        try (Connection c = conn(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    /**Bookings per month for the last `months` months (inclusive).*/
    public Map<String, Integer> bookingsPerMonth(int months) {
        Map<String, Integer> map = new LinkedHashMap<>();
        LocalDate start = LocalDate.now().minusMonths(months - 1).withDayOfMonth(1);
        // ensure every month key exists (ordered)
        LocalDate cur = start;
        for (int i = 0; i < months; i++) {
            map.put(cur.getYear() + "-" + String.format("%02d", cur.getMonthValue()), 0);
            cur = cur.plusMonths(1);
        }

        String sql = "SELECT to_char(travel_date,'YYYY-MM') AS ym, COUNT(*) AS cnt " +
                "FROM bookings WHERE travel_date >= ? GROUP BY ym ORDER BY ym";
        try (Connection c = conn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(start));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    map.put(rs.getString("ym"), rs.getInt("cnt"));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     * Distribution by ticket_class
     * returns Map ticket_class -> count
     */
    public Map<String, Integer> bookingClassDistribution() {
        Map<String, Integer> map = new LinkedHashMap<>();
        String sql = "SELECT ticket_class, COUNT(*) AS cnt FROM bookings GROUP BY ticket_class";
        try (Connection c = conn(); Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) map.put(rs.getString("ticket_class"), rs.getInt("cnt"));
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return map;
    }

    /**
     * Returns a page of booking summaries (joins passengers and flights for display).
     * pageIndex is 0-based.
     */
    public List<BookingSummary> getBookingsPage(int pageIndex, int pageSize) {
        List<BookingSummary> out = new ArrayList<>();
        int offset = pageIndex * pageSize;
        String sql = "SELECT b.id, p.full_name AS passenger_name, f.flight_number AS flight_number, " +
                "b.ticket_class, b.travel_date, b.seat_number, b.total_price, b.status " +
                "FROM bookings b " +
                "LEFT JOIN passengers p ON b.passenger_id = p.id " +
                "LEFT JOIN flights f ON b.flight_id = f.id " +
                "ORDER BY b.travel_date DESC " +
                "LIMIT ? OFFSET ?";

        try (Connection c = conn(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, pageSize);
            ps.setInt(2, offset);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    BookingSummary s = new BookingSummary();
                    s.setId(rs.getInt("id"));
                    s.setPassengerName(rs.getString("passenger_name"));
                    s.setFlightNumber(rs.getString("flight_number"));
                    s.setTicketClass(rs.getString("ticket_class"));
                    Date d = rs.getDate("travel_date");
                    s.setTravelDate(d != null ? d.toLocalDate() : null);
                    s.setSeatNumber(rs.getString("seat_number"));
                    s.setTotalPrice(rs.getDouble("total_price"));
                    s.setStatus(rs.getString("status"));
                    out.add(s);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return out;
    }

    /** total bookings used for pagination count */
    public int totalBookingCount() {
        return countBookings();
    }
}
