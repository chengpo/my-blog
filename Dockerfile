FROM openjdk:8-jre-alpine
# ADD src/main/webapp  /opt/my-blog/src/main/webapp
ADD target  /opt/my-blog/target
WORKDIR /opt/my-blog/

EXPOSE 80
EXPOSE 8000
EXPOSE 9010
EXPOSE 9011


# enable jvm remote jmx
ENV JVM_REMOTE_JMX='-Dcom.sun.management.jmxremote=true -Dcom.sun.management.jmxremote.ssl=false -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.port=9010 -Dcom.sun.management.jmxremote.rmi.port=9011 -Djava.rmi.server.hostname=localhost -Dcom.sun.management.jmxremote.local.only=false'

# enable tomcat remote debugging
ENV TOMCAT_REMOTE_DEBUGGING='-Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n'

ENV JAVA_OPTS="${JVM_REMOTE_JMX} ${TOMCAT_REMOTE_DEBUGGING}"

CMD ["target/bin/webapp", "--port", "80"]
