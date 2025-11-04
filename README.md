# Sportradar Coding Academy 2026 - Backend

This repository contains my solution for the Sportradar Coding Academy Backend take-home assignment.

## Tech Stack

- Java 21
- Spring Boot
- MariaDB (SQL, not H2)
- JPA / Hibernate

## Database Design (ERD)

I designed the database in third normal form.  
The application uses 4 tables:

- sports
- teams
- venues
- matches

`matches` references `teams` and `venues` via foreign keys.

![ERD](ERD.png)
