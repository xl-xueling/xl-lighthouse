FROM eclipse-temurin:11-jre

WORKDIR /app

ENV LDP_HOME=/app

COPY target/lighthouse-insights-2.3.6.jar app.jar
COPY target/lib ./lib

RUN mkdir -p /app/conf
COPY config/ ./conf/

RUN echo "=== 构建时文件验证 ===" && \
    ls -la /app/ && \
    echo "配置文件:" && \
    ls -la /app/conf/ && \
    echo "依赖数量:" $(find /app/lib -name "*.jar" | wc -l)

CMD ["java", "-Dloader.path=/app/lib", "-Dspring.config.location=file:/app/conf/lighthouse-insights.yml", "-jar", "app.jar"]
