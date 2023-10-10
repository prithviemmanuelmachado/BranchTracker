# PicNotes
> The aim of this project is to implement a RestAPI gateway and microservices in Spring boot using RabbitMQ for the backend, and Angular with Redux for the frontend

# Architecture
* The Frontend is developed using angularJS
* The Backend is developed using spring boot
* The Backend consists of a gateway and 2 microservices
* All user related activities are handled by User_Service
* All picture related activitiees are handled by Picture_Service
* The gateway communicates with the microservices using rabbitMQ
* Some functions are request/response type, some are just request and forget
* For the frontend the app uses REST APIs for user related activities and web sockets for picture related activities