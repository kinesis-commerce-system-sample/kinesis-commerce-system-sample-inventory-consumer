#!/bin/bash

LOG_FILE=/tmp/restart_application.log
> ${LOG_FILE}

/opt/kinesis-commerce-system-sample-inventory-consumer/dist/current/bin/restart.sh 2>&1 | tee -a ${LOG_FILE}
RETVAL=${PIPESTATUS[0]}

exit ${RETVAL}

