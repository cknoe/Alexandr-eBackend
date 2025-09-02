docker-compose up -d
./mvnw spring-boot:run

test
./mvnw spring-boot:run -Dspring-boot.run.profiles=test