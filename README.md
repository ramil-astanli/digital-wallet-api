# Digital Wallet Management System (RESTful API)

This project is a robust, production-ready **Digital Wallet Backend** built with **Spring Boot 3.4**. It manages financial accounts, handles multi-currency transactions, and ensures data integrity with manual transaction management and soft-delete capabilities.



## Professional Highlights & Features

* **Soft-Delete Architecture**: Implemented a custom deactivation/restoration logic for wallets, ensuring data is never accidentally lost while keeping the active list clean.
* **Transactional Integrity**: Money transfers are wrapped in `@Transactional` to ensure atomicity—if one part fails, the whole operation rolls back.
* **Containerized Environment**: Fully orchestrated with **Docker** and **Docker Compose**, separating the application logic from the **PostgreSQL** database.
* **Validation & Error Handling**: Global exception handling for business logic (e.g., "Insufficient Funds", "Wallet Not Found") and Jakarta Validation for API inputs.
* **Microservice Ready**: Designed with a decoupled structure, ready to be migrated to a Spring Cloud environment (Eureka, Gateway).



## 🚀 Advanced Features & Production Readiness

* **Secure Infrastructure**: Orchestrated with **Nginx** as a reverse proxy to handle incoming traffic and mask the internal Spring Boot architecture.
* **Stateless JWT Security**: Fully integrated **Spring Security 6** with JWT-based authentication, implementing a stateless architecture for better scalability.
* **Resilience & Fault Tolerance**:
    * **Spring Retry**: Implemented exponential backoff strategies for critical operations like Wallet creation.
    * **Automated Error Recovery**: Failed background tasks are automatically captured in a `failed_operation` log and triggers an instant **SMTP alert** to the administrator.
* **Event-Driven Wallet Provisioning**: Utilizes `ApplicationEventPublisher` and `@TransactionalEventListener` to decouple user registration from financial account setup, ensuring high performance.
## Tech Stack

* **Backend**: Java 21, Spring Boot 4.0.1
* **Data**: Spring Data JPA, Hibernate 7, PostgreSQL
* **Mapping**: MapStruct (for clean DTO/Entity separation)
* **DevOps**: Docker, Docker Compose
* **Tooling**: Lombok, Gradle, Postman

---

## Getting Started (Docker)

No need for local database setup! Run the entire stack with one command:

```bash
docker-compose up --build