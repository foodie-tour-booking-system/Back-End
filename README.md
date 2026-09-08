# Foodie Tour Booking System — Back-End

REST API for a food tour booking platform. Customers browse tours and routes, book seats on scheduled departures, pay online, and leave feedback; staff manage tours, dishes, schedules, and reports. Includes an AI assistant that answers customer questions from the platform's own content.

**My role:** Backend developer — API design, database schema, authentication, and payment integration.

---

## Features

- **Tours & routes** — tour catalog, route details, dishes, and image galleries
- **Scheduling & booking** — scheduled departures with seat booking and booking status tracking
- **Payments** — VNPay and OnePay integration with callback handling, plus transaction records
- **Authentication & authorization** — self-issued JWT validated as an OAuth2 resource server, with role- and permission-based access control
- **AI assistant** — retrieval-augmented chatbot built on Spring AI, embedding PDF documents into a pgvector store so answers stay grounded in real tour content
- **Media storage** — image upload and delivery through AWS S3
- **Feedback & reporting** — customer feedback and operational reports for staff
- **Email** — transactional mail for booking confirmations and notifications

---

## Tech Stack

| Layer | Technology |
| :--- | :--- |
| Language | Java 21 |
| Framework | Spring Boot 3, Spring MVC, Spring Data JPA, Spring Security (OAuth2 Resource Server) |
| Database | PostgreSQL 16 (with pgvector) |
| AI | Spring AI, OpenAI models, PDF document reader, pgvector store |
| Cloud | AWS S3 |
| Payments | VNPay, OnePay |
| Other | MapStruct, Lombok, springdoc-openapi (Swagger), Spring Mail |
| DevOps | Docker, Docker Compose, GitHub Actions |

---

## Architecture

Modular monolith — the codebase is split by business domain, each module self-contained with its own controller, service, repository, entity, DTO, and mapper:

```
org.foodie_tour/
├── config/          # security, AI, S3, OpenAPI
└── modules/
    ├── auth/            # authentication, roles, permissions
    ├── customer/        # customer profiles
    ├── employee/        # staff accounts
    ├── tours/           # tour catalog and dishes
    ├── routes/          # route details and images
    ├── schedules/       # departures and scheduling
    ├── booking/         # bookings
    ├── tracking/        # booking/tour status tracking
    ├── transaction/     # payment records
    ├── vnpay/ onepay/   # payment gateway integrations
    ├── feedback/        # customer feedback
    ├── report/          # operational reports
    ├── images/ aws/     # image metadata and S3 storage
    ├── mail/            # transactional email
    ├── chatbot/         # AI assistant (Spring AI + pgvector)
    └── system/          # admin configuration
```

Entities never leave the service layer — MapStruct maps them to DTOs, and JPA Specifications handle dynamic filtering.

---

## Getting Started

### Prerequisites
- Java 21+
- Docker & Docker Compose

### Run locally

```bash
git clone https://github.com/foodie-tour-booking-system/Back-End.git
cd Back-End

# copy the environment template and fill in your values
cp .env.example .env

# starts PostgreSQL and the application
docker compose up -d
```

The API will be available at `http://localhost:8080`.

### Environment variables

| Variable | Description |
| :--- | :--- |
| `DB_NAME`, `DB_USER`, `DB_PASSWORD`, `DB_PORT` | PostgreSQL connection |
| `PORT` | Port the application is exposed on |
| `JWT_*` | Signing key and token lifetimes |
| `OPENAI_API_KEY` | Model access for the AI assistant |
| `AWS_*` | S3 bucket credentials and region |
| `VNPAY_*`, `ONEPAY_*` | Payment gateway credentials |
| `MAIL_*` | SMTP settings for transactional email |

---

## API documentation

Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## Deployment

CI/CD runs on GitHub Actions (`.github/workflows/ci-cd.yml`): the workflow builds the JAR, packages it into a Docker image, and redeploys the stack.
