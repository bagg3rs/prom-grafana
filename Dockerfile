FROM maven:3.6.3-jdk-11-slim AS build
WORKDIR /home
COPY src ./src
COPY pom.xml ./
RUN mvn package

FROM openjdk:11-jre-slim
COPY --from=build /home/target/metricssimulator-0.0.1-SNAPSHOT.jar /usr/local/lib/metricssimulator.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/metricssimulator.jar"]