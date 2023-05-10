# Ontop’s Challenge for backend software engineer with Java

# SOFTWARE REQUIREMENT 

1. Install JAVA 11 
2. Spring 2.5.0
3. Maven 3
4. Install PostgresSQL 

# HOW TO IMPORT THE PROJECT ON IDE
1. Clone the code from git repository using https://github.com/oyisco/OntopChallange.git
2. Import the code into the IDE 
3. Maven Clean 
4. Maven Install

# DATASOURCE CONNECTION: 

This is in application.yml inside resource folder , you can decide to change the database name, username and password of your jdbc connection

# RUNNUNG THE APPLICATION LOCALLY
There are several ways to run a Spring Boot application on your local machine. One way is to execute the main method in the com.ontop.challenge.Application class from your IDE.

Alternatively you can use the Spring Boot Maven plugin like so.

mvn spring-boot:run

# API DOCUMENTATION :

The Poject consist of two documentation depending on what you want

# POSTMAN COLLECTION API: 
to access this, just import the postman colletion.
# SWAGGER DOCUMENTATION :
this is accessable by usng the following url  http://localhost:9090/swagger-ui.html#/


# SECURITY :
This project has a JWT security , which means you will need to login. Follow the steps below to login;
1. Create an account using this URL (http://localhost:9090/v1/accounts/create) for user registration which will generate a defualt username and password but this will not be return as part of the account objects
2. Login using this URL http://localhost:9090/v1/accounts/login with username = "idris", password "1234" and it will generate a Bearer token 
3. On POSTMAN collection click on Environment locate the "current value" at the top right and paste the Token with Bearer 

NOTE: This token will expire after 5hr.

