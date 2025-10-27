package com.iot.honeypot;

import com.iot.honeypot.honeypot.HttpHoneypot;
import com.iot.honeypot.honeypot.TelnetHoneypot;
import com.iot.honeypot.service.AttackService;
import java.sql.SQLException;
import java.io.IOException;

public class MainServer {
    public static void main(String[] args) {
        try {
            AttackService attackService = new AttackService();

            // Create and start HTTP honeypot
            HttpHoneypot httpHoneypot = new HttpHoneypot(8080, attackService);
            new Thread(() -> {
                try {
                    httpHoneypot.start();
                } catch (IOException e) {
                    System.err.println("Error in HTTP honeypot: " + e.getMessage());
                }
            }).start();

            // Create and start Telnet honeypot
            TelnetHoneypot telnetHoneypot = new TelnetHoneypot(2323, attackService);
            new Thread(() -> {
                try {
                    telnetHoneypot.start();
                } catch (IOException e) {
                    System.err.println("Error in Telnet honeypot: " + e.getMessage());
                }
            }).start();
        } catch (SQLException e) {
            System.err.println("Error initializing AttackService: " + e.getMessage());
        }
    }
}