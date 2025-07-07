# Step 1: Build an API

---

## Entity Definition

| Attribute     | Type    | Constraints        | Notes                         |
|---------------|---------|--------------------|-------------------------------|
| Id            | UUID    | Primary Key        | Auto-generated                |
| First Name    | String  | Not Null           | Required                      |
| Middle Name   | String  | Nullable           | Optional                      |
| Last Name     | String  | Not Null           | Required                      |
| Email Address | String  | Not Null, Unique   | Used to identify uniqueness   |
| Phone Number  | String  | Not Null           | Can be composite format       |

---

## How to Create the Database Manually via MySQL CLI

1. Open your terminal and login to MySQL:
```bash
mysql -u root -p
```

2. Once inside the MySQL shell, run the following command to create the database:
```sql
CREATE DATABASE customer_db;
```

3. You can check if the database was created:
```sql
SHOW DATABASES;
```

---

## Database Configuration

MySQL is used as the external database. Update your `application.properties` with:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/customer_db
spring.datasource.username=root
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

---

## CRUD Endpoints

| Method | Endpoint              | Description           |
|--------|------------------------|-----------------------|
| POST   | `/api/customers`       | Create new customer   |
| GET    | `/api/customers`       | Fetch all customers   |
| GET    | `/api/customers/{id}`  | Get customer by ID    |
| PUT    | `/api/customers/{id}`  | Update customer       |
| DELETE | `/api/customers/{id}`  | Delete customer       |

---

## Unit Testing

Unit tests are written using **JUnit 5** and **Mockito** for the `CustomerService` class:

---