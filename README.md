
# Spring Boot Microservices Complete Guide 🚀

A complete end-to-end **microservices architecture** built with **Spring Boot** and **Spring Cloud**.
Includes **MySQL** and **MongoDB** for data persistence, along with service discovery, API Gateway, security, inter-process communication, circuit breaking, distributed tracing, event-driven architecture, containerization, and monitoring.

---

## 📑 Table of Contents

* [Introduction](#introduction)
* [High-Level Architecture](#high-level-architecture)
* [Logical Architecture](#logical-architecture)
* [Build Services](#build-services)
* [Inter-Process Communication](#inter-process-communication)
* [Service Discovery (Eureka)](#service-discovery-eureka)
* [API Gateway (Spring Cloud Gateway)](#api-gateway-spring-cloud-gateway)
* [Secure Microservices (Keycloak)](#secure-microservices-keycloak)
* [Circuit Breaker](#circuit-breaker)
* [Distributed Tracing](#distributed-tracing)
* [Event-Driven Architecture (Kafka)](#event-driven-architecture-kafka)
* [Database Configuration (MySQL & MongoDB)](#database-configuration-mysql--mongodb)
* [Dockerization](#dockerization)
* [Monitoring (Prometheus & Grafana)](#monitoring-prometheus--grafana)
* [How to Run](#how-to-run)
* [Contributing](#contributing)

---

## 🎬 Introduction

This repository is a **blueprint for building cloud-native microservices** using the Spring ecosystem.
It demonstrates real-world practices and includes **MySQL** and **MongoDB** as storage solutions.

---

## 🏗 High-Level Architecture

The system consists of multiple independent services communicating via REST and Kafka events.
All services are secured with Keycloak, routed through an API Gateway, and registered in Eureka.

---

## 📐 Logical Architecture

* **Product Service** → MongoDB
* **Order Service** → MySQL
* **Inventory Service** → MySQL
* **Notification Service** → MongoDB
* **Discovery Server (Eureka)**
* **API Gateway**
* **Keycloak Server**
* **Kafka Broker**
* **Prometheus + Grafana**

---

## 🔨 Build Services

Each service is built using:

* Spring Boot
* Spring Data JPA (for MySQL) / Spring Data MongoDB
* Lombok
* Maven

---

## 🔗 Inter-Process Communication

* **REST API** (Synchronous)
* **Kafka Events** (Asynchronous)

---

## 🛰 Service Discovery (Eureka)

* Services register with **Netflix Eureka Server**.
* API Gateway uses Eureka to route traffic dynamically.

---

## 🚪 API Gateway (Spring Cloud Gateway)

* Single entry point for all clients.
* Handles routing, load balancing, and authentication.

---

## 🔐 Secure Microservices (Keycloak)

* OAuth2 & OpenID Connect with **Keycloak**.
* API Gateway enforces authentication & authorization.

---

## ⚡ Circuit Breaker

* Implemented using **Resilience4j**.
* Prevents cascading failures between services.

---

## 🔍 Distributed Tracing

* Integrated with **Zipkin** or **Jaeger**.
* Trace requests across multiple services.

---

## 📡 Event-Driven Architecture (Kafka)

* Kafka used for **asynchronous communication**.
* Notification service consumes order events.

---

## 🗄 Database Configuration (MySQL & MongoDB)

* **MySQL**: Relational storage for orders, inventory, and transactional data.
* **MongoDB**: Document storage for product catalog and notifications.

Example `application.properties` (do **not commit real passwords**):

```properties
# MySQL
spring.datasource.url=jdbc:mysql://localhost:3306/orderdb
spring.datasource.username=root
spring.datasource.password=<password>
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update

# MongoDB
spring.data.mongodb.uri=mongodb://<username>:<password>@localhost:27017/productdb
```

⚠️ **Never commit real passwords**. Use environment variables or `.gitignore` as discussed.

---

## 🐳 Dockerization

* Each service is containerized with Docker.
* Docker Compose manages multi-service deployment, including MySQL and MongoDB containers.

---

## 📊 Monitoring (Prometheus & Grafana)

* Metrics exposed by Spring Boot Actuator.
* Prometheus scrapes metrics.
* Grafana provides dashboards.

---

## ▶ How to Run

1. Clone the repo:

   ```bash
   git clone https://github.com/vipunsanjana/Spring-Microservices-Complete-Blueprint.git
   cd Spring-Microservices-Complete-Blueprint
   ```
2. Start Docker services:

   ```bash
   docker-compose up -d
   ```

    * MySQL & MongoDB will run in containers.
3. Access services:

    * Eureka Dashboard → `http://localhost:8761`
    * API Gateway → `http://localhost:8080`
    * Keycloak → `http://localhost:8081`
    * Grafana → `http://localhost:3000`

---

## 🤝 Contributing

Contributions, issues, and feature requests are welcome!
Feel free to fork this repo and submit a PR.

---

## 👤 Author

Vipun Sanjana – Software Engineer | Full Stack, AI & DevOps (Former SE Intern @WSO2)

Email: vipunsanjana@gmail.com

GitHub: https://github.com/vipunsanjana

LinkedIn: https://www.linkedin.com/in/vipunsanjana

---