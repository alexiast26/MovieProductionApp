# 🎬 Movie Production App

A microservices-based web application built for our university project to manage movie production workflows, staff, users, notifications, and reports.

## 🚀 Tech Stack

- **Backend:** Spring Boot (Java microservices: API Gateway, User Service, Movie Service, Staff Service, Notification Service, Report Service)
- **Frontend:** React + Vite (with React Router, i18next, and Recharts)
- **Database:** MySQL 8.0
- **Containerization:** Docker & Docker Compose

## 🛠️ Project Structure

- `api_gateway/` - Main entry point routing requests to backend services
- `movie_service/` - Manages movies, production details, and media
- `staff_service/` - Manages cast, crew, and staff assignments
- `users_service/` - Handles authentication, user accounts, and roles
- `notification_service/` - Manages alerts and notifications
- `report_service/` - Generates statistics and data exports (CSV, JSON, XML, DOC)
- `Frontend/frontend-filme/` - The React user interface

## 🏃‍♂️ How to Run

1. Make sure you have Docker and Docker Compose installed.
2. Run the entire system with Docker Compose:
   ```bash
   docker-compose up --build
   ```
3. Access the frontend app and API gateway as configured in the setup!
