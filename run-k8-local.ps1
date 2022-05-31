kubectl delete -f ./kubernetes/activemq-deployment.yml
kubectl delete -f ./kubernetes/musicshop-backend-deployment.yml
kubectl delete -f ./kubernetes/postgres-deployment.yml
kubectl delete -f ./kubernetes/configmap.yml
kubectl delete -f ./kubernetes/secret.yml


./gradlew war
docker build -f Dockerfile -t team-f-musicshop-backend .

kubectl apply -f ./kubernetes/secret.yml
kubectl apply -f ./kubernetes/configmap.yml
kubectl apply -f ./kubernetes/activemq-deployment.yml
kubectl apply -f ./kubernetes/musicshop-backend-deployment.yml
kubectl apply -f ./kubernetes/postgres-deployment.yml