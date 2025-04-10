# Uber Clone Application

## Overview
This Uber Clone Application is a Spring Boot-based backend system that replicates essential ride-hailing features:
- **User Roles:** Rider, Driver and Admin.
- **Admin Functionalities:** Onboard new drivers.
- **Rider Functionalities:** Request rides, Cancel ride, make payments, and rate drivers.
- **Driver Functionalities:** Accept ride requests, start and end rides, receive payments, and rate riders.
- **API Documentation:** Available at the default Swagger-UI URL.
- Admins need to be edited directly in the database once a user has been signed up.
- Built with a focus on scalability, security, and maintainability.

---

## Technologies Used
- **Backend Framework:** Spring Boot (version 3.3.1)
- **Programming Language:** Java 21
- **Build Tool:** Maven
- **Database:** PostgreSQL with PostGIS for locational data
- **Distance Calculation:** OSRM API and PostGIS for geospatial and routing data
- **Security:** JWT for authentication and authorization (Access Token: 10 minutes, Refresh Token: 30 days)
- **SMS Service:** Twilio for OTP-based messaging
- **Email Service:** Gmail SMTP for email notifications
- **Testing Tools:** JUnit, Mockito, and Testcontainers for PostgreSQL with PostGIS
- **Deployment:** PostgreSQL database deployed on AWS

---

## Prerequisites
- **Java:** Ensure Java 21 is installed.
- **Maven:** Install Maven for building the project.
- **PostgreSQL Database:** Ensure a PostgreSQL instance (with PostGIS) is available.
- **Environment Variables(Prod):**
  - `DB_HOST_URL`: Host url for db host
  - `DB_NAME`
  - `DB_PASSWORD`
  - `DB_USERNAME`
  - `JWT_SECRET_KEY`
  - `SMTP_HOST` : Example -> smtp.gmail.com
  - `SMTP_PASSWORD`
  - `SMTP_USERNAME`
  - `TWILIO_ACCOUNT_SID`
  - `TWILIO_AUTH_TOKEN`
  - `TWILIO_PHONE_NO`
  - `SPRING_PROFILES_ACTIVE`: prod
- **Environment Variables(Dev):**
  - `DB_PASSWORD`
  - `DB_USERNAME`
  - `SMTP_HOST` : Example -> smtp.gmail.com
  - `SMTP_PASSWORD`
  - `SMTP_USERNAME`
  - `TWILIO_ACCOUNT_SID`
  - `TWILIO_AUTH_TOKEN`
  - `TWILIO_PHONE_NO`
  - `SPRING_PROFILES_ACTIVE` : dev
    
---
