#!/bin/bash

jvm_pid="$1"
app_name="$2"

kill -9 "${jvm_pid}"

echo "${app_name} : oome shutdown (${jvm_pid})"
