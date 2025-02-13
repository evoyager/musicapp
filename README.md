### Java Spring template project

This project is based on a GitLab [Project Template](https://docs.gitlab.com/ee/gitlab-basics/create-project.html).

Improvements can be proposed in the [original project](https://gitlab.com/gitlab-org/project-templates/spring).

### CI/CD with Auto DevOps

This template is compatible with [Auto DevOps](https://docs.gitlab.com/ee/topics/autodevops/).

If Auto DevOps is not already enabled for this project, you can [turn it on](https://docs.gitlab.com/ee/topics/autodevops/#enabling-auto-devops) in the project settings.

### Run locally

Set those values:
in resource-service/src/main/resources/application.properties:
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres

in resource-service/src/main/java/com/epam/resource/client/SongServiceClient.java:
String songServiceUrl = "http://localhost:8081/songs";

in: song-service/src/main/resources/application.properties:
spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
spring.datasource.username=postgres

### Run in Docker

Set those values:
in resource-service/src/main/resources/application.properties:
spring.datasource.url=jdbc:postgresql://db:5432/postgres

in resource-service/src/main/java/com/epam/resource/client/SongServiceClient.java:
String songServiceUrl = "http://song-service:8081/songs";

in: song-service/src/main/resources/application.properties:
spring.datasource.url=jdbc:postgresql://db:5432/postgres




