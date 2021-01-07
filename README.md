# Project Assessment Batch Job

This batch job will obtain many of the metrics available from Github using the Github REST Search API.

## Configuration

Provide an `application.properties` file with the following:

```
spring.main.web-application-type=none
github.user=<YOUR_GITHUB_USER>
github.token=<YOUR_GITHUB_TOKEN>
spring.project.users=<GITHUB_IDS_FOR_EACH_TEAM_MEMBER> //This is comma delimited
spring.project.repo=<repo:GITHUB_RELATIVE_URL_TO_REPO>
```

An example is as follows

```
spring.main.web-application-type=none
github.user=mminella
github.token=a1b2c3d4e5f6g7h8i9j0k1l2m3n4o5p6q7r8s9t0
spring.project.users=benas,mminella
spring.project.repo=repo:spring-projects/spring-batch
```
