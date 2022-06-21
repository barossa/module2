FROM openjdk:8

ARG WAR_FILE=controller/target/*.war

COPY ${WAR_FILE} /usr/opt/rest-app/app.war

WORKDIR /usr/opt/rest-app

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.war"]