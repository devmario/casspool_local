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

#get
{"exe":"get","query":{"where":"User","key":"88695329749732113"}}
#output: {"login_week":"2014-07","nickname":"QPXXPqfEKt8quBfwsRyZLA==","message_blocked":"","coin":"500","tutorial":"","picurl":"HalLVL+k/1mkbW4N22ddJNH4Evq1/QPcaj8LvZBbQdctQUJyOMEZ8TISD9HzadDWZezkH+H4CG9xUluWxMHIqmxULw/pJL7Sx1URB0hxdW1XfcRbKhhCp+BzmY2VsLHy","lv":"1","avail":"","isguest":"","id":"88695329749732113","exp":"0","isopenpicurl":"","stam":"5","nextexp":"200","maxstam":"20","unregist":"1","gem":"5","isquest":"","review":"0.0"}

#get with row id
{"exe":"get","query":{"where":"User","key":"88695329749732113","rows":["id"]}}
#output: {"id":"88695329749732113"}

#get multi
{"exe":"get_multi","query":{"where":"User","keys":["88695329749732113", "88335334833984240"]}}
#output: {"88335334833984240":{"login_week":"2014-01","nickname":"MPlZxEZJSkr064Y1PfxU5g==","coin":"8354","tutorial":"1","picurl":"V+9uPD8aPQZdD+OfI9sAzXd2BvhbOz1E+pe7pRlAs/v4LoLAy1QX52hdahCUnKyHsn6UfH35LZmeKHNx1dnPXA3a2CMl31JuUTKXZrsF1z9pwUsQCNyuv0N/ETcDNkyY","lv":"10","avail":"Cookie,Candy,Gelato","isguest":"","id":"88335334833984240","exp":"1500","isopenpicurl":"1","stam":"12","maxstam":"50","nextexp":"4000","gem":"5793","isquest":""},"88695329749732113":{"login_week":"2014-07","nickname":"QPXXPqfEKt8quBfwsRyZLA==","message_blocked":"","coin":"500","tutorial":"","picurl":"HalLVL+k/1mkbW4N22ddJNH4Evq1/QPcaj8LvZBbQdctQUJyOMEZ8TISD9HzadDWZezkH+H4CG9xUluWxMHIqmxULw/pJL7Sx1URB0hxdW1XfcRbKhhCp+BzmY2VsLHy","lv":"1","avail":"","isguest":"","id":"88695329749732113","exp":"0","isopenpicurl":"","stam":"5","nextexp":"200","maxstam":"20","unregist":"1","gem":"5","isquest":"","review":"0.0"}}

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

#insert multi
{"exe":"insert_multi","query":{"where":"User", "data":{"insert_user_1_id":{"nickname":"guest1"}, "insert_user_2_id":{"nickname":"guest2"}}}}
#output: {}

#counter get
{"exe":"get_multi","query":{"where":"SA_Tool","keys":["1392008425.admin", "1388136414.admin"], "rows":["Base_010_Vanilla"], "is_counter":true}}
#output:{"1388136414.admin":{"Base_010_Vanilla":18},"1392008425.admin":{"Base_010_Vanilla":1}}

#get super column counter
{"exe":"get_multi","query":{"where":"WeeklyDessertCounter","keys":["1388725994.88327908239392065"], "rows":["2014-01"], "is_counter":true, "is_super":true}}
#output: {"1388725994.88327908239392065":{"2014-01":{"total":3,"fun":1,"beauty":2}}}

#counter modify
{"exe":"insert_multi","query":{"where":"SA_Tool", "data":{"1392008425.admin":{"010_Face_Defeyeall":2}}, "is_counter":true}}
#output:{}

#super counter modify
{"exe":"insert_multi","query":{"where":"WeeklyDessertCounter", "data":{"1388725994.88327908239392065":{"2014-01":{"fun":2}}}, "is_counter":true, "is_super":true}}
#output:{}

```
