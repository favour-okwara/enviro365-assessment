# Investment Management System

The Investment Management System is a Spring Boot application that provides RESTful API endpoints for managing investor profiles, products, and withdrawal notices.

## Table of Contents
- [Overview](#overview)
- [Installation](#installation)
- [API Endpoints](#api-endpoints)
- [Features](#features)
- [Usage Examples](#usage-examples)

## Overview

The Investment Management System is designed to simplify the management of investor-related data. It allows users to retrieve investor information, view associated products, create withdrawal notices, and generate statements.

## Installation

To run the application locally, follow these steps:

1. Clone the repository:
   ```bash
   git clone https://github.com/favour-okwara/enviro365-assessment.git
   ```

2. Navigate to the project directory:
   ```bash
   cd enviro365-assessment
   ```

3. Build the project:
   ```bash
   mvn clean install
   ```

4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

### Investor

- `GET /api/investors/{id}`: Retrieve investor information by ID.

### Product

- `GET /api/investors/{id}/products`: Retrieve a list of products associated with the investor.
- `GET /api/investors/{investorId}/products/{productId}`: Retrieve detailed information about a specific product.

### Withdrawal Notice

- `POST /api/investors/{investorId}/products/{productId}/withdraw`: Create a withdrawal notice for the specified product.
- `GET /api/investors/{investorId}/products/{productId}/statements`: Generate a statement for withdrawal notices within a date range.

## Features

- View investor details.
- Retrieve product information.
- Create withdrawal notices.
- Generate statements for withdrawal notices.

## Usage Examples

- Retrieve investor information:
  ```bash
  GET /api/investors/1
  ```

- Generate a statement for withdrawal notices within a date range

    ```bash
    # Generate a statement for withdrawal notices starting from date to current date
    GET /api/investors/1/products/2/statements?start=2023-01-01

    # Generate a statement for withdrawal notices within a date range.
    GET /api/investors/1/products/2/statements?start=2023-01-01&end=2023-12-31
    ```


- Create a withdrawal notice:
  ```bash
  POST /api/investors/1/products/2/withdraw
  Body:
  {
    "amount": 1000.0,
    "withdrawalDate": "2024-03-15",
    "bankName": "Standard Bank",
    "accountNumber": "1234567890"
  }
  ```

