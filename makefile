.PHONY: default

build:
	mvn package

clean:
	mvn clean

run:
	mvn exec:exec

default: build
