### Tech Stack
- Java 17
- Spring Boot 3
- Mysql 8

### Setup
Set the following properties according to the environment in ``src/main/resources/application.ym``:

```
server port is set to 8087
datasource url=jdbc:mysql://localhost:3306/cleaning_service
datasource username=root
datasource password=my-secret-pw

```

Create Schema named 'cleaning_service' So liquibase can create tables in that.

This project uses liquibase for database DDL scripts and for DML scripts e.g (Adding vehicles and cleaners) CommandLineRunner is user which will populate data when application gets up.

### API Documentation
This project uses Swagger for API documentation. When application is started, the documentation will be available on the following path:

```
http://[host]:[port]/swagger-ui/index.html

```

### Entity Relationship Diagram

![JUSTLIFE ERD](https://github.com/AMurtaza95/JustLifeCaseStudy/blob/main/ERD%20For%20JustLife.PNG)


### Testing
Run the following command to run test cases:

```
mvn clean verify
```
