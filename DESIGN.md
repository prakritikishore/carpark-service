# Design Thinking & Architecture Decisions

## Design Decisions & Trade-Offs

### 1. In-Database Haversine Subquery vs. In-Memory Post-Processing
* **Evaluated:** Fetching all candidate car parks into JVM memory and running spatial calculations using libraries like Lucene or Spatial4j.
* **Selected Strategy:** Native SQL evaluation of the Haversine distance formula within an inner subquery projection.
* **Trade-Off Analysis:** Performing trigonometric operations (`COS`, `ACOS`, `RADIANS`) in native SQL introduces database CPU utilization. However, pushing this calculation into the DB engine allows applying distance filtering (`sub.distanceKm <= :radiusKm`) and zero-lot elimination (`sub.lotsAvailable > 0`) prior to pagination (`LIMIT :limit OFFSET :offset`). This drastically reduces JVM heap overhead, network transport size, and garbage collection pressure under heavy search traffic.

### 2. Subquery Alias Qualification (`sub.lotsAvailable`)
* **Evaluated:** Unqualified column names or `HAVING` clauses in outer native SQL queries.
* **Selected Strategy:** Wrapping the projection in a named subquery `SELECT * FROM (...) sub` and explicitly qualifying `sub.lotsAvailable > 0` and `sub.distanceKm`.
* **Reasoning:** In SQL engines (including H2 and MySQL), referencing inner projection aliases directly in the outer `WHERE` clause without explicit qualifier scoping can lead to unexpected alias resolution errors or fallback to raw join state. Explicit subquery qualification enforces deterministic filtering across dialects.

---

## Technical & Operational Reflections

### 1. Most Challenging Technical Decision
**Decision:** Managing First-Level Cache flushing and transactional boundaries during native query integration testing.
* **Context:** In Spring Boot integration tests annotated with `@Transactional`, JPA entities created during setup (`saveAll()`) reside in the Persistence Context (L1 cache) and JDBC write buffers.
* **Challenge:** Native SQL queries bypass the JPA First-Level Cache and execute directly against the H2 database engine tables. Without explicitly synchronizing memory state (`saveAllAndFlush()` or manual `flush()`), native spatial queries execute against stale database states. Establishing flush protocols ensured integration tests remained deterministic without requiring expensive context reloads (`@DirtiesContext`).

### 2. Investigating "Nearby Results 10km Away" Reports
If operational monitoring or user reports indicate spatial anomalies (e.g., results returned 10 km away from search location):

1. **Coordinate Parameter Alignment:** Verify whether `latitude` and `longitude` parameters were inadvertently transposed between client payloads and native SQL bindings (`SIN(RADIANS(:lat))` vs `SIN(RADIANS(:lng))`). Inverted coordinates alter spherical distance equations significantly.
2. **Radius Unit Validation:** Confirm whether distance parameters are specified in kilometers versus meters. Passing `3.0` under an engine expecting meters restricts searches to 3 meters, whereas passing `3000` to an engine expecting kilometers shifts spatial bounds dramatically.
3. **SVY21 to WGS84 Geocoding Accuracy:** Audit the ingestion pipeline converting HDB SVY21 national grid coordinates into WGS84 latitude/longitude decimal degrees to rule out projection drift.

### 3. Future Improvements Given More Time
* **PostGIS Spatial R-Tree Indexing:** Transition from H2 to PostgreSQL/PostGIS using `GEOGRAPHY(Point, 4326)` columns and spatial R-Tree indexing (`ST_DWithin`). This replaces trigonometric table scans with $O(\log N)$ spatial index lookups.
* **Redis GeoSpatial Caching:** Implement a Redis caching layer using `GEOADD` and `GEORADIUS` to serve real-time availability lookups with sub-millisecond responses.