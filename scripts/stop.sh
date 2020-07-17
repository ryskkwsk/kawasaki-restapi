##!/usr/bin/env bash

PID=$(ps -ax | grep kawasaki-restapi.jar | grep -v grep | awk '{print $1}')
kill ${PID}