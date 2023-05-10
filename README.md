# Ontop’s Challenge for backend software engineer with Java

Software Requirement to run the project

1. Install JAVA 11 
2. Spring 2.5.0
3. Maven 3
4. Install PostgresSQL 

How to import the project to your IDE
1. Clone the code form git ropository using https://github.com/oyisco/OntopChallange.git
2. Import the code inot the IDE 
3. Meven Clean 
4. Maven Install
5.Run the project from IDE.

Running the application locally
There are several ways to run a Spring Boot application on your local machine. One way is to execute the main method in the de.codecentric.springbootsample.Application class from your IDE.

Alternatively you can use the Spring Boot Maven plugin like so

mvn spring-boot:run

API DOCUMENTATION 

The Poject consist of two documentation depending of what you want 

POSTMAN COLLECTION API: to access this is part of the email send and all you need to do is to import the collection
SWAGGER DOCUMENTATION : this is accessable by usng the follwong url  http://localhost:9090/swagger-ui.html#/


SECURITY 
THis project has a JWT security which means you will need to login , but for the LOGIN to work u need to first do the following
1. Create Account using this URL (http://localhost:9090/v1/accounts/create) 
2. Login using  http://localhost:9090/v1/accounts/login with usernam = idris and password 1234 this will generate a Bearer token 
3. then on POSTMAN collection click on Environment then locate Ontop Environment and paste the Bearer Token in the current value. 
4.This token will expire after 5hr.

