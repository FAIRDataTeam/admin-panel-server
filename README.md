# Admin Panel - Server Application

> It's a server part of the Admin Panel application.

[![Build Status](https://travis-ci.org/FAIRDataTeam/admin-panel-server.svg?branch=master)](https://travis-ci.org/FAIRDataTeam/admin-panel-server.svg?branch=master)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE.md)

`Admin Panel` serves for managing and deploying the configurations of the Fair Data Point instances 

## Deployment

The simplest way is to use [Docker Compose](https://docs.docker.com/compose/). Requirements are just to have Docker installed, privileges for current user and the Docker daemon started.

1.  Create a folder (e.g., `/panel`, all commands in this manual are from this working directory)
2.  Copy (and adjust) docker-compose.yml provided below
3.  Run the Admin Panel with Docker compose `docker-compose up -d`
4.  After starting up, you will be able to open the Admin Panel in your browser on <http://localhost:8080>
5.  You can use `docker-compose logs` to see the logs and `docker-compose down` to stop all the services

```
version: '3'
services:

  server:
    image: fairdata/admin-panel-server
    restart: always
    ports:
      - 8080:8080
    links:
      - mongo:mongo

  mongo:
    image: mongo:4.0.12
    restart: always
    ports:
      - 27017:27017
    command: mongod
```

## Contribute

### Requirements

 - **Java** (recommended 1.8)
 - **Maven** (recommended 3.2.5 or higher)
 - **Docker** (recommended 17.09.0-ce or higher) - *for build of production image*

### Build & Run

Run these comands from the root of the project

```bash
$ mvn spring-boot:start
```

### Run tests

Run these comands from the root of the project

```bash
$ mvn test
```

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for more details.
