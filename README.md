# Car Park Service

A Spring Boot microservice providing real-time spatial proximity searches for Singapore HDB car parks with live lot availability.

---

## Quick Start (Docker Compose)

Reviewers do not need Java, Maven, or Gradle installed locally. Ensure [Docker Engine](https://docs.docker.com/get-docker/) and [Docker Compose](https://docs.docker.com/compose/) are installed and running.

### 1. Build and Run the Application
```bash
docker compose up --build
The application will compile inside a multi-stage Docker build, start the Spring Boot runtime on port 8080, initialize the H2 in-memory spatial database, and load static HDB car park metadata.2. Verify Application HealthBashcurl http://localhost:8080/actuator/health
API DocumentationGET /api/v1/carparks/nearbyRetrieves available car parks sorted by distance relative to a target geographic point using the Haversine formula.Query ParametersParameterTypeRequiredDefaultDescriptionlatitudedoubleYes-Target latitude in decimal degrees (-90.0 to 90.0)longitudedoubleYes-Target longitude in decimal degrees (-180.0 to 180.0)radiusdoubleNo3.0Search radius in kilometers (> 0.0)limitintNo10Maximum results to return (1 to 100)offsetintNo0Offset index for pagination (>= 0)Example RequestBashcurl -X GET "http://localhost:8080/api/v1/carparks/nearby?latitude=1.3000&longitude=103.8500&radius=3.0&limit=10&offset=0" \
  -H "Accept: application/json"

Example Request
GET "http://localhost:8080/api/v1/carparks/nearby?latitude=1.3000&longitude=103.8500&radius=3.0&limit=10&offset=0" \
  -H "Accept: application/json"

Example Response (200 OK)
JSON {
  "resultCount": 10,
  "page": 0,
  "limit": 20,
  "carParks": [
    {
      "carParkNo": "WCB",
      "address": "BLK 261/262/264 WATERLOO BASEMENT CAR PARK",
      "latitude": 1.298808,
      "longitude": 103.851938,
      "distanceKm": 0.253,
      "totalLots": 143,
      "lotsAvailable": 94,
      "lotType": "C",
      "isStale": false
    },
    {
      "carParkNo": "CY",
      "address": "BLK 269/269A/269B CHENG YAN COURT CAR PARK",
      "latitude": 1.300626,
      "longitude": 103.853665,
      "distanceKm": 0.413,
      "totalLots": 32,
      "lotsAvailable": 14,
      "lotType": "C",
      "isStale": false
    }
  ]
}
Solution Architecture  ┌───────────────────────────────┐
                       │    HDB Real-Time API / CSV    │
                       └───────────────┬───────────────┘
                                       │ Async Sync / Ingestion
                                       ▼
┌─────────────────┐           ┌─────────────────┐
│  REST Client    │ ────────> │Spring Controller│
└─────────────────┘           └────────┬────────┘
                                       │
                                       ▼
                              ┌─────────────────┐
                              │ Service Layer   │
                              └────────┬────────┘
                                       │
                                       ▼
                              ┌─────────────────┐
                              │  Repository     │ (Haversine Native SQL)
                              └────────┬────────┘
                                       │
                                       ▼
                              ┌─────────────────┐
                              │   PostgreSQL    │
                              └─────────────────┘
Framework: Java 17 / Spring Boot 3.xPersistence: Spring Data JPA with Native SQL interface projection (CarParkQueryResult)Spatial Calculation: In-database spherical Haversine math with subquery projection filtering (sub.lotsAvailable > 0)Data Isolation: Synchronized persistence flush boundaries ensuring test execution state consistency