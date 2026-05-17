# run
docker-compose up -d
./mvnw spring-boot:run

# test
./mvnw spring-boot:run -Dspring-boot.run.profiles=test

# Connection
curl -v -X POST http://localhost:8080/login -H "Content-Type: application/json" -d '{"username":"testuser","password":"password"}' | jq
curl -v http://localhost:8080/cards -H "Authorization: Bearer TOKEN"| jq

# API Endpoints

## Authentication (public)
- POST /login  
  - Description: Authenticate user and return JWT  
  - Example body: {"username":"testuser","password":"password"}  
  - Visibility: public

- POST /register  
  - Description: Create a new user  
  - Example body: {"username":"newuser","password":"password","email":"a@b.c"}  
  - Visibility: public

- POST /refresh-token  
  - Description: Refresh JWT token  
  - Visibility: public

- POST /logout  
  - Description: Logout / invalidate token (if implemented)  
  - Visibility: public

## Cards (authenticated)
- GET /cards  
  - Description: List cards  
  - Visibility: authenticated (JWT required)

- GET /cards/{id}  
  - Description: Get a card by id  
  - Visibility: authenticated

- POST /cards  
  - Description: Create a card  
  - Example body: {"title":"Title","description":"..."}  
  - Visibility: authenticated  
  - Roles: check CardController for role restrictions

- PUT /cards/{id}  
  - Description: Update a card  
  - Example body: {"title":"New title","description":"..."}  
  - Visibility: authenticated

- DELETE /cards/{id}  
  - Description: Delete a card  
  - Visibility: authenticated  
  - Roles: check CardController for role restrictions

## Third-party / utility
- GET /api/logodev  
  - Description: Third-party utility endpoint (uses LOGO_DEV_KEY)  
  - Visibility: public

## Technical notes
- Authentication: send JWT in header Authorization: Bearer <token>.  
- CORS: allowed origin configured via Spring property `app.frontend-url` (e.g. http://localhost:3000).  
- To auto-list mappings locally:
  - From repo:  
    grep -R --line-number -E "@(GetMapping|PostMapping|PutMapping|DeleteMapping|PatchMapping|RequestMapping)" src/main/java | sed 's/src\/main\/java\///'
  - Or via Actuator after starting the app:  
    ./mvnw spring-boot:run  
    curl -sS http://localhost:8080/actuator/mappings | jq