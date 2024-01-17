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
