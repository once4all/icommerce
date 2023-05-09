# Introduction 

# About the Project
The project contain 4 services: 
1. Authentication service: for authentication and user management.
2. Product service: use for searching the products.
3. Cart service: cart and booking management.
4. Common service: which contains endpoints for getting category-like data.  

All the 4 can be easily separated as a microservice but in this project I include them in 1 service only to keep it simple enough.

# Project structure
As you can see, I have 3 children projects under the ICommerce parent project:
1. icommerce-api: contains all endpoints' configuration files, and their generated code. This part communicate with clients.
2. icommerce-infrastructure: contains dom, repository files which contact and setup the DB.
3. icommerce-service : contains implementation and business classes, all other problems resolved here.

# Technologies and libraries
Some notable techs and libs in this project are:
1. Spring Boot
2. Hibernate
3. Open API 3 plugins : for generating boiler plate code
4. Spring security
5. Lombok : for annotation procssing
6. Mapstruct

# Principles
For designing the classes , I always follow these principles:
1. OOP
2. SOLID : the code will be easier to extends, less effort.
3. YAGNI and KISS : the code provide what I think are the client's need, no over engineering.
4. DRY: try to reduce the duplicate code.
5. MVC : as you can see from the project structure. 

# Build and Test
These steps show how to build your code and run the tests.
1.  Create a database, in this project I use **PostgresSQL** as DB
2.  Run initialize SQL in folder script
3.  Build & create docker image:
~~~
cd icommerce-service
mvn clean install
docker build -t icommerce-service:0.0.1 .
~~~
4.  Run docker image with specific environment variables, change the db variables to yours:
~~~
docker run --name icommerce-service -p 8089:8089 -e spring.datasource.url=jdbc:postgresql://10.144.212.73:5432/icommerce?currentSchema=public -e spring.datasource.username=postgres -e spring.datasource.password=postgres  icommerce-service:0.0.1
~~~
**Note:** use the correct db URL
# Room for improvements
In this project I could have improve many things 
- Add a central configuration service
- Add deployment infrastructure design
- Using cache or elastic search to improve product searching
- Add more unit tests 
- ...

# CURL commands
Replace the url localhost:8089 to any other url that you started, the services require authentication in the header so please replace the ${token_param} with your access_token after login
1. Login with user
~~~
curl --location --request POST 'localhost:8089/authenticate/login' \
--header 'Content-Type: application/json' \
--data-raw '{
    "username":"test",
    "password":"test"
}'
~~~

2. Get all categories
~~~
curl --location --request GET 'localhost:8089/common/categories' \
--header 'Authorization: Bearer ${token_param}'
~~~

3. Get all categories
~~~
curl --location --request GET 'localhost:8089/common/brands' \
--header 'Authorization: Bearer ${token_param}'
~~~

4. Get all colours
~~~
curl --location --request GET 'localhost:8089/common/colours' \
--header 'Authorization: Bearer ${token_param}'
~~~

5. Find product
~~~
curl --location --request GET 'localhost:8089/product?colours=1,2,3&priceTo=101000&keyword=b' \
--header 'Authorization: Bearer ${token_param}' \
--data-raw ''
~~~

6. Get current cart
~~~
curl --location --request GET 'localhost:8089/cart' \
--header 'Authorization: Bearer ${token_param}'
~~~

7. Get list booking 
~~~
curl --location --request GET 'localhost:8089/booking' \
--header 'Authorization: Bearer ${token_param}'
~~~

8. Add item to current cart
~~~
curl --location --request POST 'localhost:8089/cart/add-item' \
--header 'Authorization: Bearer ${token_param}' \
--header 'Content-Type: application/json' \
--data-raw '{
    "productId": 3,
    "colourId": 2,
    "quantity": 1
}'
~~~

9. Remove item to current cart
~~~
curl --location --request POST 'localhost:8089/cart/remove-item/2' \
--header 'Authorization: Bearer ${token_param}' \
--header 'Content-Type: application/json' \
--data-raw '{
    "quantity":"2"
}'
~~~

10. Checkout current cart 
~~~
curl --location --request POST 'localhost:8089/cart/checkout' \
--header 'Authorization: Bearer ${token_param}' \
--header 'Content-Type: application/json' \
--data-raw '{
    "totalPrice":200000
}'
~~~
