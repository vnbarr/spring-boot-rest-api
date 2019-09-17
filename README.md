# Backend API

## Cloning the project

    git clone https://

### Installing dependencies locally

We will need to install the dependencies locally. This is the command you will need to run

    mvn clean install
    
### Running application
    spring-boot:run -Dspring.profiles.active=local

### Database initialization

## Create database

   CREATE DATABASE "db_test";

## Run db.migration
   Scripts are under db/migration folder, you can setup migrations using flyway framework