#!/bin/bash

./build_maven.sh
if [ $? != 0 ]; then
	echo "[casspool_tool error build maven]"
	exit 1;
fi
echo "[casspool_tool success build maven]"

PID=`ps -ef | grep casspool | grep -v grep | awk '{print $2}'`
if [ -n "$PID" ]; then
	kill $PID
	echo "[casspool_tool kill casspool process]"
fi

./run_jar.sh 127.0.0.1

if [ $? != 0 ]; then
	exit 1
fi

exit 0
