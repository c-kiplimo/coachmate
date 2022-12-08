FROM adoptopenjdk/openjdk11:alpine-jre

ADD target/*.jar /app.jar

RUN ls -ltrh /

# default environment
ENV JVM_OPTS=""

ENV SPRING_OPTS=""

ENTRYPOINT ["java", "-jar", "/app.jar"]

EXPOSE 10290