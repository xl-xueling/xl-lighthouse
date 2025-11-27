FROM eclipse-temurin:11-jre

WORKDIR /app

ENV LDP_HOME=/app

COPY target/lighthouse-insights-2.3.6.jar app.jar
COPY target/lib ./lib

RUN mkdir -p /app/conf
COPY config/ ./conf/

CMD ["java", "-Dloader.path=/app/lib", "-Dspring.config.location=file:/app/conf/lighthouse-insights.yml", "-jar", "app.jar"]
