# Medical — Patient Management Microservices

A production-style patient management system built with Java, Spring Boot, and microservices.

## Course references
- YouTube: [Build & Deploy a Production-Ready Patient Management System](https://www.youtube.com/watch?v=tseqdcFfTUY)
- Reference code: [chrisblakely01/java-spring-microservices](https://github.com/chrisblakely01/java-spring-microservices)

## Current status
- `patient-service` — Spring Boot skeleton with H2, Patient entity, and seed data

## How to run (patient-service)
```powershell
cd patient-service
.\mvnw.cmd spring-boot:run
```

- App: http://localhost:4000
- H2 console: http://localhost:4000/h2-console  
  JDBC URL: `jdbc:h2:mem:testdb` · User: `admin_viewer` · Password: `password`

## Branch strategy
Topic work happens on feature branches (for example `feature/patient-crud`) and is merged into `main` when the topic is complete.
