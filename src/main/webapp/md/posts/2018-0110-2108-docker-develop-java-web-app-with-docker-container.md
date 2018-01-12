This is a note of setting up docker container for developing Java web app.<br>The current blog website is used as an example to run in docker container.

## Creat Dockerfile

First of all, a Dockerfile is needed for specify the container environment.<br> BTW. The 'Dockerfile' is a file naming convention as the 'makefile' to make, 'pom.xml' to maven.

```
FROM openjdk:8-jre-alpine
Add target /opt/my-blog/target
WORKDIR /opt/my-blog/

EXPOSE 80
EXPOSE 8000

ENV REMOTE_DEBUG='-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n'

ENV JAVA_OPTS="${REMOTE_DEBUG}"

CMD ["target/bin/webapp", "--port", "80"]
```
To keep the image as small as possible, I use openjdk:8-jre-alpine as my base image. The target folder is copied into the docker image. So later on we may start up my webapp inside the container.

JDWP is enabled as well for remote debugging. To me, this is a crucial feature of a devloping environment. I'd like to attach to the running java process with connection like 'localhost:8000' from my IDE for debugging.


Once the docker container started, it automatically executes command "target/bin/webapp --port 80" which starts up the web app and bind to port 80.


## Make docker image

This is an easy step. Simply create docker image with command:

``` 
$ docker build -t my-blog .
```
As the command shows, I name the image as 'my-blog'.


## Run docker container

Run the docker container with command:

```
$ docker run --rm -p 80:80 -v "${PWD}"/src/main/webapp:/opt/my-blog/src/main/webapp my-blog
```
This command starts up docker container with the 'my-blog' image.
Furthermore, it bind the host 'webapp' directory to the 'webapp' directory in container environment. With this directory binding, I am able to update static content files without restarting the docker container.


Docker is a handy tool for rapidly recreating consistent developing environment. Hopefully this post is helpful for you as well.


That is it. Bye for now.


<!--eof-->
