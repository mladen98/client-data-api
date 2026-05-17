# Client Data API

Eine RESTful API zur Verwaltung und zum Abrufen von Kundendaten, entwickelt mit Spring Boot und MySQL.

## 📋 Inhaltsverzeichnis

- [Überblick](#überblick)
- [Technologie-Stack](#technologie-stack)
- [Voraussetzungen](#voraussetzungen)
- [Installation](#installation)
- [Konfiguration](#konfiguration)
- [Verwendung](#verwendung)
- [API-Endpoints](#api-endpoints)
- [Projektstruktur](#projektstruktur)
- [Architektur-Übersicht](#🏗️-architektur-übersicht)
- [Code-Erklärungen](#💻-code-erklärungen)
- [Testing](#🧪-testing)
- [Deployment-Architektur](#📊-deployment-architektur)
- [Fehlerbehandlung](#fehlerbehandlung)
- [Sicherheit](#🔐-sicherheit)
- [Troubleshooting](#🐛-troubleshooting)

## 🎯 Überblick

Die Client Data API ist eine Spring Boot REST-Anwendung, die Kundendaten aus einer MySQL-Datenbank abruft und bereitstellt. Sie bietet sichere und effiziente Endpoints für den Zugriff auf Kundeninformationen.

## 🛠️ Technologie-Stack

| Technologie | Version |
|------------|---------|
| **Java** | 11 |
| **Spring Boot** | 2.3.4.RELEASE |
| **MySQL** | Latest (JDBC) |
| **Maven** | 3.6+ |
| **JUnit** | 5 |

**Abhängigkeiten:**
- `spring-boot-starter-web` - REST API Support
- `spring-boot-starter-jdbc` - Datenbankzugriff
- `mysql-connector-java` - MySQL Driver
- `spring-boot-starter-test` - Testing Framework

## 📦 Voraussetzungen

- **Java 11** oder höher
- **Maven 3.6** oder höher
- **MySQL Server** (mit VPN-Zugang für FHNW Server)
- **Git** (optional)

## 🚀 Installation

### 1. Repository klonen
```bash
git clone <repository-url>
cd client-data-api
```

### 2. Abhängigkeiten herunterladen
```bash
./mvnw clean install
```

Oder unter Windows:
```bash
mvnw.cmd clean install
```

### 3. Anwendung starten
```bash
./mvnw spring-boot:run
```

Die API läuft dann unter: `http://localhost:8082`

## ⚙️ Konfiguration

Die Konfiguration befindet sich in `src/main/resources/application.properties`:

```properties
# Server-Port
server.port=8082

# Datenbank-Verbindung
spring.datasource.url=jdbc:mysql://192.168.111.13/vl_custmgmt?allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=vl_custmgmt
spring.datasource.password=d854hg23t48+f2z-fvtz8tb0b4v
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

**Wichtig:** Die Datenbankverbindung ist nur via VPN erreichbar.

### Umgebungsspezifische Konfiguration

Für verschiedene Umgebungen können separate Dateien erstellt werden:
- `application-dev.properties` - Entwicklung
- `application-prod.properties` - Produktion

Mit `-Dspring.profiles.active=dev` können diese aktiviert werden.

## 📖 Verwendung

### Anwendung starten
```bash
./mvnw spring-boot:run
```

### Tests ausführen
```bash
./mvnw test
```

### Build erstellen
```bash
./mvnw clean package
```

Das JAR-File wird in `target/` erstellt.

## 🔌 API-Endpoints

### Kundendaten abrufen
```http
GET /api/customers/{customerReference}
```

**Parameter:**
- `customerReference` (String, erforderlich) - Die Referenznummer des Kunden

**Response (200 OK):**
```json
{
  "customerReference": "12345",
  "destination": "Musterstraße 1, 1234 Musterstadt",
  "recepientPhone": "+41 44 123 45 67",
  "email": "customer@example.com"
}
```

**Error Responses:**

| Status | Fehler | Beschreibung |
|--------|--------|-------------|
| 404 | `CustomerNotFoundException` | Kunde nicht gefunden |
| 500 | `InternalServerError` | Interner Serverfehler |

**Beispiele:**

```bash
# Mit curl
curl http://localhost:8082/api/customers/12345

# Mit httpie
http GET http://localhost:8082/api/customers/12345
```

## 📂 Projektstruktur

```
src/
├── main/
│   ├── java/ch/fhnw/case6/clientdata/
│   │   ├── ClientDataApiApplication.java       # Haupteinstiegspunkt
│   │   ├── api/
│   │   │   └── CustomerController.java         # REST Controller
│   │   ├── dto/
│   │   │   └── CustomerData.java               # Data Transfer Object
│   │   ├── exception/
│   │   │   ├── CustomerNotFoundException.java  # Custom Exception
│   │   │   └── GlobalExceptionHandler.java     # Globaler Exception Handler
│   │   ├── repository/
│   │   │   └── CustomerRepository.java         # Datenbankzugriffsschicht
│   │   └── service/
│   │       └── CustomerService.java            # Business Logic
│   └── resources/
│       └── application.properties              # Konfigurationsdatei
└── test/
    └── java/ch/fhnw/case6/clientdata/
        └── ClientDataApiApplicationTests.java  # Unit Tests
```

## 🏗️ Architektur-Übersicht

### Schichtenmodell (Layered Architecture)

```
+-------------------------------------------------------+
|  HTTP Client (Browser, cURL, etc.)                  |
+---------------------------+---------------------------+
                            |
                            v HTTP GET /api/customers/{id}
+-------------------------------------------------------+
| +----------------------------------------------+    |
| |  API-Layer (Presentation)                    |    |
| |  CustomerController.java                     |    |
| |  +- @GetMapping("/api/customers/{ref}")     |    |
| |  +- Verarbeitet HTTP-Requests                |    |
| +-------------------+--------------------------+    |
+---------------------+-----------------------------+
| +----------------------------------------------+    |
| |  Service-Layer (Business Logic)              |    |
| |  CustomerService.java                        |    |
| |  +- getCustomerData(reference)              |    |
| |  +- Validierungen                           |    |
| |  +- Exception-Handling                       |    |
| +-------------------+--------------------------+    |
+---------------------+-----------------------------+
| +----------------------------------------------+    |
| |  DTO (Data Transfer Object)                  |    |
| |  CustomerData.java                           |    |
| |  +- customerReference: String                |    |
| |  +- destination: String                      |    |
| |  +- recepientPhone: String                   |    |
| |  +- email: String                            |    |
| +-------------------+--------------------------+    |
+---------------------+-----------------------------+
| +----------------------------------------------+    |
| |  Repository-Layer (Data Access)              |    |
| |  CustomerRepository.java                     |    |
| |  +- findByCustomerReference(reference)      |    |
| |  +- JDBC/SQL-Zugriff                        |    |
| +-------------------+--------------------------+    |
|                     |                                |
| Spring Boot Container                               |
+---------------------+-----------------------------+
                      |
                      v JDBC
+-------------------------------------------------------+
|  MySQL Datenbank (FHNW Server)                      |
|  +- customer (Tabelle)                              |
|  +- customerAddress (Tabelle)                       |
|  +- JOIN-Abfrage: findByCustomerReference          |
+-------------------------------------------------------+
```

### Request/Response Flow

```
CLIENT REQUEST
─────────────────────────────────────────────────────────

1. HTTP GET http://localhost:8082/api/customers/12345
   │
   └──► CustomerController.getCustomerData("12345")
        │
        └──► CustomerService.getCustomerData("12345")
             │
             ├─ Validierung der Eingabe
             │
             └──► CustomerRepository.findByCustomerReference("12345")
                  │
                  └──► MySQL-Query ausführen
                       SELECT c.custId, a.street, a.zip, a.town, c.phone, c.email
                       FROM customer c
                       JOIN customerAddress a ON c.custId = a.custId
                       WHERE c.custId = '12345'

─────────────────────────────────────────────────────────
RESPONSE

✓ Erfolgreich (200 OK):
  {
    "customerReference": "12345",
    "destination": "Musterstraße 1, 1234 Musterstadt",
    "recepientPhone": "+41 44 123 45 67",
    "email": "customer@example.com"
  }

✗ Fehler (404 Not Found):
  CustomerNotFoundException
  └─ GlobalExceptionHandler
     └─ Error Response JSON mit Fehlermeldung
```

### Component Interaction

```
+---------------------------------------------------+
|    ClientDataApiApplication                     |
|     (Main Application Entry)                    |
|    @SpringBootApplication                       |
+-------------------+-----------------------------+
                    |
      +---------+---+---------+
      |                       |
      v                       v
+----------+          +----------+
| Customer | | Global |
| Controller| | Exception|
|           | | Handler |
| @RestCtrl | |           |
| +- getCust| | @ControllerAdvice |
|   Data()  | | +- handle Customer |
+-------+-+ | |   NotFound     |
        |   | +- handle Generic  |
        |   |    Exceptions    |
        |   +----------+
        |
        +-> @Inject CustomerService
        |
        v
+-------------------------------------+
| CustomerService                     |
|                                     |
| +- getCustomerData(reference)      |
| |  +- Validation                  |
| |  +- Service Logic               |
| |  +- Error Handling              |
| |                                 |
| +-> @Inject CustomerRepository     |
+--------+------------------------+
         |
         v
+-------------------------------------+
| CustomerRepository                  |
|                                     |
| +- findByCustomerReference(ref)   |
| |  +- SQL Query Building          |
| |  +- JDBC Execution             |
| |                                 |
| +-> JdbcTemplate (Spring JDBC)     |
+--------+------------------------+
         |
         v
      +----------+
      | MySQL DB |
      |          |
      | Queries: |
      | - SELECT |
      | - JOIN   |
      | - WHERE  |
      +----------+
```

### DTO Mapping

```
MySQL Result Set (Raw JDBC Result)
===================================
{
  custId: "12345",
  street: "Musterstraße 1",
  zip: "1234",
  town: "Musterstadt",
  phone: "+41 44 123 45 67",
  email: "customer@example.com",
  type: "shipping"
}
         |
         | Repository: resultSet.getString("...")
         v
+-------------------------------------------+
| CustomerData (DTO)                        |
| =========================================|
| - customerReference: "12345"              |
| - destination: "Musterstraße 1, 1234..." |
| - recepientPhone: "+41 44 123 45 67"     |
| - email: "customer@example.com"           |
+-------------------------------------------+
         |
         | Controller: ResponseEntity<CustomerData>
         v
+-------------------------------------------+
| JSON Response (HTTP)                      |
| =========================================|
| {                                         |
|   "customerReference": "12345",           |
|   "destination": "Musterstraße 1...",    |
|   "recepientPhone": "+41 44 123 45 67",  |
|   "email": "customer@example.com"         |
| }                                         |
+-------------------------------------------+
```

## 💻 Code-Erklärungen

### 1️⃣ CustomerController - REST API Entry Point

**Datei:** `src/main/java/ch/fhnw/case6/clientdata/api/CustomerController.java`

```java
@RestController                              // ← Markiert diese Klasse als REST Controller
public class CustomerController {

    private final CustomerService customerService;

    // Constructor Injection - Spring injiziert die Abhängigkeit automatisch
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/api/customers/{customerReference}")  // ← HTTP GET Endpoint
    public CustomerData getCustomerData(
        @PathVariable String customerReference         // ← URL-Parameter extrahieren
    ) {
        return customerService.getCustomerData(customerReference);  // ← Service aufrufen
    }
}
```

**Ablauf:**
1. HTTP-Request kommt an: `GET /api/customers/12345`
2. Spring mapped das zu dieser Methode
3. `customerReference` erhält den Wert `12345`
4. Service wird aufgerufen zur Verarbeitung

---

### 2️⃣ CustomerService - Business Logic Layer

**Datei:** `src/main/java/ch/fhnw/case6/clientdata/service/CustomerService.java`

```java
@Service                                     // ← Spring verwaltet diese Klasse
public class CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public CustomerData getCustomerData(String customerReference) {
        // 1. Validierung
        if (customerReference == null || customerReference.isEmpty()) {
            throw new IllegalArgumentException("Customer Reference ist erforderlich");
        }

        // 2. Daten abrufen
        CustomerData customer = customerRepository
            .findByCustomerReference(customerReference);

        // 3. Exception werfen wenn nicht gefunden
        if (customer == null) {
            throw new CustomerNotFoundException(
                "Kunde mit Reference " + customerReference + " nicht gefunden"
            );
        }

        return customer;  // ← Response wird zu JSON konvertiert
    }
}
```

**Verantwortlichkeiten:**
- ✓ Validierung der Eingaben
- ✓ Business Logic
- ✓ Exception Handling
- ✓ Repository-Aufrufe koordinieren

---

### 3️⃣ CustomerRepository - Database Access Layer

**Datei:** `src/main/java/ch/fhnw/case6/clientdata/repository/CustomerRepository.java`

```java
@Repository                                  // ← Spring verwaltet als Repository Bean
public class CustomerRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;       // ← Spring bietet QueryBuilder

    private static final String SQL_QUERY = 
        "SELECT c.custId AS customerReference, " +
        "CONCAT(a.street, ', ', a.zip, ' ', a.town) AS destination, " +
        "c.phone AS recepientPhone, c.email AS email " +
        "FROM customer c " +
        "JOIN customerAddress a ON c.custId = a.custId " +
        "WHERE c.custId = ? AND a.type = 'shipping' " +
        "LIMIT 1";

    public CustomerData findByCustomerReference(String reference) {
        try {
            // SQL-Query mit Parameterbinding (→ schützt vor SQL-Injection)
            return jdbcTemplate.queryForObject(
                SQL_QUERY,
                new Object[]{reference},  // ← Parameter
                rowMapper()               // ← ResultSet zu CustomerData
            );
        } catch (EmptyResultDataAccessException e) {
            return null;  // ← Keine Ergebnisse gefunden
        }
    }

    // Mapped Datenbankzeilen zu CustomerData Objekten
    private RowMapper<CustomerData> rowMapper() {
        return (rs, rowNum) -> CustomerData.builder()
            .customerReference(rs.getString("customerReference"))
            .destination(rs.getString("destination"))
            .recepientPhone(rs.getString("recepientPhone"))
            .email(rs.getString("email"))
            .build();
    }
}
```

**Aufgaben:**
- ✓ SQL-Queries definieren
- ✓ JDBC-Aufrufe ausführen
- ✓ ResultSets zu Objekten mappen
- ✓ Datenbankfehler behandeln

---

### 4️⃣ CustomerData - Data Transfer Object (DTO)

**Datei:** `src/main/java/ch/fhnw/case6/clientdata/dto/CustomerData.java`

```java
@Data                                        // ← Lombok: Getter, Setter, toString
@Builder                                     // ← Builder-Pattern für Konstruktion
@NoArgsConstructor                           // ← Parameterloser Konstruktor
@AllArgsConstructor                          // ← Konstruktor mit allen Feldern
public class CustomerData {

    private String customerReference;        // ← Kunden-ID
    private String destination;              // ← Formatierte Adresse
    private String recepientPhone;           // ← Telefonnummer
    private String email;                    // ← Email-Adresse
}
```

**Nutzen:**
- ✓ Strukturiert Daten für API-Response
- ✓ Lombok generiert Boilerplate-Code
- ✓ Wird zu JSON serialisiert/deserialisiert

**Automatisierte JSON-Umwandlung:**
```json
{
  "customerReference": "12345",
  "destination": "Musterstraße 1, 1234 Musterstadt",
  "recepientPhone": "+41 44 123 45 67",
  "email": "customer@example.com"
}
```

---

### 5️⃣ GlobalExceptionHandler - Error Handling

**Datei:** `src/main/java/ch/fhnw/case6/clientdata/exception/GlobalExceptionHandler.java`

```java
@ControllerAdvice                            // ← Globale Exception-Behandlung
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)    // ← HTTP 404 Status
    public ErrorResponse handleCustomerNotFound(
        CustomerNotFoundException ex
    ) {
        return ErrorResponse.builder()
            .status(404)
            .message(ex.getMessage())
            .timestamp(LocalDateTime.now())
            .build();
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  // ← HTTP 500 Status
    public ErrorResponse handleGenericException(Exception ex) {
        return ErrorResponse.builder()
            .status(500)
            .message("Ein interner Fehler ist aufgetreten")
            .timestamp(LocalDateTime.now())
            .build();
    }
}
```

**Fehlerbehandlung:**
- ✓ Abfangen von Exceptions
- ✓ Strukturierte Fehler-Response
- ✓ Passender HTTP-Status Code

---

## 🔍 Fehlerbehandlung

Die Anwendung implementiert einen globalen Exception Handler in `GlobalExceptionHandler.java`:

- **CustomerNotFoundException** - Wird ausgelöst, wenn ein Kunde nicht gefunden wird
- **SQLException** - Bei Datenbankfehlern
- **IllegalArgumentException** - Bei ungültigen Eingaben

Fehlerresponses enthalten aussagekräftige JSON-Meldungen mit HTTP-Statuscode.

## 🧪 Testing

Unit Tests befinden sich in `src/test/java`:

```bash
# Alle Tests ausführen
./mvnw test

# Einen spezifischen Test ausführen
./mvnw test -Dtest=ClientDataApiApplicationTests
```

### Test-Abdeckung

```
+-----------------------------------+
| Test-Pyramide                     |
+-----------------------------------+
|     E2E Tests (selten)            |  <- API-Integration Tests
+-----------------------------------+
|   Integration Tests               |  <- Controller + Service + DB
+-----------------------------------+
| Unit Tests (viele, schnell)       |  <- Service, Repository, DTO
+-----------------------------------+
|Komponenten-Tests                  |  <- Einzelne Methoden
+-----------------------------------+
```

---

## 📊 Deployment-Architektur

```
+-------------------------------------------+
| Externe Clients                           |
| (Web, Mobile, Tools)                      |
+----------+---------+----------+
           |
     +-----+------+
     | HTTPS/HTTP |
     +-----+------+
           |
+----------v-------------------------------------------+
| Server (Port 8082)                                   |
| +----------------------------------------------+    |
| | Spring Boot Application                      |    |
| | +- Embedded Tomcat                          |    |
| | +- Spring Framework                         |    |
| | +- Application Logic                        |    |
| |   (Controller, Service, etc)                |    |
| +----------+-------------------------------+    |
+-----------+------------------------------------------+
            |
      +-----+--------+
      | JDBC         |
      | (Port 3306)  |
      +-----+--------+
            |
+----------v-------------------------------------------+
| MySQL Database Server                               |
| (FHNW - nur via VPN erreichbar)                     |
| +----------------------------------------------+    |
| | vl_custmgmt (Database)                       |    |
| | +- customer (Table)                          |    |
| | |  +- custId (PK)                           |    |
| | |  +- phone                                  |    |
| | |  +- email                                  |    |
| | +- customerAddress (Table)                  |    |
| |    +- custId (FK)                           |    |
| |    +- street, zip, town                     |    |
| |    +- type (shipping/billing)               |    |
| +----------------------------------------------+    |
+---------------------------------------------------+
```

---

## 📈 Complete Request Flow Diagramm

```
+-----------------------------------------------------+
| CLIENT REQUEST: GET /api/customers/12345           |
+------------------+--------------------------------+
                   |
                   v
      +----------------------------+
      | Spring DispatcherServlet    |  <- URL-Pattern Matching
      | (Central Request Handler)   |
      +----------+-------------------+
                 |
                 v
      +-------------------------------+
      | CustomerController            |
      | @GetMapping("/api/customers") |
      |                               |
      | 1) Empfaengt: ref = "12345"  |
      | 2) Ruft auf: customerService.|
      |    getCustomerData("12345")   |
      | 3) Erwartet: CustomerData    |
      +----------+--------------------+
                 |
                 v
      +-------------------------------+
      | CustomerService               |
      |                               |
      | 1) Validiert: ref!=null OK    |
      |    OK) Validation erfolgreich |
      |                               |
      | 2) Ruft auf: customerRepo.   |
      |    findByCustomerReference    |
      | 3) Erwartet: CustomerData    |
      +----------+--------------------+
                 |
                 v
      +-------------------------------+
      | CustomerRepository            |
      |                               |
      | 1) Praepariert SQL-Query:    |
      |    SELECT c.custId,           |
      |    CONCAT(...) AS dest,       |
      |    c.phone, c.email           |
      |    FROM customer c            |
      |    JOIN customerAddress a     |
      |    WHERE c.custId = '12345'  |
      |                               |
      | 2) Faehrt aus: jdbcTemplate.  |
      |    queryForObject(...)        |
      | 3) Mapped ResultSet           |
      | 4) Gibt CustomerData zurück   |
      +----------+--------------------+
                 |
         +-------v---------+
         | MySQL Execution |
         +-------+----------+
                 |
         +-------v---------+
         | Erfolg OK       |
         +-------v---------+
                 |
      +-------------------------------+
      | CustomerData DTO              |
      | {                             |
      |   customerReference: "12345", |
      |   destination: "Str 1, XY",  |
      |   recepientPhone: "+41...",  |
      |   email: "cust@example.com"  |
      | }                             |
      +----------+--------------------+
                 |
                 v
      +-------------------------------+
      | GlobalExceptionHandler        |
      | - Keine Exception             |
      | - HTTP 200 OK Response        |
      +----------+--------------------+
                 |
                 v
      +-------------------------------+
      | Spring ContentNegotiation     |
      | Konvertiert zu JSON           |
      | (@RestController auto)        |
      +----------+--------------------+
                 |
                 v
      +-------------------------------+
      | HTTP 200 OK Response          |
      | Content-Type: appl/json       |
      |                               |
      | {                             |
      |   "customerReference":...     |
      |   "destination": "...",       |
      |   "recepientPhone": "...",    |
      |   "email": "..."              |
      | }                             |
      +----------+--------------------+
                 |
                 v
      +-------------------------------+
      | CLIENT RECEIVES RESPONSE      |
      | OK) Erfolg!                   |
      +-------------------------------+
