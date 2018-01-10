.PHONY: default

build:
	mvn package

docker-image: build
	docker build -t my-blog .

run-docker:
	docker run --rm -p 80:80 my-blog

clean:
	mvn clean

run:
	mvn exec:exec

default: build
