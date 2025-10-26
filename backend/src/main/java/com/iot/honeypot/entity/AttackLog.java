package com.iot.honeypot.entity;

import java.sql.Timestamp;

public class AttackLog {
    private int id;
    private String type;
    private String source;
    private String target;
    private Timestamp timestamp;

    // Constructor matching the usage in AttackService
    public AttackLog(int id, String type, String source, String target, Timestamp timestamp) {
        this.id = id;
        this.type = type;
        this.source = source;
        this.target = target;
        this.timestamp = timestamp;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getSource() {
        return source;
    }

    public String getTarget() {
        return target;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    // Optional: Setters, if needed
    public void setId(int id) {
        this.id = id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "AttackLog{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", source='" + source + '\'' +
                ", target='" + target + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
