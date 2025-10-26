# Honeypot-For-IoT-Devices

A small honeypot project for IoT devices (backend Java Spring Boot, simple frontend UI).

## Requirements

- Java 21 (LTS) — the project has been updated to target Java 21. Install a JDK 21 distribution (Eclipse Temurin / Adoptium, Oracle JDK, Azul, etc.), set JAVA_HOME to the JDK installation path and add its `bin` to your PATH.
- Maven 3.x to build the backend.

Quick check & build (PowerShell):
```powershell
# Confirm Java
java -version

# Confirm Maven
mvn -v

# From repo root: build backend
mvn -f .\backend\pom.xml clean package
```
# Honeypot-For-IoT-Devices