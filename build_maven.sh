#!/bin/bash

echo "[casspool_tool compile...]"
mvn compile -DskipTests -q
if [ $? != 0 ]; then
	echo "[casspool_tool fail compile maven]"
	exit 1
fi
echo "[casspool_tool success compile maven]"

echo "[casspool_tool package...]"
mvn package -DskipTests -q
if [ $? != 0 ]; then
	echo "[casspool_tool fail package maven]"
	exit 2
fi
echo "[casspool_tool success package maven]"

exit 0

