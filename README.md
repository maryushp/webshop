# Webshop - Spring Boot RESTful API

___

## Overview

Webshop is a simple online web shop implemented as a RESTful API using Spring Boot.
The application provides two roles: User and Admin. Users can perform operations such as creating orders, searching 
for items buy categories or names, and viewing their order history. Admins, on the other hand, have additional 
privileges for managing items, categories, and viewing all orders.

___

## Table of Contents

* [General Info](#general-information)
* [Technologies Used](#technologies-used)
* [Database](#database)
___

## General Information

- This version is created to migrate to the Spring Boot from Spring and add major features.
- New added features are: 
  - Hibernate
  - Zalando Problem for exception handling
  - Data Transfer Object
  - JsonMergePatch for update methods
  - Pagination
  - Spring security
  - JWT token
  - New entities
- System expose REST APIs to perform CRUD operations for entities.
___

## Technologies Used

- Java 11
- Spring Boot framework
- Lombok
- JUnit, Mockito, H2Database
- Swagger
- MySQL database
- Maven Build Tool
- JPA Hibernate
- Zalando Problem
- Data Transfer Object
- JsonMergePatch
- Jakarta Validation
- Spring Security
- JWT
___

## Database
Database scrypt can be found in database directory

![db_scheme.png](database%2Fdb_scheme.png)