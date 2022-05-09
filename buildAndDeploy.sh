#!/bin/bash
set -x


#Stops images
docker-compose -p ssl-deploy down

# remove existing volumes
docker volume rm -f ssl-db ssl-pgadmin

echo "build and deploy"

docker-compose -p ssl-deploy up -d db

sleep 2

# command used only for git bash windows
docker-compose -p ssl-deploy exec -T -u postgres db //bin/bash -c "cd /scripts ; /scripts/DB_creation.sh"

# command for others systems
# docker-compose -p ssl-deploy exec -T -u postgres db /bin/bash -c "cd /scripts ; /scripts/DB_creation.sh"


docker-compose -p ssl-deploy up -d pgadmin

docker-compose -p ssl-deploy up -d ssl-service

