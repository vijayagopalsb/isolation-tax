FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /app

COPY . .

# Run ArchUnit tests (this will fail if isolation is violated)
RUN mvn -pl archunit-tests -am test

# Build the executable jar for service-a
RUN mvn clean package -pl service-a -am -DskipTests -Dspring-boot.repackage.skip=false

FROM eclipse-temurin:21-jre
WORKDIR /app

COPY --from=build /app/service-a/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]


## Build stage
#FROM maven:3.9.9-eclipse-temurin-21 AS build
#WORKDIR /app
#
## Copy the entire project (required for multi-module build)
#COPY . .
#
## Build the executable jar for service-a and its dependencies
#RUN mvn clean package -pl service-a -am -DskipTests -Dspring-boot.repackage.skip=false
#
## Runtime stage
#FROM eclipse-temurin:21-jre
#WORKDIR /app
#
## Copy the built jar
#COPY --from=build /app/service-a/target/*.jar app.jar
#
#EXPOSE 8081
#ENTRYPOINT ["java", "-jar", "app.jar"]