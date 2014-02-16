build and run localhost
```bash
./build_and_run.sh
```

run
```bash
./run_jar.sh 127.0.0.1
```

tutorial
```bash
telnet localhost 6666

#User 88695329749732113 get
{"exe":"get","query":{"where":"User","key":"88695329749732113"}}
#output: result data json

#insert
{"exe":"insert","query":{"where":"column family name","column key":"id","data":{"data_id_0":"data_value_0","data_value_1":"data_value_1"}}}
#output: inserted data json
```
