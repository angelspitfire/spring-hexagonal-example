# Project Overview

## Introduction
This document provides a comprehensive overview of our Spring Boot-based application, focusing on brand, campaign, and creative management. The solution leverages a microservices architecture, ensuring scalability and flexibility.

## Architecture Overview
The application is structured around several key components:

- **Spring Boot Application**: Serves as the backbone, facilitating RESTful APIs for brand, campaign, and creative management.
- **MongoDB**: Used for persisting data across different entities.
- **Layered Architecture**: Includes Controller, Service, and Repository layers, each responsible for specific aspects of the application logic.

Refer to the component diagrams in `docs/ArchitectureOverview.puml` and `docs/Other.puml` for more details.

## Hexagonal Architecture

This project adopts the Hexagonal Architecture pattern to promote loose coupling and separation of concerns. This architectural style allows us to isolate the core logic of our application from external influences and technologies, making the system more adaptable and easier to test.

### Inside the Hexagon: Domain and Application Layers

- **Domain Layer**: Contains the business logic and domain models (e.g., `Brand`, `Campaign`, `Creative`). This layer is at the heart of the application, encapsulating the rules and processes specific to the business.
- **Application Layer**: Acts as a bridge between the domain layer and the outside world. It contains application services (e.g., `ManageBrandUseCase`, `ManageCampaignUseCase`, `ManageCreativeUseCase`) that orchestrate the execution of business logic.

### Outside the Hexagon: Ports and Adapters

- **Ports**: Defined by interfaces, ports represent the points of interaction between the application and the outside world. For example, `BrandRepository`, `CampaignRepository`, and `CreativeRepository` can be seen as outgoing ports to the database, while the REST controllers serve as incoming ports for HTTP requests.
- **Adapters**: Implement the ports to work with specific technologies. For instance, the Spring Data MongoDB repositories (`BrandRepositoryImpl`, `CampaignRepositoryImpl`, `CreativeRepositoryImpl`) are adapters for the database, and the Spring MVC controllers (`BrandController`, `CampaignController`, `CreativeController`) are adapters for the web API.

By adhering to the Hexagonal Architecture, we ensure that changes in external technologies or platforms have minimal impact on the core business logic, thereby enhancing the maintainability and scalability of our application.

## Class Diagrams
Class diagrams for `Brand`, `Campaign`, and `Creative` entities are detailed in `docs/Brand.puml`, illustrating the relationships and fields for each entity.

## Use Case Diagram
The use case diagram in `docs/UseCase.puml` outlines the system's functionality from a client's perspective, including creating, listing, updating, and deleting brands, campaigns, and creatives.

## Development Environment
- **Programming Language**: Java
- **Framework**: Spring Boot
- **Build Tool**: Maven

## Getting Started
To set up the project locally:
1. Clone the repository.
2. Install dependencies using Maven.
3. Run the application through your IDE or command line.

## Conclusion
This project aims to provide a robust solution for managing brands, campaigns, and creatives. Future improvements may include enhanced analytics, deeper integration with social media platforms, and improved UI/UX for the administrative dashboard.