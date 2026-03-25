#!/bin/sh
# Gradle wrapper bootstrap - downloads gradle if needed
APP_HOME=$(cd "$(dirname "$0")" && pwd)
CLASSPATH="$APP_HOME/gradle/wrapper/gradle-wrapper.jar"
if [ ! -f "$CLASSPATH" ]; then
  echo "Downloading Gradle wrapper jar..."
  mkdir -p "$APP_HOME/gradle/wrapper"
  curl -sL "https://raw.githubusercontent.com/nicoulaj/gradle-wrapper/refs/heads/master/gradle/wrapper/gradle-wrapper.jar" -o "$CLASSPATH" 2>/dev/null || \
  curl -sL "https://github.com/nicoulaj/gradle-wrapper/raw/refs/heads/master/gradle/wrapper/gradle-wrapper.jar" -o "$CLASSPATH" 2>/dev/null
fi
exec java -classpath "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
