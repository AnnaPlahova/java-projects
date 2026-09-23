#!/bin/sh
set -e

cd "$(dirname "$0")"

chmod +x ./gradlew

JAR="gradle/wrapper/gradle-wrapper.jar"
URL="https://raw.githubusercontent.com/gradle/gradle/v8.14.0/gradle/wrapper/gradle-wrapper.jar"
MIN_SIZE=1000

need_download=0
if [ ! -f "$JAR" ]; then
  need_download=1
else
  size=$(wc -c < "$JAR")
  if [ "$size" -lt "$MIN_SIZE" ]; then
    need_download=1
  fi
fi

if [ "$need_download" -eq 1 ]; then
  echo "Downloading gradle-wrapper.jar..."
  if command -v curl >/dev/null 2>&1; then
    curl -fsSL -o "$JAR" "$URL"
  elif command -v wget >/dev/null 2>&1; then
    wget -q -O "$JAR" "$URL"
  else
    echo "Need curl or wget to download gradle-wrapper.jar" >&2
    exit 1
  fi
fi
