####
# This Dockerfile is used to build a Quarkus application using a multi-stage build.
# It supports both JVM and native builds.
####

####
# Stage 1: Build the application
####
FROM docker.io/library/maven:3.9.5-eclipse-temurin-17 AS build

WORKDIR /app

# Copy the Maven pom.xml and download dependencies (for better layer caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy the source code
COPY src ./src

# Build the application
# For JVM mode (default)
RUN mvn package -DskipTests

# Uncomment the line below and comment the line above for native build
# RUN mvn package -Pnative -DskipTests

####
# Stage 2: Create the runtime image (JVM mode)
####
FROM registry.access.redhat.com/ubi8/openjdk-17-runtime:1.18

ENV LANGUAGE='en_US:en'

# We make four distinct layers so if there are application changes the library layers can be re-used
COPY --from=build --chown=185 /app/target/quarkus-app/lib/ /deployments/lib/
COPY --from=build --chown=185 /app/target/quarkus-app/*.jar /deployments/
COPY --from=build --chown=185 /app/target/quarkus-app/app/ /deployments/app/
COPY --from=build --chown=185 /app/target/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"
ENV JAVA_APP_JAR="/deployments/quarkus-run.jar"

ENTRYPOINT [ "java", "-jar", "/deployments/quarkus-run.jar" ]


####
# Uncomment the section below for native builds
####
# FROM registry.access.redhat.com/ubi8/ubi-minimal:8.9 AS runtime
#
# WORKDIR /work/
# RUN chown 1001 /work \
#     && chmod "g+rwX" /work \
#     && chown 1001:root /work
#
# COPY --from=build --chown=1001:root /app/target/*-runner /work/application
#
# EXPOSE 8080
# USER 1001
#
# ENTRYPOINT ["./application", "-Dquarkus.http.host=0.0.0.0"]
