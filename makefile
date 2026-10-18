.PHONY: default

build:
	mvn package

docker-build:
	docker build -t my-blog .

docker-run:
	docker run --rm -p 80:80 -p 8000:8000 -p 9010:9010 -p 9011:9011  -v "${PWD}"/src/main/webapp:/opt/my-blog/src/main/webapp my-blog

clean:
	mvn clean

webapp:
	mvn package exec:exec@webapp

static:
	mvn package resources:copy-resources@copy-static-files exec:exec@static

static-serv:
	jwebserver -b :: -p 8080 -d "`pwd`/static-dist"

push-lfs:
	git lfs push origin master --all

default: build
