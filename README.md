# Cooking Ideas

Java Spring Boot project for the management of culinary recipes, created to learn the best practices
of Domain Driven
Design.

## Requirements

- Java 17

## Run the project

First run docker containers

```shell
$ docker-compose up -d
```

then run the spring app as usual

## Contributing

### Using colima on osx

To be able to run tests using testcontainer on colima, remember to define following environment variables:

```shell
export DOCKER_HOST="unix://${HOME}/.colima/default/docker.sock"
export TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/var/run/docker.sock
export TESTCONTAINERS_RYUK_DISABLED=true
```