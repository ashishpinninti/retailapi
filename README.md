# retailapi

This application is a RESTful service that can perform basic GPPD operations on product and therefore its corresponding 
price details.

- External Framework: Spring Boot
- Data Store: MongoDB

## Running the application:

Make sure MongoDB server is up and running on port 27017.
Run the application by using 'mvn spring-boot:run' command.

## Interacting with application:

### GET - To retrieve product details:

    URI: http://localhost:8080/products/v1/15117729
    Response: 
            {
              "id": 15117729,
              "name": "Apple&reg; iPad Air 2 16GB Wi-Fi - Gold",
              "current_price": {
                "value": "2",
                "currency_code": "INR"
              }
            }
            
### PUT - To update price details:

    URI: http://localhost:8080/products/v1/15117729
    Request Body:
                {
                  "current_price": {
                    "value": "10",
                    "currency_code": "USD"
                  }
                }
    Response:
            {
              "id": 15117729,
              "name": "Apple&reg; iPad Air 2 16GB Wi-Fi - Gold",
              "current_price": {
                "value": "10",
                "currency_code": "USD"
              }
            }
            
## Additional Details:
- The application on start loads the DB with some sample data.
- Similarly POST and DELETE operations can be performed to test various cases.
- The project is equipped with Unit and End-to-End tests.
  - Run 'mvn test' command to run the test cases.
- It handles various error situations by returning proper HTTP Codes and messages.
  




