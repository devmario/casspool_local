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

import com.vanillabreeze.CassGet;
import com.vanillabreeze.CassInsert;
import com.vanillabreeze.CassGetMulti;

class CassCon implements Runnable {
	private Socket server = null;
	private String line = "";
	private Keyspace keyspace = null;

	CassCon(Socket server, Keyspace keyspace) {
		this.server = server;
		this.keyspace = keyspace;
	}

	public void run () {
		try {
			DataInputStream in = new DataInputStream(this.server.getInputStream());
			PrintStream out = new PrintStream(this.server.getOutputStream());

			while((line = in.readLine()) != null && !line.equals("quit")) {
				try {
					JSONObject input = new JSONObject(line);
					String exe = (String)input.get("exe");
					JSONObject output = null;
					if(exe.equals("get")) {
						CassGet query = new CassGet(this.keyspace, input.getJSONObject("query"));
						output = query.execute();
					} else if(exe.equals("insert")) {
						CassInsert query = new CassInsert(this.keyspace, input.getJSONObject("query"));
						output = query.execute();
					} else if(exe.equals("get_multi")) {
						CassGetMulti query = new CassGetMulti(this.keyspace, input.getJSONObject("query"));
						output = query.execute();
					}
					if(output != null)
						out.println(output.toString());
				} catch(JSONException e) {
					e.printStackTrace();
				}
			}
			this.server.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
