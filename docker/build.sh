#!/bin/bash

DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
BUILDROOT=$DIR/../
REPO=yanbinwang
cd ${BUILDROOT}

# 从pom.xml的finalName获取项目名称，失败则从mvn clean的打印信息中抽取
getName(){
    [ -f pom.xml ] && {
       CONTAINER=`awk '/<finalName>[^<]+<\/finalName>/ { \
            gsub(/<finalName>|<\/finalName>/,"",$1); \
            print $1; \
            exit;}' pom.xml`
    }
    [ -z $CONTAINER ] && {
       CONTAINER=`mvn clean -DskipTests|awk '$2=="Building" {print $3;exit;}'`
    }
}


CONTAINER="" && getName
TAG=$(git rev-parse --short HEAD)
DOCKER_IMAGE=$REPO/$CONTAINER:$TAG

echo $DOCKER_IMAGE

cmd="docker build \
        --no-cache -t $DOCKER_IMAGE \
        -f $DIR/Dockerfile \
        $BUILDROOT"
echo $cmd
eval $cmd
