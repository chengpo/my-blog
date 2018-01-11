.PHONY: default

build:
	mvn package

docker-build:
	docker build -t my-blog .

docker-run:
	docker run --rm -p 80:80 -p 8000:8000 -p 9010:9010 -p 9011:9011  -v "${PWD}"/src/main/webapp:/opt/my-blog/src/main/webapp my-blog

clean:
	mvn clean

run:
	mvn exec:exec

default: build
