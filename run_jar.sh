#!/bin/bash

nohup java -cp target -jar target/casspool.jar $1 1> ../casspool.log 2>&1 &
if [ $? != 0 ]; then
	echo "[casspool_tool fail to run casspool server]"
	exit 1
fi
echo "[casspool_tool started casspool server]"

exit 0
