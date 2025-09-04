docker-compose up -d
./mvnw spring-boot:run

#test
./mvnw spring-boot:run -Dspring-boot.run.profiles=test

#Connection
curl -v -X POST http://localhost:8080/login -H "Content-Type: application/json" -d '{"username":"testuser","password":"password"}' | jq
curl -v http://localhost:8080/cards -H "Authorization: Bearer TOKEN"| jq


quand je mettrai de nouveaux role, il faudra changer cardcontroller