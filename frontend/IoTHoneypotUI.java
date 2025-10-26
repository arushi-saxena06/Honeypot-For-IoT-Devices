package com.iot.honeypot.ui;

import com.iot.honeypot.server.HoneypotServer;
import com.iot.honeypot.service.AttackService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IoTHoneypotUI extends JFrame implements HoneypotServer.LogListener {

    private final AttackService attackService;
    private HoneypotServer honeypotServer;
    private JTextArea logArea;
    private JButton startButton;

    public IoTHoneypotUI() {
        this.attackService = new AttackService();
        setupUI();
    }

    private void setupUI() {
        setTitle("IoT Honeypot UI");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        logArea = new JTextArea();
        logArea.setEditable(false);
        add(new JScrollPane(logArea), BorderLayout.CENTER);

        startButton = new JButton("Start Honeypot");
        add(startButton, BorderLayout.SOUTH);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startHoneypots();
            }
        });

        setVisible(true);
    }

    private void startHoneypots() {
        honeypotServer = new HoneypotServer(8080, 2323, attackService);
        honeypotServer.start();
        logArea.append("Honeypots started!\n");
    }

    @Override
    public void onNewAttack(String log) {
        SwingUtilities.invokeLater(() -> logArea.append(log + "\n"));
    }

    public static void main(String[] args) {
        new IoTHoneypotUI();
    }
}
