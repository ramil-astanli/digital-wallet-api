This project is a robust, production-ready Digital Wallet Backend built with Spring Boot 3.4.1. It manages financial accounts, handles multi-currency transactions, and ensures data integrity with manual transaction management and soft-delete capabilities.
🌟 Professional Highlights & Features

    Soft-Delete Architecture: Implemented custom deactivation/restoration logic for wallets, ensuring data is never accidentally lost while keeping the active list clean.

    Transactional Integrity: Money transfers are wrapped in @Transactional to ensure atomicity—if one part fails, the whole operation rolls back.

    Containerized Environment: Fully orchestrated with Docker and Docker Compose, separating application logic from the PostgreSQL database.

    Validation & Error Handling: Global exception handling for business logic (e.g., "Insufficient Funds") and Jakarta Validation for API inputs.

    Microservice Ready: Designed with a decoupled structure, ready to be migrated to a Spring Cloud environment.

🚀 Advanced Features & Production Readiness

    Secure Infrastructure: Orchestrated with Nginx as a reverse proxy to mask the internal Spring Boot architecture.

    Stateless JWT Security: Fully integrated Spring Security 6 with JWT-based authentication for better scalability.

    Resilience & Fault Tolerance:

        Spring Retry: Exponential backoff strategies for critical operations like Wallet creation.

        Automated Error Recovery: Failed background tasks are captured in a failed_operation log and trigger an instant SMTP alert to the administrator.

    Event-Driven Wallet Provisioning: Utilizes ApplicationEventPublisher and @TransactionalEventListener to decouple user registration from financial account setup.

🛠 Tech Stack

    Backend: Java 21, Spring Boot 3.4.1

    Data: Spring Data JPA, Hibernate 6.6, PostgreSQL, Redis

    Mapping: MapStruct (for clean DTO/Entity separation)

    DevOps: Docker, Docker Compose, Nginx

    Tooling: Lombok, Gradle, Postman

⚙️ Configuration & Environment Variables

For security reasons, sensitive information like SMTP passwords and JWT keys are not hardcoded. You need to set these variables to run the project.
Option 1: Using a .env file (Recommended for Docker)

Create a file named .env in the root directory:
Kod hissəsi

# Database Credentials
DB_USER=ramoo19
DB_PASSWORD=12341234

# Security & Mail (REQUIRED)
# Go to Google Account -> Security -> 2-Step Verification -> App Passwords
SMTP_PASS=your_16_character_app_password

# JWT Secret Key
JWT_SECRET=ZmFzdC13YWxsZXQtYXBwLXNlY3JldC1rZXktZm9yLTIwMjY=

📡 API Endpoints & Usage

All endpoints require a JWT Bearer Token in the header: Authorization: Bearer <your_token>.
👛 Wallet Management
Method	Endpoint	Description
POST	/wallets	Create a new digital wallet
GET	/wallets	Retrieve all active wallets for current user
GET	/wallets/{id}	Get detailed information of a specific wallet
DELETE	/wallets/{id}	Deactivate a wallet (Soft-delete)
PATCH	/wallets/{id}/restore	Restore a previously deactivated wallet
💸 Financial Operations
Method	Endpoint	Description
POST	/wallets/transfer	Execute a secure money transfer
GET	/wallets/transactions/{walletId}	Fetch transaction history
🚀 Getting Started (Docker)

No need for local database setup! Run the entire stack with one command:

    Clone the repository:
    Bash

    git clone https://github.com/ramil_astanli/wallet-management-system.git
    cd wallet-management-system

    Run the stack:
    Bash

    docker-compose up --build -d

    Access:

        API Gateway (Nginx): http://localhost/wallets

        Direct API: http://localhost:8080/wallets

📝 Example Request (Money Transfer)

POST /wallets/transfer
JSON

{
  "fromWalletId": 1,
  "toWalletId": 2,
  "amount": 100.0,
  "currency": "USD"
}

## ⚖️ License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
