# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/3.2.6/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/3.2.6/maven-plugin/reference/html/#build-image)
* [Distributed Tracing Reference Guide](https://micrometer.io/docs/tracing)
* [Getting Started with Distributed Tracing](https://docs.spring.io/spring-boot/docs/3.2.6/reference/html/actuator.html#actuator.micrometer-tracing.getting-started)
* [Spring Boot Testcontainers support](https://docs.spring.io/spring-boot/docs/3.2.6/reference/html/features.html#features.testing.testcontainers)
* [Testcontainers Vault Module Reference Guide](https://java.testcontainers.org/modules/vault/)
* [Testcontainers Postgres Module Reference Guide](https://java.testcontainers.org/modules/databases/postgres/)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/3.2.6/reference/htmlsingle/index.html#actuator)
* [Spring Batch](https://docs.spring.io/spring-boot/docs/3.2.6/reference/htmlsingle/index.html#howto.batch)
* [Spring Cache Abstraction](https://docs.spring.io/spring-boot/docs/3.2.6/reference/htmlsingle/index.html#io.caching)
* [Config Client Quick Start](https://docs.spring.io/spring-cloud-config/docs/current/reference/html/#_client_side_usage)
* [Eureka Discovery Client](https://docs.spring.io/spring-cloud-netflix/docs/current/reference/html/#service-discovery-eureka-clients)
* [Cloud Bootstrap](https://docs.spring.io/spring-cloud-commons/docs/current/reference/html/)
* [Vault Client Quick Start](https://docs.spring.io/spring-cloud-vault/docs/current/reference/html/#client-side-usage)
* [Spring Data JDBC](https://docs.spring.io/spring-boot/docs/3.2.6/reference/htmlsingle/index.html#data.sql.jdbc)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.6/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/docs/3.2.6/reference/htmlsingle/index.html#using.devtools)
* [JDBC API](https://docs.spring.io/spring-boot/docs/3.2.6/reference/htmlsingle/index.html#data.sql)
* [Testcontainers](https://java.testcontainers.org/)
* [Thymeleaf](https://docs.spring.io/spring-boot/docs/3.2.6/reference/htmlsingle/index.html#web.servlet.spring-mvc.template-engines)
* [Validation](https://docs.spring.io/spring-boot/docs/3.2.6/reference/htmlsingle/index.html#io.validation)
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.6/reference/htmlsingle/index.html#web)

### Guides

The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service with Spring Boot Actuator](https://spring.io/guides/gs/actuator-service/)
* [Creating a Batch Service](https://spring.io/guides/gs/batch-processing/)
* [Caching Data with Spring](https://spring.io/guides/gs/caching/)
* [Service Registration and Discovery with Eureka and Spring Cloud](https://spring.io/guides/gs/service-registration-and-discovery/)
* [Using Spring Data JDBC](https://github.com/spring-projects/spring-data-examples/tree/master/jdbc/basics)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Accessing Relational Data using JDBC with Spring](https://spring.io/guides/gs/relational-data-access/)
* [Managing Transactions](https://spring.io/guides/gs/managing-transactions/)
* [Handling Form Submission](https://spring.io/guides/gs/handling-form-submission/)
* [Validation](https://spring.io/guides/gs/validating-form-input/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Testcontainers support

This project
uses [Testcontainers at development time](https://docs.spring.io/spring-boot/docs/3.2.6/reference/html/features.html#features.testing.testcontainers.at-development-time).

Testcontainers has been configured to use the following Docker images:

* [`postgres:latest`](https://hub.docker.com/_/postgres)
* [`openzipkin/zipkin:latest`](https://hub.docker.com/r/openzipkin/zipkin/)

Please review the tags of the used images and set them to the same as you're running in production.

