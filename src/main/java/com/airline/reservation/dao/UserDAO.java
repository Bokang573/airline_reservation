package com.airline.reservation.dao;

import com.airline.reservation.model.User;
import com.airline.reservation.util.DBUtil;

import java.sql.*;

public class UserDAO {
    public User findByUsername(String username) {
        String sql = "SELECT user_id, username, password_hash, role, email FROM users WHERE username = ?";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                User u = new User();
                u.setUserId(rs.getInt("user_id"));
                u.setUsername(rs.getString("username"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setRole(rs.getString("role"));
                u.setEmail(rs.getString("email"));
                return u;
            }
        } catch (Exception ex) { ex.printStackTrace(); }
        return null;
    }

    public User create(User user) throws SQLException {
        String sql = "INSERT INTO users (username, password_hash, role, email) VALUES (?, ?, ?, ?) RETURNING user_id";
        try (Connection c = DBUtil.getConnection(); PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getRole() == null ? "CUSTOMER" : user.getRole());
            ps.setString(4, user.getEmail());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) user.setUserId(rs.getInt(1));
        }
        return user;
    }
}

