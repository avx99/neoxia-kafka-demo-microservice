@echo off
echo Deleting Kafka broker volumes and Zookeeper data...
rd /s /q docker-compose\volumes\kafka
rd /s /q docker-compose\volumes\zookeeper
echo Volumes deleted successfully.

echo Creating Kafka broker volumes and Zookeeper data...
mkdir docker-compose\volumes\kafka\broker-1
mkdir docker-compose\volumes\kafka\broker-2
mkdir docker-compose\volumes\kafka\broker-3
mkdir docker-compose\volumes\zookeeper\data
mkdir docker-compose\volumes\zookeeper\transactions
echo Volumes created successfully.

echo Done.
rem pause
