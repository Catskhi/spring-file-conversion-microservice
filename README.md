# File Convert System

A microservices-based system for file conversion, user management, and email notifications. Built with Spring Boot, each service is independently deployable and communicates via messaging and REST APIs.

## Project Structure

- **mail/**: Handles email notifications and messaging.
- **processing/**: Manages file conversion and processing tasks.
- **user/**: Manages user accounts and authentication.

## Prerequisites

- Java 17+
- Maven
- Docker & Docker Compose

## Getting Started

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Catskhi/spring-file-conversion-microservice
   cd file_convert_system
   ```

2. **Build the services:**
   ```bash
   cd mail && ./mvnw clean package
   cd ../processing && ./mvnw clean package
   cd ../user && ./mvnw clean package
   ```

3. **Run with Docker Compose:**
   ```bash
   docker-compose up --build
   ```
   Run this command in each service directory or configure a root-level compose file.

## Usage

- Access service APIs via their respective ports (see each service's README or `application.yml`).
- Interact with the system using REST clients (Postman, curl) or integrate with your frontend.

## Configuration

- Service-specific settings are in `src/main/resources/application.yml`.
- Database migrations are managed with Flyway (`db/migration/`).

## Contributing

Pull requests are welcome. For major changes, open an issue first to discuss what you would like to change.

## License

This project is licensed under the MIT License.

