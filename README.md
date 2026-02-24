# Pantry Tracker System  
CSCE 548 – Projects 1 & 2

---

## System Overview

Pantry Tracker is a layered Java application for managing pantry inventory, meals, recipes, and prepared meals.

The system was built incrementally across two projects:

### Project 1
- Designed MySQL schema
- Implemented DAO layer using JDBC
- Implemented CRUD operations for all entities

### Project 2
- Implemented Business Layer (`PantryBusinessLayer`)
- Implemented Service Layer (REST API using Javalin)
- Hosted services locally
- Built console-based client for testing
- Demonstrated full CRUD functionality via HTTP

---

## Architecture
Client (PowerShell / Console Client)
↓
Service Layer (Javalin REST API)
↓
Business Layer (PantryBusinessLayer)
↓
DAO Layer (JDBC)
↓
MySQL Database


This architecture enforces separation of concerns:
- The Service layer never directly accesses DAOs.
- The Business layer encapsulates validation and rules.
- The DAO layer handles database interaction.

---

## Technologies Used

- Java 17
- Maven
- MySQL
- JDBC
- Javalin (embedded Jetty server)
- Jackson (JSON serialization)

---

## Platform / Hosting

Platform used for hosting:
- Windows 11 (Localhost)
- Javalin embedded server
- Maven build system

The service runs on:
http://localhost:7070

Health check endpoint:
http://localhost:7070/health

---

## Database Setup (Project 1)

Before running the service:

1. Ensure MySQL server is running.
2. Execute:
   - `db_creation.sql`
   - `db_insertion.sql`
3. Confirm credentials in `DBUtil.java` match your MySQL configuration.

---

## Running the Application (Project 2 Service Layer)

Open PowerShell in project directory: 
pantry_tracker


Change the user and password in the DBUtil.java file!!!!!!

Run:

```powershell terminal 1
mvn -q clean compile
mvn -q exec:java "-Dexec.mainClass=PantryServiceApi"

The -q flag runs Maven in quiet mode for cleaner output.

You should see:
PantryServiceApi started on http://localhost:7070

Stop the server with:
Ctrl + C

run simple client: 

```powershell terminal 2
mvn -q exec:java "-Dexec.mainClass=PantryConsoleClient"