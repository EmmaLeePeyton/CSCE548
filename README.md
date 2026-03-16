# Pantry Tracker

Pantry Tracker is a multi‑layer Java application designed to manage
household pantry inventory, ingredients, meals, and recipes.\
The system provides a REST API built with **Javalin**, a **MySQL
database** for data storage, and a **web frontend** for interacting with
pantry data.

This project demonstrates an **n‑tier architecture**, separating the
data, business logic, service/API, and client layers.\
The application supports creating, retrieving, updating, and deleting
pantry‑related records through both API endpoints and a web interface.

This repository contains the final implementation for **CSCE 548 --
Project 4**.

------------------------------------------------------------------------

# Features

The Pantry Tracker system allows users to manage:

-   Categories
-   Ingredients
-   Storage locations
-   Pantry inventory items
-   Recipes
-   Meals and prepared meals

Supported operations include:

-   Retrieve all records
-   Retrieve a single record by ID
-   Create new records
-   Update existing records
-   Delete records

Additional capabilities include:

-   REST API endpoints built with Javalin
-   JSON serialization using Jackson
-   MySQL database persistence
-   JavaScript web frontend
-   Console client for testing API functionality

------------------------------------------------------------------------

# Architecture

The project follows a layered architecture.

**Client Layer**\
Web frontend and console client used to interact with the API.

**Service Layer**\
REST API endpoints implemented using Javalin.

**Business Layer**\
Handles application logic and validation between the service and data
layers.

**Data Layer**\
DAO classes responsible for database operations.

**Database**\
MySQL database storing pantry data.

------------------------------------------------------------------------

# Technologies Used

-   Java
-   Javalin
-   Jackson JSON Library
-   MySQL
-   Maven
-   HTML
-   JavaScript
-   CSS

------------------------------------------------------------------------

# Prerequisites

Before running the application, install the following:

-   Java JDK (17 or newer recommended)
-   Maven
-   MySQL Server
-   A Java IDE such as IntelliJ IDEA or VS Code

------------------------------------------------------------------------

# Database Setup

1.  Create the database using the SQL script:

```{=html}
<!-- -->
```
    db_creation.sql

2.  Insert sample data:

```{=html}
<!-- -->
```
    db_insertion.sql

3.  Update database credentials in:

```{=html}
<!-- -->
```
    DBUtil.java

Modify the username and password fields to match your MySQL
configuration.

------------------------------------------------------------------------

# Running the Backend Service

Navigate to the project directory containing the Maven configuration.

Build the project:

    mvn clean compile

Run the service:

    mvn exec:java

The service will start on:

    http://localhost:7070

You can test the service by visiting:

    http://localhost:7070/health

------------------------------------------------------------------------

# Running the Frontend

Navigate to the **frontend** folder and open:

    index.html

in a web browser.

The frontend communicates with the backend REST API at:

    http://localhost:7070

From the interface you can view and manage pantry data such as
locations, ingredients, and inventory items.

------------------------------------------------------------------------

# Example API Endpoints

Retrieve all locations

    GET /api/locations

Retrieve a single location

    GET /api/locations/{id}

Create a new location

    POST /api/locations

Update a location

    PUT /api/locations/{id}

Delete a location

    DELETE /api/locations/{id}

Similar endpoints exist for ingredients, inventory items, recipes,
meals, and other entities.

------------------------------------------------------------------------

# Project Structure

    CSCE548
   ├── frontend
   │   ├── app.js
   │   ├── index.html
   │   └── style.css
   │
   ├── pantry_tracker
   │   ├── pom.xml
   │   ├── README.md
   │   └── src
   │       ├── main
   │       │   ├── java
   │       │   │   └── org
   │       │   │       └── example
   │       │   │           └── pantrytracker
   │       │   │               ├── PantryConsoleClient.java
   │       │   │               ├── PantryServiceApi.java
   │       │   │               ├── business
   │       │   │               ├── dao
   │       │   │               ├── dto
   │       │   │               ├── exception
   │       │   │               ├── model
   │       │   │               └── util
   │       │   └── resources
   │       └── test
   │
   ├── db_creation.sql
   └── db_insertion.sql

------------------------------------------------------------------------

# Testing

The system was tested by performing CRUD operations through the web
frontend and API endpoints.

Example tests include:

-   retrieving all pantry locations
-   adding a new ingredient
-   updating an inventory item
-   deleting a location
-   verifying database updates after API operations

Screenshots and full system testing results are included in the project
deployment document.

------------------------------------------------------------------------

# Deployment Documentation

A full deployment and system testing document is included in the
repository.

This document contains:

-   setup instructions
-   hosting steps
-   system test screenshots
-   graduate project analysis
-   AI prompt documentation
-   troubleshooting steps

------------------------------------------------------------------------

# Graduate Project Addendum

The graduate version of this project required documenting the use of AI
tools during development.

The deployment document includes:

-   prompts used to generate code
-   changes made to generated output
-   errors encountered and resolved
-   evaluation of AI tool effectiveness

------------------------------------------------------------------------

# Author

Student: EmmaLee Peyton\
Course: CSCE 548\
Assignment: Project 4
