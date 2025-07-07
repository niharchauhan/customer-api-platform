# Step 2: Integration Testing

---

## Testing Strategy

We adopted a **pattern-based testing approach** using `@SpringBootTest` and `@AutoConfigureMockMvc` annotations to enable integration testing for our Spring Boot API.

We covered:

- All CRUD operations for `/api/customers`
- Error handling scenarios:
  - Duplicate email address
  - Invalid or non-existent customer ID for GET/PUT/DELETE

---

## How to Run Integration Tests

Make sure MySQL is running and configured correctly, then run:

```bash
cd customer-api-service
mvn test -Dspring.profiles.active=test
```

Maven will automatically run all unit and integration test cases.

---

## CI/CD Automation

Integration tests are automatically triggered in the CI/CD pipeline during the build phase using GitHub Actions. This ensures that any code changes are verified through testing before progressing to later stages like packaging or deployment.

```yaml
- name: Build with Maven
  run: mvn clean install
```

#### What This Step Does:

- Cleans up any previously compiled classes or cached data to ensure a clean build environment.
- Compiles the latest source code and resolves dependencies using Maven.
- Executes all defined unit and integration test cases to validate business logic and API behavior.
- Immediately fails the pipeline if any test fails, preventing unstable or broken code from being deployed to staging or production environments.  

---
