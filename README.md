#  Smart Campus REST API (JAX-RS) 

## Overview 

This project implements a RESTful API for a **Smart Campus system** that manages:

* Rooms  
* Sensors  
* Sensor Readings              

The API is built using **JAX-RS (Jersey)** and follows REST principles such as:

* Resource-based design
* Proper HTTP methods & status codes
* Nested resources
* Error handling with Exception Mappers

---

##  Tech Stack 

* Java (JDK 8+)
* JAX-RS (Jersey)
* Maven
* Embedded server (Grizzly / Tomcat)
* In-memory storage (HashMap / ArrayList)

> ❗ No database is used as per coursework requirements.

---

##  How to Run the Project 

### Clone the Repository 

```bash
git clone https://github.com/your-username/smart-campus-api.git
cd smart-campus-api
```

###Build the Project 

```bash
mvn clean install
```

###Run the Server 

```bash
mvn exec:java
```

###Base URL 

```bash
http://localhost:8080/api/v1
```

---

##API Structure 

```
/api/v1
   ├── /rooms
   │     └── /{roomId}
   │
   ├── /sensors
   │     └── /{sensorId}
   │           └── /readings
```

---

## 🔗 Sample CURL Commands (Required)

###1. Discovery Endpoint 

```bash
curl -X GET http://localhost:8080/api/v1
```

---

###2. Create a Room 

```bash
curl -X POST http://localhost:8080/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{
  "id": "LIB-301",
  "name": "Library Quiet Study",
  "capacity": 50
}'
```

---

###3. Get All Rooms 

```bash
curl -X GET http://localhost:8080/api/v1/rooms
```

---

###4. Create a Sensor 

```bash
curl -X POST http://localhost:8080/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{
  "id": "TEMP-001",
  "type": "Temperature",
  "status": "ACTIVE",
  "currentValue": 22.5,
  "roomId": "LIB-301"
}'
```

---

###5. Filter Sensors by Type 

```bash
curl -X GET "http://localhost:8080/api/v1/sensors?type=Temperature"
```

---

###6. Add Sensor Reading 

```bash
curl -X POST http://localhost:8080/api/v1/sensors/TEMP-001/readings \
-H "Content-Type: application/json" \
-d '{
  "id": "reading-001",
  "timestamp": 1710000000000,
  "value": 23.1
}'
```

---

###7. Get Sensor Readings 

```bash
curl -X GET http://localhost:8080/api/v1/sensors/TEMP-001/readings
```

---

###8. Delete Room (Failure Example if Sensors Exist) 

```bash
curl -X DELETE http://localhost:8080/api/v1/rooms/LIB-301
```

---

## Error Handling 

The API uses custom exceptions and maps them to HTTP responses:

| Scenario                 | Status Code               |
| ------------------------ | ------------------------- |
| Room has sensors         | 409 Conflict              |
| Invalid roomId in sensor | 422 Unprocessable Entity  |
| Sensor in maintenance    | 403 Forbidden             |
| Unexpected errors        | 500 Internal Server Error |

---

##Key Features 

* RESTful API design
* Resource nesting (`/sensors/{id}/readings`)
* Query filtering (`?type=CO2`)
* Exception handling with mappers
* Request/response logging using filters

---

## 📹 Video Demonstration

A 10-minute demonstration video showing API testing using Postman is included in the Blackboard submission.

---

##Notes 

* All data is stored in-memory using collections
* No external database is used
* The API is designed for demonstration and coursework purposes

---

##Author 

Student Name: Limuthu Lohiru  
Module: 5COSC022W - Client-Server Architectures