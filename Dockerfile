FROM openjdk:8-jre-alpine
ARG Version=9.7.3
ARG Extension=tgz
ARG Name=pivotal-gemfire-${Version}
ARG BinaryName=${Name}.${Extension}
# Default ports:
# RMI/JMX 1099
# REST 8080
# PULE 7070
# LOCATOR 10334
# CACHESERVER 40404
EXPOSE 8080 10334 40404 1099 7070
# Make sure to have gemfire downloaded in archive directory
COPY archive/${BinaryName} /
RUN apk add bash
RUN apk add iproute2
RUN tar -xvzf /${BinaryName}
RUN rm -f /${BinaryName}
ENV GEMFIRE_HOME /${Name}
ENV PATH $PATH:$GEMFIRE_HOME/bin
VOLUME /data
COPY wait_for.sh /
RUN chmod 777 /wait_for.sh && ls -alh /wait_for.sh
CMD ["gfsh"]