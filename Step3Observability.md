# Step 3: API Observability

---

For Observability, we have included:
- **Logging**: To understand what's happening in the system.
- **Metrics**: To quantify performance and track behaviors.

---

## Logging Configuration

- Default logging is handled via SLF4J.
- Logs are automatically output to the console.

---

## Metrics via Micrometer + Prometheus

Micrometer is bundled with Spring Boot Actuator to provide metrics.

### Dependencies in `pom.xml`
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

### Configuration in `application.properties`
```properties
management.endpoints.web.exposure.include=*
management.endpoint.prometheus.enabled=true
management.metrics.export.prometheus.enabled=true
management.endpoint.health.show-details=always
```

### Sample Metrics Exposed
Access via: [http://localhost:8080/actuator/prometheus](http://localhost:8080/actuator/prometheus)
Health Check endpoint: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)

Some sample metrics:
- `http_server_requests_seconds_count`
- `jvm_memory_used_bytes`
- `process_cpu_usage`
- `logback_events_total`

---

## Assumptions on Telemetry Collectors

- Prometheus is assumed to be scraping the `/actuator/prometheus` endpoint for metrics.
- Grafana is assumed to be used to visualize these Prometheus metrics.
- Spring Boot's built-in logging is used, and it's assumed logs are being collected using ELK (Elasticsearch, Logstash, Kibana).

Sample dashboard panels in Grafana can include:
- Request latency over time
- Error rate per endpoint
- JVM memory usage

---