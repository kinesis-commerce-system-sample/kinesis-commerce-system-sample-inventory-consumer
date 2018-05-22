#!/bin/bash

LOG_FILE=/tmp/before_install.log
> ${LOG_FILE}

# check presense
echo "yum list installed python27-pip" | tee -a ${LOG_FILE}
yum list installed python27-pip 2>&1 | tee -a ${LOG_FILE}

if [ "${PIPESTATUS[0]}" -ne 0 ]; then
    echo "yum install -y python27-pip" | tee -a ${LOG_FILE}
    yum install -y python27-pip 2>&1 | tee -a ${LOG_FILE}
fi

# install ansible
echo "pip install ansible" | tee -a ${LOG_FILE}
pip install ansible 2>&1 | tee -a ${LOG_FILE}

if [ "${PIPESTATUS[0]}" -ne 0 ]; then
    exit ${PIPESTATUS[0]}
fi


exit 0
