#!/bin/bash

echo "Deleting Kafka broker volumes and Zookeeper data..."
rm -rf docker-compose/volumes/kafka
rm -rf docker-compose/volumes/zookeeper
echo "Volumes deleted successfully."

echo "Creating Kafka broker volumes and Zookeeper data..."
mkdir -p docker-compose/volumes/kafka/broker-1
mkdir -p docker-compose/volumes/kafka/broker-2
mkdir -p docker-compose/volumes/kafka/broker-3
mkdir -p docker-compose/volumes/zookeeper/data
mkdir -p docker-compose/volumes/zookeeper/transactions
echo "Volumes created successfully."

echo "Done."
