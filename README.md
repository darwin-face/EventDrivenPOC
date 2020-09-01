# Email Sending Service

## Introduction
This is an intermediate service to send emails to a 3rd party emailing API. You have a choice on which email service to 
send to. The Snailgun service is asynchronous, but can be queried to fetch the updated information, and the Spendgrid service
is a synchronous email service.

## How to Run
In the root folder, run `docker-compose up` to start the database and the queue,
and then run `gradle bootRun` to start the application. It should start on `localhost:8080`, and you can hit
`localhost:8080/greeting` to check if the application is up. In the `application.properties`, the `email.spendgrid.api-key`
and the `email.snailgun.api-key` both need to be updated with the appropriate API keys.

## How to Use
Depending on which email service you deploy the application with, the `email.service` in the `application.properties` needs
to be updated to either `spendgrid` or `snailgun`. Afterwards, you can POST to the `/email` endpoint with a request body that 
looks as follows

      {
          "to": "susan@abcpreschool.org",
          "to_name": "Miss Susan",
          "from": "noreply@mybrightwheel.com",
          "from_name": "brightwheel",
          "subject": "Your Weekly Report",
          "body": "<h1>Weekly Report</h1><p>You saved 10 hours this week!</p>"
      }

## Why certain things are used
### Gradle 
This is a java Spring Boot service, but it is built using Gradle over Maven. 
This is done because even though most Spring Boot applications use Maven, the json configuration of gradle would make it 
easier to read for anyone used to other languages, especially other developers coming from an Android development background.

### Java
I'm primarily a Java developer, and using the language I'm most familiar with helped me develop this quicker. I actually
use a fairly standard code skeleton that I try to update as the tech updates. If I had more time, I might use Typescript/Node 
instead so that the front end developers could read and understand this code base quicker.

### Lombok
Project lombok helps extract a lot of java boilerplate on POJOs a lot, letting the developer use annotations
instead of having to hand write a lot of getter and setter methods.

### Docker, Postgres
By bundling with docker, I can push up a configuration file, which will have a Postgres Database to be able to store the
emails that are sent to the Spendgrid and Snailgun API to create a sort of stored "out/sentbox". 

## If I had more time I would
1 - do more verification on the emails used for both the "to" and "from" pieces of the email so that the API 
would be more robust against email spoofing attacks.

2 - write a full test suite, including Junit tests for each object, and Spock integration tests for end to end coverage. Everything
here has been manually tested, but would like to make a fully automated test suite for a CI/CD pipeline, or maybe have taken 
a more TDD approach

3 - write a queue system for eventual consistency between this system, and the external API

4 - extract out different services/repositories for the Spendgrid and Snailgun send that can be called within the email 
    service so that they could be split out into different microservices if need be

5 - figure out if Snailgun has webhooks to be able to push statuses into the system. If not, make a cron job to 
    regularly check statuses on emails instead of having to manually hit a GET endpoint to be able to pull them in
    
6 - make Typed Id objects for the different entities (SpendgridEmailId, SnailgunEmailId, one on for the returned object)

7 - add another GET endpoint to fetch all the emails sent for a particular email address