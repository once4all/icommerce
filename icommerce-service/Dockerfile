#-----------------------------------------------------------------------------------------------------
# DicomMetadata Service
#-----------------------------------------------------------------------------------------------------
# https://medium.com/@hudsonmendes/docker-spring-boot-choosing-the-base-image-for-java-8-9-microservices-on-linux-and-windows-c459ec0c238
FROM openjdk:8-jre-alpine

# Keep consistent with pom.xml
ENV APP_JAR_NAME icommerce-service

# Install curl
RUN apk --update add curl bash && \
    rm -rf /var/cache/apk/*

RUN mkdir /app

ADD ./target/${APP_JAR_NAME}.jar /app/
ADD ./src/main/resources/run.sh /app/
RUN chmod +x /app/run.sh

WORKDIR /app

# Start application
EXPOSE 8089
EXPOSE 5432
ENTRYPOINT ["/bin/bash","-c"]
CMD ["/app/run.sh"]
