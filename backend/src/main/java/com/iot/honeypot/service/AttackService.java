package com.iot.honeypot.service;

import com.iot.honeypot.db.DatabaseConnection;
import com.iot.honeypot.entity.AttackLog;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AttackService {
    private final Connection connection;
    private final List<AttackLogListener> listeners = new ArrayList<>();

    @FunctionalInterface
    public interface AttackLogListener {
        void onAttackLogged(AttackLog log);
    }

    public void addAttackLogListener(AttackLogListener listener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener);
        }
    }

    public void removeAttackLogListener(AttackLogListener listener) {
        listeners.remove(listener);
    }

    public AttackService() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
        initializeDatabase();
    }

    private void initializeDatabase() throws SQLException {
        // Create attack_logs table if it doesn't exist
        String createTable = 
            "CREATE TABLE IF NOT EXISTS attack_logs (" +
            "    id BIGINT AUTO_INCREMENT PRIMARY KEY," +
            "    timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
            "    protocol VARCHAR(50) NOT NULL," +
            "    source_ip VARCHAR(50) NOT NULL," +
            "    payload TEXT," +
            "    port INT" +
            ")";
        
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createTable);
            // Ensure 'port' column exists for older schemas that may lack it
            String checkPortColumn = "SELECT COUNT(*) AS cnt FROM information_schema.COLUMNS " +
                    "WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'attack_logs' AND COLUMN_NAME = 'port'";
            try (ResultSet rs = stmt.executeQuery(checkPortColumn)) {
                if (rs.next()) {
                    int cnt = rs.getInt("cnt");
                    if (cnt == 0) {
                        System.out.println("AttackService: 'port' column missing, altering table to add it.");
                        stmt.execute("ALTER TABLE attack_logs ADD COLUMN port INT");
                    }
                }
            }
        }
    }

    public void recordAttack(String protocol, String sourceIp, String payload, int port) throws SQLException {
        String sql = "INSERT INTO attack_logs (protocol, source_ip, payload, port) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, protocol);
            stmt.setString(2, sourceIp);
            stmt.setString(3, payload);
            stmt.setInt(4, port);
            stmt.executeUpdate();

            // Get the inserted record for notification
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    long id = rs.getLong(1);
                    AttackLog log = getAttackLog(id);
                    notifyListeners(log);
                }
            }
        }
    }

    public List<AttackLog> getAllAttacks() throws SQLException {
        List<AttackLog> attacks = new ArrayList<>();
        String sql = "SELECT * FROM attack_logs ORDER BY timestamp DESC";
        
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                attacks.add(new AttackLog(
                    rs.getLong("id"),
                    rs.getTimestamp("timestamp"),
                    rs.getString("protocol"),
                    rs.getString("source_ip"),
                    rs.getString("payload"),
                    rs.getInt("port")
                ));
            }
        }
        return attacks;
    }

    private AttackLog getAttackLog(long id) throws SQLException {
        String sql = "SELECT * FROM attack_logs WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new AttackLog(
                        rs.getLong("id"),
                        rs.getTimestamp("timestamp"),
                        rs.getString("protocol"),
                        rs.getString("source_ip"),
                        rs.getString("payload"),
                        rs.getInt("port")
                    );
                }
            }
        }
        throw new SQLException("Attack log not found with id: " + id);
    }

    private void notifyListeners(AttackLog log) {
        for (AttackLogListener listener : listeners) {
            listener.onAttackLogged(log);
        }
    }
}
