#!/bin/bash

mvn compile
if [ $? != 0 ]; then
	echo "[casspool_tool fail compile maven]"
	exit 1
fi
echo "[casspool_tool success compile maven]"

mvn package
if [ $? != 0 ]; then
	echo "[casspool_tool fail package maven]"
	exit 2
fi
echo "[casspool_tool success package maven]"

exit 0

