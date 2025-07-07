# Step 4: Containerization

This step involves packaging the Customer API into a Docker container to ensure deployment across any environment and consistent environment management.

---

## Dockerfile

The project includes a `Dockerfile` that defines how to build the container:

---

## Building the Docker Image

Run the following command from the root of the `customer-api-service` project to build the image:

```bash
docker build -t customer-api-service .
```

---

## Running the Container

Use the command below to start the application container. We have used `host.docker.internal` as the hostname to allow the containerized Spring Boot application to connect to the MySQL database running on the host machine, rather than looking for the database inside the container.

```bash
docker run -d \
  -p 8080:8080 \
  -e DB_HOST=host.docker.internal \
  -e DB_PORT=3306 \
  -e DB_NAME=customer_db \
  -e DB_USER=root \
  -e DB_PASSWORD= \
  --name customer-api \
  customer-api-service
```

---

## Required Inputs (Environment Variables)

- `DB_HOST`: Hostname or IP of the MySQL server
- `DB_PORT`: Port of the MySQL service (typically `3306`)
- `DB_NAME`: Name of the database schema
- `DB_USER`: MySQL username
- `DB_PASSWORD`: MySQL user password

---

## Expected Outcome

The service will be accessible at: `http://localhost:8080/api/customers`

---
