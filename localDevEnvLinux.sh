docker-compose down
./gradlew war
docker-compose build
docker-compose -f docker-compose.yml -f docker-compose.local.yml up -d
