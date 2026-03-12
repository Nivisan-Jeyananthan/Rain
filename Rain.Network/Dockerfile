# multi-stage build for the Rain.Network console server
# build stage uses a full JDK to compile sources
FROM openjdk:27-ea-12-jdk-trixie AS build

# set working directory and copy source
WORKDIR /app
COPY src ./src

# compile all java sources into classes directory
RUN mkdir -p classes \
    && find src -name "*.java" > /tmp/sources.txt \
    && javac -d classes @/tmp/sources.txt

# runtime stage uses a slim JRE image
FROM openjdk:27-ea-12-jdk-trixie
WORKDIR /app

# copy compiled classes from build stage
COPY --from=build /app/classes ./classes

# default port (can be overridden when starting the container)
ENV PORT=8080

# entrypoint runs the server main class; port is appended as an argument
ENTRYPOINT ["java", "-cp", "classes", "ch.nivisan.raincloud.network.server.ServerMain"]
# allow a port to be specified via docker run arguments or environment
CMD ["${PORT}"]
