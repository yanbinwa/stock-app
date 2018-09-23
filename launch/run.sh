#!/bin/sh

SERVICE_NAME=stock-app

java -Xms512m -Xmx6g \
     -XX:+UseConcMarkSweepGC -XX:+CMSParallelRemarkEnabled \
     -XX:MaxMetaspaceSize=6g \
     -XX:+UseCMSInitiatingOccupancyOnly -XX:CMSInitiatingOccupancyFraction=70 \
     -XX:+ScavengeBeforeFullGC -XX:+CMSScavengeBeforeRemark \
     -jar target/${SERVICE_NAME}.jar
