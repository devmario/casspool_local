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
			DataInputStream in = new DataInputStream(server.getInputStream());
			PrintStream out = new PrintStream(server.getOutputStream());

			while((line = in.readLine()) != null && !line.equals("quit")) {
				try {
					JSONObject input = new JSONObject(line);
					String exe = (String)input.get("exe");
					if(exe.equals("get")) {
						CassGet query = new CassGet(this.keyspace, input.getJSONObject("query"));
						JSONObject output = query.execute();
						out.println(output.toString());
					}
				} catch(JSONException e) {
					e.printStackTrace();
				}
			}
			server.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
