# Job Board API
Production-ready REST API for a job board platform built with Spring Boot 4, featuring JWT authentication, role-based access control, and PostgreSQL persistence. Deployed on Render via Docker.
## Live API
```
https://job-board-backend-pfwb.onrender.com/api
```
## Tech Stack
- **Backend:** Spring Boot 4.0.5, Spring Security
- **Authentication:** JWT (Access Token + Refresh Token)
- **Database:** PostgreSQL
- **ORM:** Spring Data JPA
- **Deployment:** Render (Docker)
## Features
- JWT based authentication with access and refresh tokens
- Role based access control (Seeker / Recruiter)
- Job listings with filters and pagination
- Job applications with status tracking
- Save and unsave jobs
- Profile management
- Health check endpoint
## API Endpoints
### Auth
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/auth/signup` | Public | Register as seeker or recruiter |
| POST | `/api/auth/login` | Public | Login and get tokens |
| POST | `/api/auth/refresh` | Public | Refresh access token |
### Jobs
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/api/job/filter` | Public | Get all jobs with filters |
| GET | `/api/job/{jobId}` | Public | Get job by ID |
| POST | `/api/job` | Recruiter | Create a job listing |
| PUT | `/api/job/{jobId}` | Recruiter | Update a job listing |
| DELETE | `/api/job/{jobId}` | Recruiter | Delete a job listing |
| GET | `/api/job/my` | Recruiter | Get recruiter's own listings |
### Applications
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/job/{jobId}/apply` | Seeker | Apply to a job |
| GET | `/api/applications/my` | Seeker | Get my applications |
| DELETE | `/api/applications/{applicationId}` | Seeker | Withdraw application |
| GET | `/api/applications/{applicationId}` | Authenticated | Get application details |
| PATCH | `/api/applications/{applicationId}/status` | Recruiter | Update application status |
| GET | `/api/job/{jobId}/applications` | Recruiter | Get all applicants for a job |
### Saved Jobs
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| POST | `/api/job/{jobId}/save` | Seeker | Save a job |
| DELETE | `/api/job/{jobId}/save` | Seeker | Unsave a job |
| GET | `/api/job/saved` | Seeker | Get all saved jobs |
### Profile
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/api/profile` | Authenticated | Get my profile |
| PUT | `/api/profile` | Authenticated | Update my profile |
### Health
| Method | Endpoint | Access | Description |
|--------|----------|--------|-------------|
| GET | `/api/health` | Public | Check API health |
## Local Setup
### Prerequisites
- Java 21
- Maven
- PostgreSQL
### Steps
1. Clone the repo
```bash
git clone https://github.com/ayushagrawal2806/job-board-backend
cd job-board-backend
```
2. Create PostgreSQL database
```sql
CREATE USER jobboard_user WITH PASSWORD 'your_password';
CREATE DATABASE job_board OWNER jobboard_user;
```
3. Update `application-dev.properties` with your local DB credentials
4. Run the project with dev profile
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```
App runs on `http://localhost:8080`
## Docker
```bash
./mvnw clean package -DskipTests
docker build -t job-board-backend .
docker run -p 8080:8080 job-board-backend
```
## Environment Variables
| Variable | Description |
|----------|-------------|
| `DATABASE_URL` | PostgreSQL connection URL |
| `USERNAME` | Database username |
| `PASSWORD` | Database password |
| `JWT_SECRET` | Secret key for JWT signing |
| `PORT` | Server port (default 8080) |
| `SPRING_PROFILES_ACTIVE` | `dev` or `prod` |
