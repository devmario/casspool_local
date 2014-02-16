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
#output: {}

#get multi
{"exe":"get_multi","query":{"where":"User","keys":["88695329749732113", "88335334833984240"]}}
#output: {"88695329749732113":{data}, "88335334833984240": {data}}


#get multi with row key
{"exe":"get_multi","query":{"where":"User","keys":["88695329749732113", "88335334833984240"], "rows":["id","exp"]}}
#output: {"88335334833984240":{"id":"88335334833984240","exp":"1500"},"88695329749732113":{"id":"88695329749732113","exp":"0"}}

#get multi with row index
{"exe":"get_multi","query":{"where":"User","keys":["88695329749732113", "88335334833984240"], "start":"a", "finish":"i"}}
#output:{"88335334833984240":{"exp":"1500","coin":"8354","avail":"Cookie,Candy,Gelato","gem":"5793"},"88695329749732113":{"exp":"0","coin":"500","avail":"","gem":"5"}}

#get multi with row index and count
{"exe":"get_multi","query":{"where":"User","keys":["88695329749732113", "88335334833984240"], "start":"a", "count":6}}
#output: {"88335334833984240":{"id":"88335334833984240","exp":"1500","coin":"8354","avail":"Cookie,Candy,Gelato","gem":"5793","isguest":""},"88695329749732113":{"id":"88695329749732113","exp":"0","coin":"500","avail":"","gem":"5","isguest":""}}

#get multi with row index and count and reverse
{"exe":"get_multi","query":{"where":"User","keys":["88695329749732113", "88335334833984240"], "start":"g", "reversed": true, "count":2}}
#output:{"88335334833984240":{"exp":"1500","coin":"8354"},"88695329749732113":{"exp":"0","coin":"500"}}
```
