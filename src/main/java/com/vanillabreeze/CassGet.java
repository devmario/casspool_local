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
import me.prettyprint.hector.api.beans.*;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.*;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;

import org.json.*;

class CassGet {
	private Keyspace keyspace;
	private JSONObject input;

	CassGet(Keyspace keyspace, JSONObject input) {
		this.keyspace = keyspace;
		this.input = input;
	}

	public JSONObject execute() {
		JSONObject output = null;
		try {
			output = new JSONObject();
			String where = (String)this.input.get("where");
			String key = (String)this.input.get("key");
			JSONArray rows = null;
			if(this.input.has("rows"))
				rows = (JSONArray)this.input.get("rows");

			try {
				ColumnFamilyTemplate<String, String> template = new ThriftColumnFamilyTemplate<String, String>(this.keyspace, where, StringSerializer.get(), StringSerializer.get());
				ColumnFamilyResult<String, String> result = null;
				if(rows == null)
					result = template.queryColumns(key);
				else {
					List<String> list_row = new ArrayList<String>();
					for(int n = 0; n < rows.length(); n++)
						list_row.add((String)rows.get(n));
					result = template.queryColumns(key, list_row);
				}

				if(result.hasResults()) {
					for(String name : result.getColumnNames()) {
						output.put(name, result.getString(name));
					}
				}
			} catch(HectorException e) {
				System.out.println(this.input.toString());
				e.printStackTrace();
			}
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return output;
	}
}
