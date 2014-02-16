package com.vanillabreeze;

import java.util.*;
import java.io.*;
import java.net.*;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.cassandra.service.template.ColumnFamilyTemplate;
import me.prettyprint.cassandra.service.template.ColumnFamilyResult;
import me.prettyprint.cassandra.service.template.ThriftColumnFamilyTemplate;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;

import org.json.JSONObject;
import org.json.JSONException;

class CassCon implements Runnable {
	private Socket server;
	private String line;
	private Keyspace keyspace;

	CassCon(Socket server, Keyspace keyspace) {
		this.server = server;
		this.keyspace = keyspace;
	}

	public void run () {
		try {
			//System.out.println("hi");
			DataInputStream in = new DataInputStream(server.getInputStream());
			PrintStream out = new PrintStream(server.getOutputStream());

			while((line = in.readLine()) != null && !line.equals("quit")) {
				//System.out.println(line);
				
				JSONObject jsonObj;
				String exe = "";
				try {
					jsonObj = new JSONObject(line);
					exe = (String)jsonObj.get("exe");
				} catch (JSONException e) {
					System.out.println("json err: " + e);
					e.printStackTrace();
				}
				//System.out.println(uid);
				try {
					String uid = exe;
					ColumnFamilyTemplate<String, String> template = new ThriftColumnFamilyTemplate<String, String>(keyspace, "User", StringSerializer.get(), StringSerializer.get());
					ColumnFamilyResult<String, String> res = template.queryColumns(uid);
					if (res.hasResults()) {
						String tmp = "";
						for (String name : res.getColumnNames()) {
							out.println(name + ":" + res.getString(name));
						}	
						out.println("get complete");
					} else {
						Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());

						Set<HColumn<String, String>> colums = new HashSet<HColumn<String,String>>();
						
						JSONObject tmp;
						try {
							tmp = new JSONObject("{\"id\":\"" + uid + "\",\"isguest\":\"false\",\"nickname\":\"\uc7a5\uc6d0\ud76c\",\"picurl\":\"http://th-p6.talk.kakao.co.kr/th/talkp/wkdqfgOrT5/ThdwpFelRV8JzwKACNq8ck/txh8d4_110x110_c.jpg\",\"isopenpicurl\":\"false\",\"coin\":\"500\",\"gem\":\"5\",\"lv\":\"1\",\"exp\":\"0\",\"nextexp\":\"200\",\"stam\":\"5\",\"maxstam\":\"20\",\"avail\":\"\",\"tutorial\":\"false\",\"message_blocked\":\"false\",\"login_week\":\"2014-07\",\"review\":\"0.0\"}");
							Iterator iter = tmp.keys();
							while(iter.hasNext()) {
								String key = (String)iter.next();
								colums.add(HFactory.createStringColumn(key, (String)tmp.get(key)));
							}					
						} catch (JSONException e) {
							System.out.println("json err: " + e);
							e.printStackTrace();
						}

						for (HColumn<String, String> column : colums) {
							 mutator.addInsertion(uid, "User", column);
						}
		    				mutator.execute();
						out.println("insert success");	
					}
				} catch (HectorException e) {
				   e.printStackTrace();
				}
			}

			server.close();
		} catch (IOException ioe) {
			System.out.println("IOException on socket listen: " + ioe);
			ioe.printStackTrace();
		}
	}
}
