# 留趣APP

### 环境配置

- Java 17
- Docker

### 项目部署

- 配置Java 17 + gradle，可运行build.gradle文件
- 运行`./build.sh`

##### build.sh介绍

部署脚本，它包括了构建 Java 应用程序的 JAR 文件、构建 Docker 镜像、运行 Docker 容器以及运行 Liquibase 数据库迁移的步骤。这里是脚本的每个部分的解释：

1. `./gradlew buildTodoCoderJar`: 使用 Gradle 构建工具运行一个自定义任务 `buildTodoCoderJar`，这个任务可能会编译 Java 代码，运行测试，并且将生成的 JAR 文件拷贝到指定位置。

2. `docker build -t springboot/springboot-liuqu:v1.0.0 -f ./docker/springboot/Dockerfile .`: 构建一个 Docker 镜像，标签为 `springboot/springboot-liuqu:v1.0.0`，使用位于 `./docker/springboot/Dockerfile` 路径下的 Dockerfile。

3. `docker run --name=springboot-liuqu -d --restart=always --add-host host.docker.internal:host-gateway -p 8083:8080 springboot/springboot-liuqu:v1.0.0`: 运行一个 Docker 容器，容器名为 `springboot-liuqu`，以守护进程模式运行，并设置为始终重启。将容器的 8080 端口映射到宿主机的 8083 端口，并添加 `host.docker.internal` 到宿主的网关。

4. `docker build -t liquibase/liquibase-mysql ./docker/liquibase`: 构建 Liquibase 用于 MySQL 的 Docker 镜像。

5. `docker run --rm -it -v "$(pwd)/src/main/resources/db/changelog:/liquibase/changelog" liquibase/liquibase-mysql update --defaultsFile=/liquibase/changelog/liquibase.docker.properties`: 运行一个 Docker 容器，执行 Liquibase 更新操作。使用当前目录下的 `src/main/resources/db/changelog` 目录作为映射到容器内的卷，并指定 Liquibase 配置文件。

确保您执行这个脚本时，当前目录是项目的根目录，所有路径和文件都存在并可访问。在运行脚本之前，您可能需要给它执行权限，可以使用 `chmod +x your-script.sh` 命令来实现。

如果脚本按预期工作，它将自动完成应用程序的构建和部署。如果您在执行过程中遇到问题，请确保 Docker 已正确安装，并且您有足够的权限来执行所有列出的命令。

### GitHub Actions CI/CD 自动化构建部署指南

`.github/workflows/build-and-deploy.yml` 定义了一个构建 jar 包和 Docker 镜像、推送到 GitHub Packages 和 GitHub Registry、部署 Docker 镜像到 AWS EC2 的 GitHub Action。

注意：这个 Action 仅仅部署了 SpringBoot 后端，没有部署 MySQL 和 liquibase。

#### 前提条件

1. AWS EC2 上必须装有 docker，即可以运行 `sudo docker` 命令。
2. AWS EC2 上必须装有 MySQL 或可以连接到其他远程 MySQL，否则会导致 SpringBoot 无法连接到 MySQL，但不会导致 Docker 容器启动失败。
3. MySQL 必须已经由 liquibase 初始化完成并配置完毕。
4. AWS EC2 必须对 0.0.0.0/0 开启 22 端口，否则会导致部署失败。
5. 已经生成了可以访问 AWS EC2 的私钥并下载到本地。
6. 已经生成了 Personal access token （详见此文：[Working with the Container registry](https://docs.github.com/en/packages/working-with-a-github-packages-registry/working-with-the-container-registry)）

#### 使用方法

1. 配置好 `application.yml`。
2. 在 GitHub 仓库中，新建以下几个 secret:
   1. `SSH_PRIVATE_KEY`: 用于访问 AWS EC2 的私钥，目前由于未知原因使用该 secret 会导致密钥验证失败，因而该 secret 暂时没有作用。
   2. `SSH_HOST`: AWS EC2 的主机名称，即连接该 EC2 的命令中 “@” 之后的部分。
   3. `USERNAME`: AWS EC2 的用户名称。一般都为 `ec2-user`。
3. 将 `build-and-deploy.yml` 中的以下字段替换为 AWS EC2 私钥：

```
-----BEGIN RSA PRIVATE KEY-----
!!! REPLACE THIS WITH YOUR PRIVATE KEY !!!
-----END RSA PRIVATE KEY-----
```
4. 进入仓库下的 Actions 页面并手动触发该 Action。
5. 任务成功执行完毕后，可以在 AWS EC2 上看到正在运行的 Docker 容器。
6. 任务成功执行完毕后，可以在 GitHub Packages 上看到构建生成的 jar 包和 Docker 镜像。
7. 如需更改生成的 Docker 镜像名称，可更改 68、69 两行。

#### 已知问题

1. 使用 `${SSH_PRIVATE_KEY}` 环境变量引用 secret 中存储的私钥并验证会导致验证失败而无法登录 EC2。
2. 运行 Gradle 任务时存在性能问题，`task:compileJava` 用时约两分钟左右，整个 Action 用时约 3 分钟，推测为依赖同步问题导致。
3. `application.properties` 和 `application.test.properties` 无法达到配置 SpringBoot 的目的。如本地无其他配置文件，请使用 `application.yml`。
4. 使用此 GitHub Actions 时，必须保证 main 分支也有对应的 yml 文件，否则无法运行。
