package com.iot.honeypot.entity;

import java.sql.Timestamp;

public class AttackLog {
    private long id;
    private Timestamp timestamp;
    private String protocol;
    private String sourceIp;
    private String payload;
    private int port;

    public AttackLog(long id, Timestamp timestamp, String protocol, String sourceIp, String payload, int port) {
        this.id = id;
        this.timestamp = timestamp;
        this.protocol = protocol;
        this.sourceIp = sourceIp;
        this.payload = payload;
        this.port = port;
    }

    // Getters
    public long getId() {
        return id;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public String getProtocol() {
        return protocol;
    }

    public String getSourceIp() {
        return sourceIp;
    }

    public String getPayload() {
        return payload;
    }

    public int getPort() {
        return port;
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public void setSourceIp(String sourceIp) {
        this.sourceIp = sourceIp;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "AttackLog{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", protocol='" + protocol + '\'' +
                ", sourceIp='" + sourceIp + '\'' +
                ", payload='" + payload + '\'' +
                ", port=" + port +
                '}';
    }
}
