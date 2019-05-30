# Matchmakers

Matchmakers is a social sharing platform where users can share their favorite movies. Users can express their opinion about a movie by either likes​ or hates​!

##### Technology Stack

* Java 11
* Spring Boot 2
* PostgreSQL 9.4
* Maven 3

##### Database

From a machine with docker installed + internet access, execute:

    docker run -p 5432:5432 --name db -e POSTGRES_PASSWORD=postgres -d postgres:9.4

Make sure your hosts file maps db to localhost

    127.0.0.1       localhost db

Init or migrate the database schema

    Init the database
    mvn clean install -DskipTests -Ddb.host=db -Ddb.port=5432 -Ddb.module.database.name=matchmakers -Ddb.module.userId=matchmakers -Ddb.module.password=matchmakers -Ddb.root.password=postgres -Dinit.database.skip=false flyway:migrate
    
    Migrate an existing database
    mvn clean install -DskipTests -Ddb.host=db -Ddb.port=5432 -Ddb.module.database.name=matchmakers -Ddb.module.userId=matchmakers -Ddb.module.password=matchmakers flyway:migrate

##### Application Configuration

* Default : {PROJECT_HOME}/src/main/resources/matchmakers-application.yml 
* Runtime : {SPRING_CONFIG_LOCATION}/matchmakers-application.yml (if not found, the app defaults to the one in the classpath)

##### Application Execution

Matchmakers is a Spring Boot application thus can be executed as a standalone application, inside a servlet container (Tomcat 9) or running a docker container:

Default configuration may be overridden using the 'spring.config.location' property setting the dirpath of the 'matchmakers-application.yml':

```
mvn spring-boot:run --spring.config.location=/path/to/conf/
```
```
$CATALINA_HOME/bin/startup.sh --Dspring.config.location=/path/to/conf/
```
```
docker build -t matchmakers .
docker run -p 8080:8080 -p 443:443 -p 80:80 --name matchmakers --link db -d matchmakers
```
