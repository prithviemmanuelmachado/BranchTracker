# Branch tracker
> The aim of this project is to implement a RestAPI gateway and microservices in Spring boot using RabbitMQ for the backend, and Angular with Redux for the frontend

# Architecture
* The Frontend is developed using angularJS
* The Backend is developed using spring boot
* The Backend consists of a gateway and 2 microservices
* All user related activities are handled by User_Service
* All branch related activities are handled by Branch_Service
* The gateway communicates with the microservices using rabbitMQ
* Some functions are request/response type, some are just request and forget
* For the frontend the app uses REST APIs for user tasks and web sockets for branch related tasks