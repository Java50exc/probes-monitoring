FROM eclipse-temurin:17-jdk-alpine
ARG VERSION
COPY ./target/config-server-${VERSION}.jar app.jar
ENTRYPOINT [ "java", "-jar", "app.jar" ]
LABEL version=$VERSION