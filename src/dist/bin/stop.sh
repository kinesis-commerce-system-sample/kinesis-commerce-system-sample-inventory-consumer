#!/bin/bash

cd $(dirname $0)/..

readonly APP_NAME="kinesis-commerce-system-sample-inventory-consumer"

readonly BASE_DIR="$(pwd)"
readonly BIN_DIR="${BASE_DIR}/bin"
readonly CONF_DIR="${BASE_DIR}/conf"
readonly LOG_DIR="${BASE_DIR}/log"
readonly RUN_DIR="${BASE_DIR}/run"
readonly PID_FILE="${RUN_DIR}/pid"

# 起動中だった場合は停止する。
if [ -f ${PID_FILE} ]; then
    ps $(cat ${PID_FILE}) | grep "${APP_NAME}"
    if [ $? -eq 0 ]; then
        kill $(cat ${PID_FILE})
    fi
fi
