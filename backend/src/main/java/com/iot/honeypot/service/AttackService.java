package com.iot.honeypot.service;

import com.iot.honeypot.db.DatabaseConnection;
import com.iot.honeypot.entity.AttackLog;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttackService {

    private final Connection connection;

    public AttackService() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    // Record a new attack in DB
    public void recordAttack(String protocol, String sourceIp, String payload) {
        String sql = "INSERT INTO attack_logs (protocol, source_ip, payload) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, protocol);
            stmt.setString(2, sourceIp);
            stmt.setString(3, payload);
            stmt.executeUpdate();
            System.out.println("Attack recorded: " + protocol + " from " + sourceIp);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Fetch all attack logs from DB
    public List<AttackLog> getAllAttacks() {
        List<AttackLog> logs = new ArrayList<>();
        String sql = "SELECT id, protocol, source_ip, payload, timestamp FROM attack_logs ORDER BY timestamp DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                AttackLog log = new AttackLog(
                        rs.getInt("id"),
                        rs.getString("protocol"),
                        rs.getString("source_ip"),
                        rs.getString("payload"),
                        rs.getTimestamp("timestamp")
                );
                logs.add(log);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }
}
