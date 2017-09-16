.PHONY: default

default: run

clean:
	mvn clean

build: clean
	mvn package 

run: build
	mvn package exec:exec

deploy:
	mvn clean heroku:deploy-war
