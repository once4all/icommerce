#!/bin/bash

GIT_BRANCH_LABEL=${GIT_BRANCH_LABEL:-"develop"}

# The environment variables are already set up by the Dockerfile
java -Dgit.config.active.branch=${GIT_BRANCH_LABEL} -Duser.timezone=Asia/Saigon -XX:+PrintFlagsFinal $JAVA_OPTIONS -jar ${APP_JAR_NAME}.jar