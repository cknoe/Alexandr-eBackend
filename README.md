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

## Users (authenticated)
- GET /users  
  - Description: Return information about the currently authenticated user.  
  - Visibility: authenticated

- GET /users/{id}  
  - Description: Return public details for a user by id (requires authentication in this app).  
  - Visibility: authenticated

- PUT /users  
  - Description: Update current user's profile and return updated user + new JWT.  
  - Example body: {"username":"newusername","email":"new@example.com","password":"newpass"}  
  - Response: 200 OK (returns user DTO and refreshed JWT)  
  - Visibility: authenticated

- DELETE /users  
  - Description: Delete the currently authenticated user's account.  
  - Response: 204 No Content  
  - Visibility: authenticated

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

## Collections (authenticated)
- GET /collections  
  - Description: Return the list of collections for the authenticated user.  
  - Visibility: authenticated (JWT required)

- GET /collections/{id}  
  - Description: Return a single collection by id for the authenticated user.  
  - Visibility: authenticated

- POST /collections  
  - Description: Create a new collection for the authenticated user.  
  - Example body: {"name":"My Collection","description":"Optional description"}  
  - Response: 201 Created  
  - Visibility: authenticated

- PUT /collections/{id}  
  - Description: Update an existing collection (by id) for the authenticated user.  
  - Example body: {"name":"Updated name","description":"Updated description"}  
  - Visibility: authenticated

- DELETE /collections/{id}  
  - Description: Delete a collection (by id) belonging to the authenticated user.  
  - Response: 204 No Content  
  - Visibility: authenticated

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