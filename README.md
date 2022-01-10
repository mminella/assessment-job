# Project Assessment Batch Job

This batch job will obtain many of the metrics available from Github using the Github REST Search API.

## Configuration

Provide an `application.properties` file with the following:

```
spring.main.web-application-type=none
spring.projectassessment.githubUser=<GITHUB USER>
spring.projectassessment.githubToken=<GITHUB TOKEN>
// projectUsers are the members of the team and not community members
spring.projectassessment.projectUsers=<COMMA DELIMITED GITHUB IDS>
spring.projectassessment.projectRepo=<GITHUB REPO>
spring.projectassessment.projectId=<STATS.SPRING.IO PROJECT ID>
spring.projectassessment.projectArtifactId=<PROJECT ARTIFACT ID> 
spring.projectassessment.dependency=<START.SPRING.IO DEPENDENCY NAME>
//If turned to false, the values of the 
spring.projectassessment.outputDates=true

# spring.elasticsearch.rest.* properties must be configured to point to the backend
spring.elasticsearch.rest.uris=<ELASTICSEARCH_API_URL>
spring.elasticsearch.rest.username=<STATS.SPRING.IO USERNAME>
spring.elasticsearch.rest.password=<STATS.SPRING.IO PASSWORD>
```

An example is as follows

```
spring.main.web-application-type=none
spring.projectassessment.githubUser=mminella
spring.projectassessment.githubToken=A4b3c2d1ezfygxhwivjuk1l2m3n4o5p6q7r8s9t0
spring.projectassessment.projectUsers=benas,mminella,cppwfs
spring.projectassessment.projectRepo=repo:spring-projects/spring-batch
spring.projectassessment.projectId=spring-batch
spring.projectassessment.projectArtifactId=spring-batch-core
spring.projectassessment.dependency=batch
spring.projectassessment.outputDates=true

# spring.elasticsearch.rest.* properties must be configured to point to the backend
spring.elasticsearch.rest.uris=https://some_url_to_elastic
spring.elasticsearch.rest.username=some_username
spring.elasticsearch.rest.password=some_password
```
To run the job, build it from the root of the project with `mvn clean package` and then execute the command `java -jar target/assessment-job-0.0.1-SNAPSHOT.jar`

NOTE: The Github Issue tags used to identify if an issue is a backlog/etc issue, are hardcoded in the code so please modify as needed.