package com.iot.honeypot.server;

import com.iot.honeypot.honeypot.HttpHoneypot;
import com.iot.honeypot.honeypot.TelnetHoneypot;
import com.iot.honeypot.service.AttackService;
import com.iot.honeypot.entity.AttackLog;

import java.sql.SQLException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HoneypotServer {
    private final List<LogListener> logListeners = new ArrayList<>();
    private final int httpPort;
    private final int telnetPort;
    private AttackService attackService;
    private HttpHoneypot httpHoneypot;
    private TelnetHoneypot telnetHoneypot;

    public interface LogListener {
        void onNewAttack(String log);
    }

    public void addLogListener(LogListener listener) {
        if (!logListeners.contains(listener)) {
            logListeners.add(listener);
        }
    }

    public void removeLogListener(LogListener listener) {
        logListeners.remove(listener);
    }

    protected void notifyLogListeners(String log) {
        for (LogListener listener : logListeners) {
            listener.onNewAttack(log);
        }
    }

    public HoneypotServer(int httpPort, int telnetPort, AttackService attackService) {
        this.httpPort = httpPort;
        this.telnetPort = telnetPort;
        this.attackService = attackService;
        
        // Initialize honeypots with the attack service and port
        httpHoneypot = new HttpHoneypot(httpPort, attackService);
        telnetHoneypot = new TelnetHoneypot(telnetPort, attackService);
        
        // Add attack log listener
        AttackService.AttackLogListener attackLogListener = (AttackLog log) -> {
            String logMessage = String.format("[%s] Attack detected from %s on port %d using protocol %s",
                log.getTimestamp(), log.getSourceIp(), log.getPort(), log.getProtocol());
            notifyLogListeners(logMessage);
        };
        attackService.addAttackLogListener(attackLogListener);
    }

    public void start() {
        System.out.println("Starting Honeypot Server...");
        System.out.printf("HTTP Honeypot listening on port %d\n", httpPort);
        System.out.printf("Telnet Honeypot listening on port %d\n", telnetPort);

        new Thread(() -> {
            try {
                httpHoneypot.start();
            } catch (IOException e) {
                String error = "Error in HTTP honeypot: " + e.getMessage();
                System.err.println(error);
                notifyLogListeners(error);
            }
        }).start();

        new Thread(() -> {
            try {
                telnetHoneypot.start();
            } catch (IOException e) {
                String error = "Error in Telnet honeypot: " + e.getMessage();
                System.err.println(error);
                notifyLogListeners(error);
            }
        }).start();

        notifyLogListeners("Honeypots are running!");
    }
}
