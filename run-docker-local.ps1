docker-compose -f docker-compose.local.yml down
./gradlew war
docker-compose -f docker-compose.local.yml build
docker-compose -f docker-compose.local.yml up -d