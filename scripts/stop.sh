#!/bin/sh

PID=$(ps -ax | grep kawasaki-restapi-0.0.1-SNAPSHOT.jar | grep -v grep | awk '{print $1}')
if [ -n "${PID}" ]; then
  sudo kill -9 ${PID}
fi
