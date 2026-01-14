#!/bin/bash
# Скрипт для настройки Gradle Wrapper

echo "Setting up Gradle Wrapper..."

# Создаем структуру папок
mkdir -p gradle/wrapper

# Скачиваем gradle-wrapper.jar
curl -L -o gradle/wrapper/gradle-wrapper.jar \
  https://github.com/gradle/gradle/raw/v8.4.0/wrapper/gradle-wrapper.jar

# Даем права
chmod +x gradlew
