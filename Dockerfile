# ---------- Builder Stage ----------
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app
COPY . .
# Build jar but skip tests for speed (remove `-x test` if you want to run tests inside the build)
RUN ./gradlew clean build -x test

# ---------- Run Stage ----------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]