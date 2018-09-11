FROM docker.registry/openjdk:8u171-jdk-alpine3.8

COPY target/uaas-java-1.0-SNAPSHOT.jar /root/uaas-java-1.0-SNAPSHOT.jar

EXPOSE 9000

CMD ["java", "-jar", "/root/uaas-java-1.0-SNAPSHOT.jar"]