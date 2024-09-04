#Build stage
FROM maven:3.8-amazoncorretto-21 AS build
LABEL maintainer="https://github.com/Mruruc" authors="mr.uruc"
WORKDIR /build
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn clean package -DskipTests

#Runtime stage
FROM amazoncorretto:21 AS prod
ARG APP_VERSION=1.0.0
ENV JAR_NAME=to-do-api-${APP_VERSION}.jar
WORKDIR /app
COPY --from=build /build/target/${JAR_NAME} /app
EXPOSE 8080
CMD  java -jar ${JAR_NAME}



