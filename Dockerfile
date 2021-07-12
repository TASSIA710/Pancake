FROM openjdk:14


# Build Project
RUN gradle build


# Export Project
COPY build/libs/** ./


# Environment Variables
ENV HTTP_ADDRESS=0.0.0.0
ENV HTTP_PORT=8080


# Expose Stuff
EXPOSE $HTTP_PORT


# Entrypoint
CMD [ "java", "-jar", "pancake-1.0.0.jar" ]
