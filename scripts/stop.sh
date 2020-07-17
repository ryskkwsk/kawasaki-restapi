##!/usr/bin/env bash

PID=$(ps -ax | grep kawasaki-restapi.jar | grep -v grep | awk '{print $1}')
if [ -n "${PID}" ]; then
  sudo kill -9 ${PID}
fi
