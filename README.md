# 🎭 Spectacol - Theater Reservation System

## Project Description

Spring Boot application for managing theater shows and seat reservations in a performance hall.

## What The System Does

- Managing theater halls and automatic seat generation
- Managing shows associated with a hall
- Client registration
- Creating reservations for one or multiple seats
- Cancelling reservations
- Viewing available seats for a show

---

## ERD Diagram

```mermaid
erDiagram
    SALA ||--o{ LOC : contains
    SALA ||--o{ SPECTACOL : hosts
    SPECTACOL ||--o{ REZERVARE : has
    CLIENT ||--o{ REZERVARE : makes
    REZERVARE ||--o{ REZERVARE_LOC : contains
    LOC ||--o{ REZERVARE_LOC : has

    SALA {
        long id PK
        string name
        int capacity
    }

    LOC {
        long id PK
        long sala_id FK
        int rowNumber
        int seatNumber
    }

    SPECTACOL {
        long id PK
        long sala_id FK
        string title
        string description
        timestamp dateTime
        double price
    }

    CLIENT {
        long id PK
        string firstName
        string lastName
        string email
    }

    REZERVARE {
        long id PK
        long client_id FK
        long spectacol_id FK
        timestamp reservationDate
        string status
        double totalPrice
    }

    REZERVARE_LOC {
        long id PK
        long rezervare_id FK
        long loc_id FK
    }
```

---

## Functional Requirements

1. The system must allow creation and management of theater halls
2. The system must automatically generate seats by rows and seat numbers
3. The system must allow creation and management of shows associated with a hall
4. The system must allow client registration with email validation
5. A client can create a reservation for one or multiple seats
6. The system must verify seat availability before creating a reservation
7. A seat cannot be reserved twice for the same show
8. The system must automatically calculate the total price of a reservation
9. The system must allow cancellation of a reservation
10. The system must allow viewing available seats for a show