```

### Fehler-Szenario

```
+-----------------------------------------------------+
| CLIENT REQUEST: GET /api/customers/INVALID         |
+------------------+--------------------------------+
                   |
                   v (wie oben bis Service...)
                   |
      +-------------------------------+
      | CustomerService               |
      |                               |
      | 1) Validiert: INVALID!=null   |
      | 2) Repository aufrufen...     |
      | 3) Erhaelt: null              |
      | 4) if (null) -> Exception!    |
      |                               |
      |    throw new                  |
      |    CustomerNotFound           |
      |    Exception(...)             |
      +----------+--------------------+
                 |
                 v Exception nach oben
      +-------------------------------+
      | GlobalExceptionHandler        |
      | @ExceptionHandler(            |
      |   CustomerNotFound...)        |
      |                               |
      | Faengt Exception ab OK        |
      | Status: HTTP 404 NOT FOUND    |
      | Response:                     |
      | {                             |
      |   "status": 404,              |
      |   "message": "Kunde mit       |
      |    Reference INVALID...",     |
      |   "timestamp": "2026-..."     |
      | }                             |
      +----------+--------------------+
                 |
                 v
      +-------------------------------+
      | CLIENT RECEIVES 404 ERROR     |
      | X) Kunde nicht gefunden       |
      +-------------------------------+
```

## 🔐 Sicherheit

- Verwende nach Möglichkeit Umgebungsvariablen für sensitive Daten (API Keys, Passwörter)
- Aktiviere SSL/TLS in Produktionsumgebungen
- Implementiere API-Key oder OAuth 2.0 Authentication bei Bedarf
- Nutze HTTPS statt HTTP

## 🐛 Troubleshooting

### Datenbankverbindung fehlgeschlagen
- Stelle sicher, dass das VPN verbunden ist
- Überprüfe die Datenbankanmeldedaten in `application.properties`
- Verifiziere die MySQL-Serveradresse und den Port

### Port 8082 bereits in Verwendung
```bash
# Unter Windows
netstat -ano | findstr :8082

# Port wechseln (in application.properties)
server.port=8083
```

### Build fehlgeschlagen
```bash
# Cache löschen und neu bauen
./mvnw clean -DskipTests=true install
```

## 👨‍💻 Autoren 

- Entwicklung: FHNW Case Study 6

---

**Letzte Aktualisierung:** Mai 2026

