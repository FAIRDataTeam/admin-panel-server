FROM openjdk:11-jdk-slim

WORKDIR /admin-panel

ADD target/admin-panel.jar /admin-panel/app.jar
ADD target/classes/application-production.yml /admin-panel/application-production.yml

ENTRYPOINT java -jar app.jar --spring.config.location=classpath:/application.yml,file:/admin-panel/application-production.yml
