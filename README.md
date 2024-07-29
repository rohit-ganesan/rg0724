# Tool Rental Application

## Overview

This is a simple tool rental application that allows users to rent tools from a store. The application supports adding, removing, and checking out tools. It also handles tool rental agreements, including calculating charges based on rental days, holidays, weekends, and discounts.

## Features

- Add tools
- Remove tools
- Checkout tools
- Generate rental agreements with detailed charges
- Error handling for invalid tool codes, rental days, and discounts

## Supported Tool Types

- Chainsaw
- Ladder
- Jackhammer

## Getting Started

### Prerequisites

- Java 19
- Maven
- Spring Boot

### Setup

1. Clone the repository:

    ```sh
    git clone <repository-url>
    cd tool-rental-application
    ```

2. Build the project using Maven:

    ```sh
    mvn clean install
    ```

3. Run the application:

    ```sh
    mvn spring-boot:run
    ```

### Configuration

Supported tool types can be configured in the `application.properties` file:

```properties
supported.tool.types=Chainsaw,Ladder,Jackhammer
```
## API Endpoints

### Add Tool

#### Endpoint:

```POST /tools/add```

#### Request Body:

```
{
"code": "LADW",
"type": "Ladder",
"brand": "Werner",
"dailyCharge": 1.99,
"weekdayCharge": true,
"weekendCharge": true,
"holidayCharge": false
}
```

#### Responses:

```
201 Created: Tool added successfully.
400 Bad Request: Unsupported tool type.
```

### Remove Tool
Endpoint:

```DELETE /tools/remove/{code}```
#### Responses:

```
200 OK: Tool removed successfully.
404 Not Found: Tool with the specified code not found.
```

### Checkout Tool
#### Endpoint:
```POST /rental/checkout```

#### Request Body:

```
{
"toolCode": "LADW",
"rentalDays": 3,
"discountPercent": 10,
"checkoutDate": "2020-07-02"
}
```
#### Responses:

```
200 OK: Rental agreement with detailed charges. 
400 Bad Request: Invalid tool code, rental days, or discount percent.
```
#### Example Response:
```
{
"toolCode": "LADW",
"toolType": "Ladder",
"toolBrand": "Werner",
"rentalDays": 3,
"checkoutDate": "2020-07-02",
"dueDate": "2020-07-05",
"dailyRentalCharge": 1.99,
"chargeDays": 1,
"preDiscountCharge": 1.99,
"discountPercent": 10,
"discountAmount": 0.20,
"finalCharge": 1.79
}
```