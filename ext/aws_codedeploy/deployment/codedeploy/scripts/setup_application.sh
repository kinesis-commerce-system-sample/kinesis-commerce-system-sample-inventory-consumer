#!/bin/bash

LOG_FILE=/tmp/setup_application.log
> ${LOG_FILE}

cd /var/app/deploy/deployment/ansible
/usr/local/bin/ansible-playbook deploy.yml 2>&1 | tee -a ${LOG_FILE}
RETVAL=${PIPESTATUS[0]}

exit ${RETVAL}
