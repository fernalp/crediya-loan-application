FROM eclipse-temurin:21-jdk-alpine

# Create non-root user and working directory
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
WORKDIR /app
VOLUME /tmp

# Copy built jar from Gradle output
COPY *.jar crediyaSolicitudes.jar

ENV JAVA_OPTS=" -Xshareclasses:name=cacheapp,cacheDir=/cache,nonfatal -XX:+UseContainerSupport -XX:MaxRAMPercentage=70 -Djava.security.egd=file:/dev/./urandom"

# Drop privileges
USER appuser

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -jar crediyaSolicitudes.jar" ]
