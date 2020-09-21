FROM amazoncorretto:11
WORKDIR /var/lib/component/java
COPY target/benchmarking-0.0.1-SNAPSHOT.jar /var/lib/component/java/lib/service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/var/lib/component/java/lib/service.jar"]