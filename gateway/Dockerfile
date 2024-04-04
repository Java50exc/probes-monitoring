FROM eclipse-temurin:17-jdk-alpine
ARG VERSION
COPY ./target/gateway-${VERSION}.jar app.jar
ENV CONFIG_SERVER_HOST=config-server
ENTRYPOINT [ "java", "-jar", "app.jar" ]
LABEL version=$VERSION