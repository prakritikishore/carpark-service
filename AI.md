# AI Collaboration Log

## 1. Setup & Environment
* **Tooling:** Cursor / GitHub Copilot (powered by Claude 3.5 Sonnet / Gemini models).
* **Context Provided:**
    * Application layer: Java 17, Spring Boot 3.x, Spring Data JPA, Lombok.
    * Spatial query specification using native SQL projections and Haversine formula bounds.

---

## 2. Delegation Strategy
* **Delegated to Agent:**
    * Drafting initial native SQL Haversine spatial query structure.
    * Writing standard DTO and Entity boilerplate representations.
    * Generating initial unit/integration test structure and `curl` testing commands.
* **Handled Manually:**
    * Diagnosing integration test execution failures related to native SQL L1 cache bypass.
    * Refining outer subquery scoping (`sub.lotsAvailable`) to ensure proper H2 dialect parsing.
    * Defining API response contract envelopes (`resultCount`, `page`, `limit`).

---

## 3. Verification & Safety Checks
* **Review Process:**
    * Every line of generated code—especially SQL spatial logic and test assertions—was manually reviewed and validated.
    * **Reasoning:** Mathematical calculations in SQL (like spherical distance) can produce subtle logical flaws (e.g., coordinate axis swapping, unit conversion mismatches) that pass naive tests but break under real spatial workloads.
* **Automated Diagnostics:**
    * Continuous test execution using JUnit 5 integration test suites (`@SpringBootTest`).
    * Direct H2 database row counting and SQL result inspections to verify entity state flushing.

---

## 4. Concrete Bug Fix Example

### Initial Agent Output
The agent provided the following native SQL `WHERE` clause for filtering available car parks:
```sql
SELECT * FROM (
    ...
) sub
WHERE sub.distanceKm <= :radiusKm
  AND lotsAvailable > 0