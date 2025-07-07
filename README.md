# Customer API Platform

This project is a Java Spring Boot-based RESTful API that manages customer information. It includes:
- Full CRUD functionality
- Unit and integration tests
- Observability features
- Docker containerization
- Kubernetes deployment manifests
- CI/CD automation via GitHub Actions
- A separate CLI-based Java application to consume the API

---

## Prerequisites

Ensure the following tools are installed:

### Java 21
- Download: https://jdk.java.net/21/
- macOS: `brew install openjdk@21`

### Maven 3.9+
- Download: https://maven.apache.org/download.cgi
- macOS: `brew install maven`

### MySQL
- Download: https://dev.mysql.com/downloads/mysql/
- macOS: `brew install mysql`
- Start MySQL: `brew services start mysql`

### Docker
- Download & install Docker Desktop: https://www.docker.com/products/docker-desktop/

### Kubernetes CLI & Minikube
- Install `kubectl`: https://kubernetes.io/docs/tasks/tools/
- Install `minikube`: https://minikube.sigs.k8s.io/docs/start/

### Git
- Download: https://git-scm.com/downloads
- macOS: `brew install git`

---

## Database Setup

### Create MySQL Database Manually

Start MySQL and execute the following in the CLI:

```sql
CREATE DATABASE customer_db;
```

---

## Running the Application Locally

### Step 1: Configure MySQL

Update `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/customer_db
spring.datasource.username=root
spring.datasource.password=<your-password>
spring.jpa.hibernate.ddl-auto=update
```

### Step 2: Build and Run

```bash
mvn clean install
```

---

## Running Tests

### Unit + Integration Tests

```bash
mvn test -Dspring.profiles.active=test
```

Ensure your MySQL DB is running and credentials match `application-test.properties`.

---

## CLI-Based API Consumer

A separate Java CLI application is included in the project to interact with the RESTful Customer API.

The CLI application is located in the `customer-cli-consumer/` directory.

---

### How to Start the CLI Application

Before running the CLI app, ensure the **Customer API** service is up and running at:

Then do the following:

#### Step 1: Update the Base URL in CLI

In `src/main/java/cli/CommandLineApp.java`, update this line:

```java
private static final String BASE_URL = "http://<URL>/api/customers";
```

Make sure the URL matches the actual host and port where the Spring Boot API is running.

---

#### Step 2: Compile and Run the CLI

Navigate to the CLI directory:

```bash
cd customer-cli-consumer
```

Then compile the code:

```bash
javac -cp "lib/*" src/main/java/cli/CommandLineApp.java
```

Run the CLI application:

```bash
java -cp "lib/*:src/main/java" cli.CommandLineApp
```

---

## Docker Usage

### Build Docker Image

```bash
docker build -t customer-api-service .
```

### Run the Container

```bash
docker run -d \
  -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  -e DB_PORT=3306 \
  -e DB_NAME=customer_db \
  -e DB_USER=root \
  -e DB_PASSWORD=<your-password> \
  --name customer-api \
  customer-api-service
```

---

## Kubernetes Deployment

### Step 1: Start Minikube

```bash
minikube start
```

### Step 2: Apply Configs

```bash
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/deployment.yaml
kubectl apply -f k8s/service.yaml
```

---

## Observability

The application exposes metrics and logs using Spring Boot Actuator and Micrometer.

Visit:

```
http://localhost:8080/actuator/health  
http://localhost:8080/actuator/metrics
```

---

## CI/CD Pipeline

This project could use GitHub Actions:

- On push/pull request to `main`:
  - Build & test the code
  - Create Docker image
  - Deploy to Kubernetes using `kubectl apply`

Automated gates ensure code quality and controlled deployment.

Please look at `ci-cd-pipeline.yml` for details.

---

## API Endpoints

- `POST /api/customers`
- `GET /api/customers`
- `GET /api/customers/{id}`
- `PUT /api/customers/{id}`
- `DELETE /api/customers/{id}`

---
