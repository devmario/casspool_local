./build_maven.sh
PID=`ps -ef | grep casspool | grep -v grep | awk '{print $2}'`
if [ -n "$PID" ]; then
	kill $PID
fi
java -cp target -jar target/casspool.jar > log_casspool &
tail -f log_casspool
