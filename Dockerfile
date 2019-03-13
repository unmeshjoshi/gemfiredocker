FROM openjdk:8-jre-alpine
ARG Version=9.1.0
# Default ports:
# RMI/JMX 1099
# REST 8080
# PULE 7070
# LOCATOR 10334
# CACHESERVER 40404
EXPOSE 8080 10334 40404 1099 7070
# Make sure to have gemfire downloaded in archive directory
COPY archive/pivotal-gemfire-${Version}.tar.gz /
RUN apk add bash
RUN apk add iproute2
RUN tar -xvzf /pivotal-gemfire-${Version}.tar.gz
RUN rm -f /pivotal-gemfire-${Version}.tar.gz
COPY provisioning/* /pivotal-gemfire-${Version}/config/
ENV GEMFIRE_HOME /pivotal-gemfire-${Version}
ENV PATH $PATH:$GEMFIRE_HOME/bin
VOLUME /data
CMD ["gfsh"]