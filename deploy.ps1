# building the transaction service
cd ./../../transactionServiceApi/transactionServiceApi
gradle build -x test

#building the webhook service
cd ./../../webhook-coolector-service/webhook-coolector-service
gradle build -x test

# starting the containers
docker compose build
docker compose up