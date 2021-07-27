./mvnw package -Pnative
cp target/*-runner ./
./build-push-to-dockerhub.sh