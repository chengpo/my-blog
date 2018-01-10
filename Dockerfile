FROM openjdk:alpine
ADD src/main/webapp  /opt/my-blog/src/main/webapp
ADD target  /opt/my-blog/target
WORKDIR /opt/my-blog/

EXPOSE 80

CMD ["target/bin/webapp", "--port", "80"]
