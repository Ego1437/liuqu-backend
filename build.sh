#!/bin/bash

# 默认环境为test，除非通过命令行传递了参数
ENVIRONMENT=${1:-test}

echo "Starting build process for $ENVIRONMENT environment..."

# build jar file with environment-specific properties
if [ "$ENVIRONMENT" == "prod" ]; then
  ./gradlew buildTodoCoderJar -Pprod
  LIQUIBASE_DEFAULTS_FILE="/liquibase/changelog/liquibase.docker.prod.properties"
  echo "Environment switched to production successfully."
elif [ "$ENVIRONMENT" == "test" ]; then
  ./gradlew buildTodoCoderJar -Ptest
  LIQUIBASE_DEFAULTS_FILE="/liquibase/changelog/liquibase.docker.test.properties"
  echo "Environment switched to test successfully."
else
  echo "Invalid environment specified: $ENVIRONMENT"
  echo "Usage: $0 [prod|test]"
  exit 1
fi
echo "Creating Docker network..."
docker network create spring-redis-net

# 启动 Redis 容器
echo "Starting Redis container..."
docker run --name redis-server -d --network spring-redis-net redis

# 构建和运行 Spring Boot 容器
echo "Building and starting Spring Boot container..."
docker build -t springboot/springboot-liuqu:v1.0.0 -f ./docker/springboot/Dockerfile .
docker run --name=springboot-liuqu -d --network=spring-redis-net -p 8083:8080 -e SPRING_REDIS_HOST=redis-server -e SPRING_REDIS_PORT=6379 springboot/springboot-liuqu:v1.0.0

# build docker image
#docker build -t springboot/springboot-liuqu:v1.0.0 -f ./docker/springboot/Dockerfile .

# Run the container by image
#docker run --name=springboot-liuqu -d --add-host host.docker.internal:host-gateway -p 8083:8080 springboot/springboot-liuqu:v1.0.0
#docker run --name=springboot-liuqu -d --add-host host.docker.internal:host-gateway -p 8083:8080 -e SPRING_REDIS_HOST=clustercfg.liuqu.kyx9nh.apse2.cache.amazonaws.com -e SPRING_REDIS_PORT=6379 springboot/springboot-liuqu:v1.0.0

# Run Liquibase migrations with the correct properties file
docker run --rm -it -v "$(pwd)/src/main/resources/db/changelog:/liquibase/changelog" liquibase/liquibase-mysql update --defaultsFile=$LIQUIBASE_DEFAULTS_FILE
