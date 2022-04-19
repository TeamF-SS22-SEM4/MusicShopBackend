sudo docker-compose down
./gradlew build
sudo docker-compose -f docker-compose.yml -f docker-compose.local.yml up -d
