FROM openjdk:11-jre-slim
#COPY --from=build /home/app/target/ssl-0.0.1-SNAPSHOT.jar /usr/local/lib/ssl-service.jar
COPY target/ssl-0.0.1-SNAPSHOT.jar /usr/local/lib/ssl-service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/ssl-service.jar"]
