package com.airline.reservation.dao;

import com.airline.reservation.model.Activity;
import com.airline.reservation.database.DatabaseConnector;
import javafx.collections.FXCollections;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DashboardDAO {

    private Connection conn;

    // Uses DatabaseConnector internally
    public DashboardDAO() {
        this.conn = DatabaseConnector.connect();
    }

    //  Dashboard Stats

    public int getTotalBookings() {
        String sql = "SELECT COUNT(*) FROM bookings";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            System.err.println("Error fetching total bookings: " + e.getMessage());
            return 0;
        }
    }

    public int getActiveFlights() {
        String sql = "SELECT COUNT(*) FROM flights WHERE active = TRUE";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            System.err.println("Error fetching active flights: " + e.getMessage());
            return 0;
        }
    }

    public int getTotalPassengers() {
        String sql = "SELECT COUNT(*) FROM passengers";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            System.err.println("Error fetching passengers: " + e.getMessage());
            return 0;
        }
    }

    public double getTotalRevenue() {
        String sql = "SELECT SUM(total_amount) FROM bookings";
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            return rs.next() ? rs.getDouble(1) : 0.0;
        } catch (Exception e) {
            System.err.println("Error fetching total revenue: " + e.getMessage());
            return 0.0;
        }
    }

    // Recent Activity

    public List<Activity> getRecentActivities() {
        List<Activity> list = new ArrayList<>();

        String sql = """
                SELECT activity_time, activity, user_name, status
                FROM activity_log
                ORDER BY activity_time DESC
                LIMIT 20
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Activity a = new Activity(
                        rs.getTimestamp("activity_time").toString(),
                        rs.getString("activity"),
                        rs.getString("user_name"),
                        rs.getString("status")
                );
                list.add(a);
            }

        } catch (Exception e) {
            System.err.println("Error fetching recent activities: " + e.getMessage());
        }

        return list;
    }

    // Stats Summary Text

    public String getStatsSummary() {
        String sql = """
                SELECT 
                    COUNT(*) AS total, 
                    SUM(total_amount) AS revenue
                FROM bookings
                """;

        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                int count = rs.getInt("total");
                double revenue = rs.getDouble("revenue");

                return "Total Bookings: " + count +
                        " | Revenue: $" + String.format("%.2f", revenue);
            }

        } catch (Exception e) {
            System.err.println("Error fetching stats summary: " + e.getMessage());
        }

        return "No data available";
    }
}
