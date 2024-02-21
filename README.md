# Prerequisites
While developing this project, some assumptions have been made regarding things that were not explicitly
said in project description or in enough details. Some of them are listed below:
- billing hour of a school is meant by for example 14:00:00 - 14:59:59 with only exception being 12:00:00
as 12:00:01 is considered as a start point of billing
- even though a child most likely has two parents, domain description implied 1:n relation Parent-Child so
an assumption was made that one imaginary parent account exists in system per child in school that both parents use
to read information about payments
- while probably nobody leaves a child at school at 1am nor picks it up at 11pm, it is not verified, as
long as entry and exit dates are on the same day and exit date is later in time than entry date, service is working
- database of choice here is H2 with in-memory setting and DDL and DML scripts to set it up for verification purposes

# Technologies and tools used
- Java 17
- Spring Web, JPA
- Hibernate 
- H2 database 
- Lombok
- JUnit, Mockito, RestAssured
- IntelliJ IDEA, Maven, Git

# API info
- GET `/api/statements/school/{schoolID}`  
query params: `year, month`  
example: `/api/statements/school/123?year=2024&month=2`
- GET `/api/statements/school/{schoolID}/parent/{parentID}`  
query params: year, month  
example: `/api/statements/school/123/parent/2?year=2024&month=2`

# DB info
![database-pseudo-schema](https://github.com/mtx210/Interview-task/assets/38227623/666c3724-905c-4c80-b420-162086c28ef4)
