
# Rahim's Gold Tracking REST API

This project implements a Gold Tracking REST API using Spring Boot. It allows users to add different types of gold items and track their prices, which are updated every minute using a custom-built Python API that performs web scraping. The architecture follows a microservices pattern with an event-driven approach.

## Features
### Database:
- Utilise Flyway Migration to execute SQL scripts for database schema management and versioning

### Devops:
- Helm charts used to deploy application on cloud(MiniKube)

### Email Notifications:
Send email notifications for account changes and updates.
Send email notifications for price alerts when specific conditions are met.

### Eureka Service Discovery:
- Service Discovery: Allows microservices to find and communicate with each other without hard-coded URLs.
- Service Registration: Automatically registers microservices.

### Gateway Service:
- API Gateway: Acts as a single entry point for all microservices.
- Load Balancing: Distributes incoming traffic across multiple instances of microservices.
- Security: Provides authentication and authorization for API access.

### Investment Service:
- Profit/Loss Calculation: Calculate profit or loss based on current gold prices.
- User Holdings: Track user gold holdings and investments.

### Notification Service:
- Customisable Alerts: Configure thresholds and conditions for price notifications.
- Price Alerts: Send email notifications when specific gold prices are hit.

### Pricing Service:
- Able to update all gold types with new price.
- Ability to retrieve updated gold prices.

### Python Api:
- Implements a FastAPI-based API to retrieve the latest gold price and allow requests from Spring
- Datasources configurable via database changes for flexible data retrieval
- Utilises encryption/decryption capabilities using Vault for secure data handling

### Reporting Service (TBD):
- Generate investment reports.

### Scheduler Service:
- Cron Jobs Management: Initiates cron jobs in microservices via Kafka messages.
- Event-Driven: Uses Kafka for communication to trigger actions across microservices.
- Periodic Tasks: Executes scheduled tasks such as updating gold prices or purging inactive accounts.

### Spring Config Server:
- Centralised Configuration: Manages and distributes configuration settings to microservices.
- Dynamic Updates: Allows for runtime configuration changes without restarting microservices.

### Technologies Used:
- Java 17
- Python 3.11
- Docker
- Spring Boot
- Spring Batch
- Spring Config Server
- Netflix Eureka Discovery
- Thymeleaf
- JavaMail API
- Hazelcast
- Hashicorp Vault
- Kafka
- MiniKube
