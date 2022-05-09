# SSL certificate information service
 This application fecthes certificates and save in database.
 
# Tools
We have used Spring boot, JAVA 11, Mapstruct for the mapping, postgresSQL database and pgadmin to connect to database.
For the testing part, we have used MockMvc, Mockito and JUnit5.

docker and docker-compose are used to build and deploy applications.

# Build and deploy
Execute the script buildAndDeploy.sh. This script deploy first the database named db and execute a script to initialize the postgresSQL database (Table creation).
After, the services pgadmin and ssl-service are deployed.


# Test ssl-service
Once ssl-service is deployed, we can call the restapi using restapi.sh script. 
We can execute differents scenarios :
1 - ./restapi.sh https://www.google.com
2 - ./restapi.sh https://www.badrequest.com

We can check on database that some certificates informations are saved.