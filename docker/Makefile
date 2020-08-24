#!/bin/sh

DOCKER=docker

# Tomcat9 
DOCKERFILE_TOMCAT9=Dockerfile.tomcat9_jdk8
IMAGE_TOMCAT9=tomcat9_jdk8

build.tomcat9:
	$(DOCKER) build -f ./$(DOCKERFILE_TOMCAT9) -t $(IMAGE_TOMCAT9) .
