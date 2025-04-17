# Device API

## Overview

The **Device API** is a RESTful API built with **Spring Boot** to manage a collection of devices. It allows users to create, update, retrieve, and delete devices, as well as filter devices by brand or state.

## Features

- Create a new device
- Update device details
- Retrieve a device by its unique ID
- Retrieve all devices
- Filter devices by brand or state
- Delete a device

## Technologies

- Java 21
- Spring Boot 3.4.4
- PostgreSQL
- Lombok

## Endpoints

**POST** `/api/devices` - Creates a new device.

**PUT** `/api/devices/{id}` - Updates an existing device by its ID.

**GET** `/api/devices/{id}` - Retrieves a device by its unique ID.

**GET** `/api/devices` - Retrieves a list of all devices.

**GET** `/api/devices/brand/{brand}` - Retrieves a list of devices filtered by brand.

**GET** `/api/devices/state/{state}` - Retrieves a list of devices filtered by state.

**DELETE** `/api/devices/{id}` - Deletes a device by its ID.

---

## Running the Application

To run the application locally:

1. Clone the repository:
   ```bash
   git clone https://github.com/vitordel/deviceapi.git
   cd deviceapi
   ```

2. Build and run the application using Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

3. The API will be available at `http://localhost:8080`.

---

## Docker

You can run this project with Docker using the official OpenJDK image and configuring PostgreSQL separately.

### Run

```bash
./mvnw clean package -DskipTests
docker-compose up --build
```

## Swagger UI

After running the application, you can access the Swagger UI to explore and test the API:

**Swagger URL:**
`http://localhost:8080/swagger-ui.html`

## Database Configuration

The application connects to a PostgreSQL database. Make sure to configure your database settings in the `application.properties` file, typically found in the `src/main/resources` folder.

Example configuration:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/deviceapi
spring.datasource.username=your-username
spring.datasource.password=your-password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## 👤 Author

- [Vitor Delgado](https://github.com/vitordel)