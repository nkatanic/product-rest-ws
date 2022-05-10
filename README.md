# product-rest-ws
Example implementation of REST web service with Spring Boot and Spring Web MVC. Example implements a simple REST service that manages a list of products. Service provides a REST API with the following endpoints:

| HTTP method | Path                       | Action                     |
| ----------- | -------------------------- | -------------------------- |
| POST        | /api/products              | Create new product         |
| GET         | /api/products              | Get all products           |
| GET         | /api/products/{id}         | Get product by id          |
| GET         | /api/products/isAvailable  | Get all available products |
| PUT         | /api/products/{id}         | Update product by id       |
| DELETE      | /api/products/{id}         | Delete product by id       |
| DELETE      | /api/products              | Delete all products        | 

<br/>

# Setting up the environment
The following instructions have been tested and proven to work on Ubuntu 20.04.

## Install Java JDK 11
https://www.digitalocean.com/community/tutorials/how-to-install-java-with-apt-on-ubuntu-20-04

## Install Maven 3.6.3
https://linuxize.com/post/how-to-install-apache-maven-on-ubuntu-20-04/

## Install and configure GIT 2.25.1
https://www.digitalocean.com/community/tutorials/how-to-install-git-on-ubuntu-20-04

## Install and configure PostgreSQL 12.9
### Install

https://www.digitalocean.com/community/tutorials/how-to-install-postgresql-on-ubuntu-20-04-quickstart

### Configure for this example
https://computingforgeeks.com/installing-postgresql-database-server-on-ubuntu/ 

**Set password for *postgres* user:**

`user0@machine0:~$ sudo su - postgres`

`postgres@machine0:~$ psql -c "alter user postgres with password 'some_password'"`

**Create test user (*dbuser*), database (*testdb*) and set database ownership to *dbuser* :**

`postgres@machine0:~$ createuser dbuser`

`postgres@machine0:~$ createdb testdb -O dbuser`

**Connect to database and set the password for *dbuser* :**

`postgres@machine0:~$ psql testdb`

`testdb=# alter user dbuser with password 'dbuser';`

<br/>

# Running the service

Navigate to the project root path (where pom.xml is located) and run:

`./mvnw spring-boot:run`

Alternatively, you can build the executable JAR file and run that:

`./mvnw clean package`

`java -jar target/product-rest-ws-0.0.1-SNAPSHOT.jar`

To run tests only:

`./mvnw test`
