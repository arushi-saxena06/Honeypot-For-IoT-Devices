package com.iot.honeypot.honeypot;

import com.iot.honeypot.service.AttackService;

import java.io.*;
import java.net.*;
import java.sql.SQLException;

public class TelnetHoneypot {

    private final int port;
    private final AttackService attackService;
    private ServerSocket serverSocket;

    public TelnetHoneypot(int port, AttackService attackService) {
        this.port = port;
        this.attackService = attackService;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("Telnet Honeypot listening on port " + port);

        while (true) {
            Socket client = serverSocket.accept();
            new Thread(() -> handleClient(client)).start();
        }
    }

    private void handleClient(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {

            out.write("Welcome to Telnet Honeypot!\r\n");
            out.flush();

            String line;
            while ((line = in.readLine()) != null) {
                System.out.println("Telnet Input: " + line);

                // Record attack
                attackService.recordAttack("Telnet", client.getInetAddress().getHostAddress(), line, port);

                // Echo back
                out.write("You said: " + line + "\r\n");
                out.flush();
            }

        } catch (IOException e) {
            System.err.println("Error handling Telnet client: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error recording Telnet attack: " + e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException ignored) {}
        }
    }
}
