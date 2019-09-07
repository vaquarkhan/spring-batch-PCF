FROM openjdk:8-jdk-alpine
MAINTAINER vaquarkhan
VOLUME /tmp
EXPOSE 9090

ENV USER_NAME monitoring
ENV APP_HOME /home/$USER_NAME/app

RUN adduser -D -u 1000 $USER_NAME
RUN mkdir $APP_HOME

ADD ["target/vkhan-batch-processing-0.1.0.jar" ,"$APP_HOME/vkhan-batch-processing-0.1.0.jar"]
RUN chown $USER_NAME $APP_HOME/vkhan-batch-processing-0.1.0.jar

USER $USER_NAME
WORKDIR $APP_HOME
RUN touch vkhan-batch-processing-0.1.0.jar

ENTRYPOINT ["java","-jar","vkhan-batch-processing-0.1.0.jar"]