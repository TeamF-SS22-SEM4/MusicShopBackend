eval $(minikube docker-env)

kubectl delete -f ./kubernetes/activemq-deployment.yml
kubectl delete -f ./kubernetes/musicshop-backend-deployment.yml
kubectl delete -f ./kubernetes/postgres-deployment.yml

./gradlew war
docker build -f Dockerfile -t team-f-musicshop-backend .

kubectl apply -f ./kubernetes/activemq-deployment.yml
kubectl apply -f ./kubernetes/musicshop-backend-deployment.yml
kubectl apply -f ./kubernetes/postgres-deployment.yml