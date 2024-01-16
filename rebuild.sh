#!/bin/bash

# 默认环境为test，除非通过命令行传递了参数
ENVIRONMENT=${1:-test}

# 停止并移除现有的 Spring Boot 容器
docker stop springboot-liuqu || true
docker rm springboot-liuqu || true

# 移除现有的 Spring Boot Docker 镜像
docker rmi springboot/springboot-liuqu:v1.0.0 || true

# 创建或检查 Docker 网络
docker network inspect spring-redis-net >/dev/null 2>&1 || \
    docker network create spring-redis-net

# 检查 Redis 容器是否已经运行
if [ ! "$(docker ps -q -f name=redis-server)" ]; then
    if [ "$(docker ps -aq -f status=exited -f name=redis-server)" ]; then
        # 如果容器已停止，删除它
        docker rm redis-server
    fi
    # 运行 Redis 容器
    docker run --name redis-server -d --network spring-redis-net \
        -v "$(pwd)/docker/redis/redis.conf:/usr/local/etc/redis/redis.conf" \
        redis redis-server /usr/local/etc/redis/redis.conf

fi

# 调用 build.sh 脚本来重新构建和启动 Spring Boot 应用
./build.sh $ENVIRONMENT
