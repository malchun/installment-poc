# Installment processor PoC
PoC for installment processor system using Temporal and Spring Boot. The main purpose of this project is to show the 
features of Temporal through a very basic implementation of an installment processor system.

## Features
Short description of the functionalities of the installment processor system.

## Dev Setup
### Docker
- Run infrastructure
```bash 
docker-compose -f docker/docker-compose-infra.yaml up -d
```
- Build and run the application
```bash
mvn clean install
docker-compose -f docker/docker-compose-app.yaml up --build
```
- To rebuild the application run ths same commands as for the first run
- For debug purposes, you can run pgadmin
```bash
docker-compose -f docker/docker-compose-devtools.yaml up -d
```
- To stop the infrastructure
```bash
docker-compose -f docker/docker-compose-infra.yaml down
```

### Usage 
Service exposes SwaggerAPI that can be used to interact with the service.
``` http://127.0.0.1:3031/swagger-ui/index.html ```
You can find available endpoints and their descriptions there.

### Metrics 
Service exposes prometheus metrics using springboot actuator.
``` http://127.0.0.1:3031/actuator/prometheus ```

### Faulty regime to test error handling
To enable faulty regime set parameter `installmentpoc.faultchance` to the desired probability of fault, for example `0.5` 
in the `application.yaml` for the used profile. If you won't the service to work stable set it to '0'.

## Requirements
### Creation and processing of installments
Example input:
```json
{
  "totalPrice": 1000,
  "downPaymentAmount": 100,
  "numberOfPayments": 3,
  "paymentIntervalInSeconds": 15,
  "customerId": "customer1"
}
```
Creation method would return installment id being created and start the processing of installment.
When an installment is defined, the application should calculate the amount of money to be charged for every installment
(e.g., for the example above $1000 total - $100 down payment, divided by number of payments (3) = $300 per payment, and 
schedule the payments using the predefined interval. e.g., one payment every 15 sec.

### Notification of completion
Example notification:
```json
{
  "installmentId": "installment1",
  "customerId": "customer1"
}
```
When an installment is fully paid, the system should raise a notification that includes the installment id and 
associated customer id. 

### Cancellation of installments
Example input:
```json
{
  "installmentId": "installment1"
}
```
There should be the ability to stop a scheduled installment at any time and refund the money charged so far.
This would be done by providing installment id. Application should issue refunds and calculate the amounts. 

### Error handling in installments
The application should handle errors in the installment processing. For example, if the charge fails, the installment
should retry the charge a few times before failing the installment.
Implemented by a InstallmentWithRefundWorkflow, that automatically refunds everything that is charged in case of an 
exception in Installment Workflow.

## Refund of charges in installments
Example input:
```json
{
  "installmentId": "installment1"
}
```
There should be a way for the system to refund started installments.


## Basic premise 
Create an installment by defining
Total price, e.g., $1000
Down payment amount (less or equal to total price) e.g., $100
Number of payments, e.g. 3
Payment interval, e.g., 15. For the purpose of the demo this could be express in seconds.
Customer id.
Creation method would return installment id being created and set its status to “Initiated”.
When an installment is defined, the application should calculate the amount of money to be charged for every installment (e.g., for the example above $1000 total - $100 down payment, divided by number of payments (3) = $300 per payment, and schedule the payments using the predefined interval. e.g., one payment every 15 sec.
Charging the down payment should set the installment status to “In progress”.
There should be the ability to stop a scheduled installment at any time and refund the money charged so far. This would be done by providing installment id. Application should issue refunds and calculate the amounts. It should set the status of the installment to “Cancelled“.
You can assume you have the following payments methods available:
charge(customer id, amount) - will charge the customer the specific amount, will return transaction id
refund(transaction id) - will refund specific transaction id
When an installment is fully paid, the system should raise a notification that includes the installment id and associated customer id. The installment status should be set to “Completed”.


## Minikube deployment with Helm (WIP)
Will be able to run on minikube. Based on https://github.com/temporalio/helm-charts.
Prerequisites:
- minikube
- helm
- kubectl
- docker

#### Instruction
- Prepare minikube
```bash
minikube start
eval $(minikube docker-env)
helm dependencies update
```
- Run postgres initiated with temporal schemas. You can initialize schemas with temporal container and then stop it.
```bash 
docker-compose -f docker/docker-compose-infra.yaml up postgresql -d
```
- Run temporal in minikube and check that it is running
```bash
helm install temporaltest -f values/values.postgresql.yaml . --timeout 300s
kubectl --namespace=default get pods -l "app.kubernetes.io/instance=temporaltest"
```
- Expose the ui
```bash
kubectl port-forward services/temporaltest-web 8080:8080
```

