# Record Shop API

## Description
The Record Shop API is a RESTful API designed to provide a robust backend for managing a record shop's catalog. Built using Java
and Spring Boot, following the MVC design pattern with a focus on best practises.

## Features
* Manage albums and artists with full CRUD operations.
* Search and filter albums by:
  * Genre
  * Release Year
  * Artist Name
* Exception handling for robust error management.
* Database support for H2 (in-memory) during development and configurable for PostgreSQL 
or other relational databases

## Setup Instructions

### Prerequisites
Before running the project, ensure you have the following installed:
- **Java Development Kit (JDK) 17+**
- **Maven** for building and managing dependencies
- **An IDE** (e.g. Intellij IDEA, Eclipse, or VS code)

### Running the API
1. **Clone the repository:**
```bash
git clone https://github.com/yourusername/record-shop-api.git
cd record-shop-api
```
2. **Configure the database:**
   * By default, the API uses H2 in-memory database for development.
   * To switch to a production ready database like PostgreSQL:
      1. create a `application-rds.properties` file: 
     ```properties
     spring.datasource.url=jdbc:postgresql://localhost:5432/recordshop
     spring.datasource.driverClassName=org.postgresql.Driver
     spring.datasource.username=<your_username>
     spring.datasource.password=<your_password>
     spring.jpa.hibernate.ddl-auto=update
     ```
     2. Set the active profile to `rds` for external database configuration in the `application.properties` file:
     ```properties
     spring.profiles.active=rds
     ```
3. **Build the project:**
```bash
mvn clean install
```
4. **Run the application:**
   * Open the project in your IDE of choice. 
   * Navigate to the `RecordShopApiApplication` class:\
  `src/main/java/recordshop/RecordShopApiApplication.java` 
   * Run the `main` method.

## Endpoints
| HTTP Method | Endpoint              | Description                                           |
|-------------|-----------------------|-------------------------------------------------------|
| GET         | `/albums`             | Get all albums.                                       |
| GET         | `/albums?query=value` | Search albums by genre, release year, or artist name. |
| GET         | `/albums/{id}`        | Get album by ID.                                      |
| POST        | `/albums`             | Add a new album.                                      |
| PUT         | `/albums/{id}`        | Update an album.                                      |
| DELETE      | `/albums/{id}`        | Delete an album.                                      |


## Testing
This API includes a comprehensive test suite to ensure all features function as expected.\
Tests include:

* Repository tests for custom database interactions
* Service-layer tests for business logic.
* Controller tests to validate endpoints.

### Running Tests
1. Open the project in your IDE.
2. Navigate to the test folder:\
   `src/test/java`
3. Run the test suite to validate all functionality.

## Credits
Created by Kye Yee | 2024