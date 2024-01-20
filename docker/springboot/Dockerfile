# jre 17 的镜像
FROM todocoder/jre:17
MAINTAINER todocoder
WORKDIR /todocoder

# jvm启动参数
ENV APP_ARGS="-XX:+UseG1GC -Xms1024m -Xmx1024m -Xss256k -XX:MetaspaceSize=128m"
ADD docker/springboot/app/*.jar /todocoder/app.jar

ADD src/main/resources/publicKey.pem /todocoder/publicKey.pem
ADD src/main/resources/privateKey.pem /todocoder/privateKey.pem

# 镜像启动后运行的脚本
#ENTRYPOINT ["java","-jar", "-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5005", "/todocoder/app.jar","${APP_ARGS}","--spring.profiles.active=dev","-c"]
#ENTRYPOINT ["java","-jar", "/todocoder/app.jar","${APP_ARGS}","--spring.profiles.active=dev","-c"]
ENTRYPOINT ["java", "-Duser.timezone=Australia/Sydney", "-jar", "/todocoder/app.jar", "${APP_ARGS}", "--spring.profiles.active=dev", "-c"]

