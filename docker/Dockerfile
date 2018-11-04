FROM yanbinwang/java:8

# Set TimeZone
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# set bundle version
ENV SERVICE_NAME=stock-app

# setup workspace
RUN mkdir -p /usr/src/${SERVICE_NAME}
RUN mkdir -p /usr/src/${SERVICE_NAME}/log
WORKDIR /usr/src/${SERVICE_NAME}

# copy the project to /usr/src/..
COPY launch ./bin
COPY docker ./docker
COPY file ./file
COPY file/stockTmp ./file/stock

# Download main jar
COPY target/${SERVICE_NAME}.jar target/${SERVICE_NAME}.jar


# defaults
ENTRYPOINT ["bin/run.sh"]