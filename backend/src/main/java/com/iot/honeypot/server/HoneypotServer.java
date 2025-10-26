package com.iot.honeypot.server;

import com.iot.honeypot.honeypot.HttpHoneypot;
import com.iot.honeypot.honeypot.TelnetHoneypot;
import com.iot.honeypot.service.AttackService;

import java.sql.SQLException;

public class HoneypotServer {

    private AttackService attackService;
    private HttpHoneypot httpHoneypot;
    private TelnetHoneypot telnetHoneypot;

    public HoneypotServer() {
        try {
            // Initialize attack service
            attackService = new AttackService();

            // Initialize honeypots with the attack service
            httpHoneypot = new HttpHoneypot(8080, attackService);
            telnetHoneypot = new TelnetHoneypot(23, attackService);

        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Start all honeypots
    public void start() {
        System.out.println("Starting Honeypot Server...");

        new Thread(() -> {
            try {
                httpHoneypot.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try {
                telnetHoneypot.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        System.out.println("Honeypots are running!");
    }

    public static void main(String[] args) {
        HoneypotServer server = new HoneypotServer();
        server.start();
    }
}
