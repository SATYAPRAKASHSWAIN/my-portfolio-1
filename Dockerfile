FROM openjdk:21
EXPOSE 8080
COPY target/my-portfolio-1.jar /usr/app/
WORKDIR /usr/app

ENTRYPOINT [ "java","-jar","/my-portfolio-1.jar" ]