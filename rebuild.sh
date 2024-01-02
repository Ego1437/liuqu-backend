#!/bin/bash

# 默认环境为test，除非通过命令行传递了参数
ENVIRONMENT=${1:-test}

# Stop the existing container
docker stop springboot-liuqu

# Remove the stopped container
docker rm springboot-liuqu

# Remove the existing image
docker rmi springboot/springboot-liuqu:v1.0.0

# Call build.sh with the environment specified
./build.sh $ENVIRONMENT
