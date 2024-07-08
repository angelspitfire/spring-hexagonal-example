# Project Architecture and Execution Guide

## Overview of the Solution

This project aims to provide a comprehensive system for managing brands, campaigns, and creatives within a digital advertising platform. Leveraging the power of Spring Boot and MongoDB, it offers a robust backend infrastructure capable of handling a variety of advertising operations efficiently. Designed with scalability and maintenance in mind, this solution is well-suited for both small startups and large enterprises looking to optimize their advertising workflows.

![Components diagram](img/Components-0.png "Components diagram")

## Architecture Overview

The system's architecture is designed following the Hexagonal Architecture approach, also known as Ports and Adapters. This design pattern aims to create a loosely coupled application that isolates the core logic from external concerns. By structuring the application into several key layers, each responsible for a distinct aspect of the application's functionality, we ensure flexibility and ease of maintenance:

- **Controller Layer (Adapters)**: Acts as the primary entry point for HTTP requests, exposing RESTful APIs for interacting with the application. This layer adapts requests from the external world into a format that the application can use.
- **Service Layer (Application Core)**: Implements the business logic and use case execution. It serves as the application's core, where the main functionalities and rules are processed.
- **Repository Layer (Adapters)**: Manages CRUD operations with the MongoDB database, acting as an adapter that allows the application core to interact with the database without being coupled to it.
- **Model Layer**: Defines the structure of the data entities used across the application, central to the application's domain.
- **Domain Layer**: Represents the core business logic and entities. It is the heart of the application, where the business rules and domain logic reside.
- **Infrastructure Layer (Adapters)**: Configures the technical infrastructure, including database connections and other system-wide settings. This layer includes adapters for various external interfaces the application interacts with.

By adhering to the Hexagonal Architecture, we ensure that the application's core logic is independent of external interfaces and frameworks, making the system more resilient to changes in technology or business requirements.

**Key Components**
![Components diagram](img/Components-0.png "Components diagram")

**Classes**
![Architecture Overview](img/ArchitectureOverview-0.png "Architecture Overview")

## Technical Stack

- **Programming Language**: Java 17
- **Framework**: Spring Boot
- **Database**: MongoDB (embedded and real instance on docker)
- **Build Tool**: Maven

## Running the Application

To get the application up and running on your local machine, follow these steps:

1. **Prerequisites**:
   - Java 17 or newer installed.
   - MongoDB running on the default port (27017).
   - Maven installed for building the application.
   - Docker installed (optional, for running MongoDB in a container).
   - Docker Compose installed (optional, for running MongoDB in a container).

2. **Clone the Repository**:
   Clone the project repository to your local machine using the following command:
   ```shell
   git clone git@github.com:angelspitfire/smart-assets-be-challenge.git
   cd smart-assets-be-challenge
    ```
3. **Build the Application**:
```shell
   mvn clean package
   ```
4. **Run the Application**:
```shell
   java -jar target/smart-assets-be-challenge-0.0.1-SNAPSHOT.jar
   ```
or alternatively, you can run the application using the following command:
```shell
   mvn spring-boot:run
   ```
or using docker-compose:
```shell
   docker-compose up
   ```
5. **Access the Application**:
    The application will be accessible at `http://localhost:8080`. You can use tools like Postman or cURL to interact with the RESTful APIs provided by the application.
    The Swagger UI is also available at `http://localhost:8080/swagger-ui.html`, providing a user-friendly interface for exploring and testing the APIs.

6. **Consideratiions**:
    - The application uses an embedded MongoDB for local use instance by default. If you prefer to use a standalone MongoDB instance, you can configure the connection details in the `application.yaml` file.
    - The application runs a MongoDB instace whe running with docker-compose. You can run the following command to start the application with docker-compose. The reason behind this is to make it easier to run the application without the need to install MongoDB on your local machine due to a bug on apple silicon machines from de.flapdoodle,de.flapdoodle.embed.mongo.
    - The application uses an in-memory database, which means that data will not persist between application restarts. For a production environment, you should configure a real MongoDB instance and provide the necessary connection details in the application configuration.
    - The application is designed to be stateless, meaning that it does not store session data between requests. This design choice ensures scalability and fault tolerance, as each request is processed independently.