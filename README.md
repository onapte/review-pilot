# Review Pilot
**Review Pilot** is a code review system powered by open-source LLMs. It integrates directly with GitHub Pull Requests to automate intelligent code review, leveraging a microservice architecture with Redis, gRPC, and Dockerized services.

## High-level Planned Architecture
![alt text](image.png)

## Goals
- ✅ Automatically trigger analysis when a GitHub PR is opened
- ✅ Use Redis as a lightweight message broker for task queuing
- ✅ Extract PR metadata and file diffs using GitHub API
- 🔜 Run the diff through an open-source LLM to generate review comments
- 🔜 Push comments back to GitHub via API
- 🔜 Enable a UI dashboard for developers to track PR analysis
- 🔜 Train and fine-tune LLM with feedback data
- 🔜 Provide full observability into code analysis, LLM confidence, and review cycles

## Data flow
- **Frontend** (React + TypeScript): Web interface to view review progress, reports, PR feedback
- **Backend (main microservice)**: Receives GitHub Webhooks → sends payloads to Redis
- **Extractor**: Pulls payloads from Redis → calls GitHub API → structures diff → sends to LLM
- **LLM Microservice**: Analyzes code via local LLM → generates feedback
- **Redis**: Acts as message queue between Ingestor and Extractor
- **GitHub API**: Used to fetch PR metadata, files, and post review comments
- **Database (Planned)**: For tracking PR history, feedback records, and auth sessions
- **Security layer (Planned)**: To secure inter-service communication and OAuth login

## Microservices
### Backend service
- Location: `backend/backend`
- Port: `8080`
- Endpoint: `POST /webhook`
- Responsibilities:
  - Receives GitHub webhook events
  - Queues payload to Redis

### Extractor service
- Location: `backend/extractor`
- Port: `8081`
- Endpoint: `GET /next-payload`
- Responsibilities:
  - Dequeues and parses GitHub payload
  - Fetches file diff and metadata via GitHub API
  - Sends structured data to LLM microservice via gRPC

### LLM service *(Not implemented yet)*
- gRPC server that:
  - Accepts parsed PR data
  - Runs LLM models for static analysis
  - Returns suggested review comments

### Frontend *(Not implemented yet)*
- React + TypeScript app
- Displays:
  - Status of reviews
  - GitHub-linked PR details
  - Review suggestions
  - Confidence/quality scores

## Running locally
### Prerequisites
- Java 17+
- Maven
- Docker
- GitHub personal access token
- Webhook configured on your GitHub repo

### 1. Clone repository
```bash
git clone https://github.com/onapte/review-pilot.git
cd review-pilot
```

### 2. Start Redis
```bash
docker run --name redis-dev -p 6379:6379 -d redis
```

### 3. Run Backend service
```bash
cd backend/backend
./mvnw spring-boot:run
```

### 4. Run Extractor service
```bash
cd ../extractor
./mvnw spring-boot:run
```

## Tech stack
- Java + Spring Boot
- Redis (via Docker)
- GitHub Webhooks + REST APIs
- gRPC
- React + TypeScript
- Open-source LLM (most probably DeepSeekCoder)

## Directory structure
(**LLM service** to be implemented next)
```bash
ReviewPilot/
├── backend/
│   ├── backend/      # -> Backend service
│   └── extractor/    # -> Extractor service
├── frontend/         # -> React dashboard (Planned)
├── docker-compose.yml (planned)
├── README.md
```

## Note
Built by @onapte. PRs, ideas, or contributions are welcome!