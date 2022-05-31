./gradlew war
docker build -f Dockerfile -t team-f-musicshop-backend .
kubectl rollout restart deployment/musicshop-backend