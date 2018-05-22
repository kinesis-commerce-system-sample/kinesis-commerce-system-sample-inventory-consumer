#!/bin/bash

cd $(dirname $0)/..

readonly APP_NAME="kinesis-commerce-system-sample-inventory-consumer"

readonly BASE_DIR="$(pwd)"
readonly BIN_DIR="${BASE_DIR}/bin"
readonly CONF_DIR="${BASE_DIR}/conf"
readonly LOG_DIR="${BASE_DIR}/log"
readonly RUN_DIR="${BASE_DIR}/run"
readonly PID_FILE="${RUN_DIR}/pid"

. ${CONF_DIR}/conf.sh

_error() {
    echo -e "\033[0;31mERROR\033[m : " "$1" 2>&1
}

_error_file_overwrite() {
    echo -e "ERROR : " "$1" > ${LOG_DIR}/${APP_NAME}.script.error 2>&1
}

_error_file_append() {
    echo -e "ERROR : " "$1" >> ${LOG_DIR}/${APP_NAME}.script.error 2>&1
}

error() {
    _error "$1"
    _error_file_overwrite "$1"

    _error "abort at $(date '+%Y-%m-%d %H:%M:%S')"
    _error_file_append "abort at $(date '+%Y-%m-%d %H:%M:%S')"
    exit 255
}


if [ "$(whoami)" != "${exec_user}" ]; then
    error "LINENO:${LINENO} | started by user '$(whoami)'. must be '${exec_user}'."
fi

# 既に起動中だった場合は停止する。停止が確認されるまで待機する。
if [ -f ${PID_FILE} ]; then
    ps $(cat ${PID_FILE}) | grep java | grep "${APP_NAME}"
    if [ $? -eq 0 ]; then
        kill $(cat ${PID_FILE})
        while :
        do
            sleep 1
            ps $(cat ${PID_FILE})
            if [ $? -ne 0 ]; then
                break
            fi
        done
    fi
fi

JAVA_OPTS="-server"
JAVA_OPTS="${JAVA_OPTS} -Xms${heap_initial}"
JAVA_OPTS="${JAVA_OPTS} -Xmx${heap_max}"
JAVA_OPTS="${JAVA_OPTS} -XX:OnOutOfMemoryError=\"/bin/bash ${BIN_DIR}/oome-shutdown.sh %p ${APP_NAME}\""
JAVA_OPTS="${JAVA_OPTS} -XX:+UseConcMarkSweepGC"

JAVA_OPTS="${JAVA_OPTS} -Duser.timezone=UTC"
JAVA_OPTS="${JAVA_OPTS} -Duser.language=en"

# TODO : JMX

export JAVA_HOME
export JAVA_OPTS
export SPRING_PROFILES_ACTIVE

# affect application.yml 'log.path.all.name'
export LOG_PATH_ALL_NAME="${APP_NAME}.app.log"
# affect application.yml 'log.path.error.name'
export LOG_PATH_ERROR_NAME="${APP_NAME}.error.log"

nohup /bin/bash ${BIN_DIR}/startScript.sh > /dev/null 2>&1 &
echo $! > ${PID_FILE}
