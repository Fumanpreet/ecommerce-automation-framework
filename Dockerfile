FROM mcr.microsoft.com/playwright/java:v1.55.0-jammy
# Execute all of the following in app folder (create and cd app)
WORKDIR /app
# Copy all folders and file but ignore the ones in .dockerignore
COPY . .
# As you download all project dependencies and plugins
RUN mvn dependency:go-offline

CMD ["mvn", "clean", "test"]