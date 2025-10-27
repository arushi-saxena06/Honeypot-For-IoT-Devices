package com.iot.honeypot.honeypot;

import com.iot.honeypot.service.AttackService;

import java.io.*;
import java.net.*;
import java.sql.SQLException;

public class HttpHoneypot {

    private final int port;
    private final AttackService attackService;
    private ServerSocket serverSocket;

    public HttpHoneypot(int port, AttackService attackService) {
        this.port = port;
        this.attackService = attackService;
    }

    public void start() throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.println("HTTP Honeypot listening on port " + port);

        while (true) {
            Socket client = serverSocket.accept();
            new Thread(() -> handleClient(client)).start();
        }
    }

    private void handleClient(Socket client) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
             BufferedWriter out = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()))) {

            String line = in.readLine();
            if (line != null) {
                System.out.println("HTTP Request: " + line);

                // Record attack
                attackService.recordAttack("HTTP", client.getInetAddress().getHostAddress(), line, port);

                // Send dummy response
                String response = "HTTP/1.1 200 OK\r\nContent-Length: 2\r\n\r\nOK";
                out.write(response);
                out.flush();
            }

        } catch (IOException e) {
            System.err.println("Error handling HTTP client: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Error recording HTTP attack: " + e.getMessage());
        } finally {
            try {
                client.close();
            } catch (IOException ignored) {}
        }
    }
}
