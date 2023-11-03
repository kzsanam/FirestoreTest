# syntax=docker/dockerfile:1

#FROM eclipse-temurin:17-jdk-jammy
#FROM maven:3.8.2-openjdk-11
#the thing above should be faster than load java, install ubuntu, install maven on top, but did not work for some reason
FROM openjdk:11
RUN apt-get update && apt-get install -y maven
# Set the working directory in the container
WORKDIR /app

# Copy the Maven Wrapper files
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:resolve

#folder copied to container
COPY src ./src

# Copy the Firestore service account key file into the container
COPY serviceAccountKey.json /app/serviceAccountKey.json

# Expose the port that your Java application may listen on (adjust as needed)
#EXPOSE 8085

# Set the GOOGLE_APPLICATION_CREDENTIALS environment variable
#ENV GOOGLE_APPLICATION_CREDENTIALS=/app/serviceAccountKey.json

# Build the application using the Maven Wrapper
RUN ./mvnw package

#run java
#CMD ["./mvnw", "java", "-jar", "target/FirestoreTest-1.0-SNAPSHOT.jar"]
CMD ["java", "-jar", "target/FirestoreTest-1.0-SNAPSHOT.jar"]
