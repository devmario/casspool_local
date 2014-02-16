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
import me.prettyprint.hector.api.query.MultigetSliceQuery;
import me.prettyprint.hector.api.beans.Rows;
import me.prettyprint.hector.api.beans.Row;
import me.prettyprint.hector.api.beans.ColumnSlice;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

class CassGetMulti {
	private Keyspace keyspace;
	private JSONObject input;

	CassGetMulti(Keyspace keyspace, JSONObject input) {
		this.keyspace = keyspace;
		this.input = input;
	}

	public JSONObject execute() {
		JSONObject output = null;
		try {
			output = new JSONObject();
			String where = (String)this.input.get("where");
			JSONArray keys = (JSONArray)this.input.get("keys");

			try {
				MultigetSliceQuery<String, String, String> multigetSliceQuery = HFactory.createMultigetSliceQuery(this.keyspace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
			
				ArrayList list_key = new ArrayList<String>();
				for(int n = 0; n < keys.length(); n++)
					list_key.add((String)keys.get(n));
				multigetSliceQuery.setColumnFamily(where);
				multigetSliceQuery.setKeys(list_key);
				multigetSliceQuery.setRange("", "", false, 1000);
				QueryResult<Rows<String, String, String>> result = multigetSliceQuery.execute();

				Rows<String, String, String> rows = result.get();
				for(Row<String, String, String> row : rows) {
					JSONObject row_json = new JSONObject();
					ColumnSlice<String, String> columns = row.getColumnSlice();
					for(HColumn<String, String> column : columns.getColumns()) {
						row_json.put(column.getName(), column.getValue());
					}
					output.put(row.getKey(), row_json);
				}
				
			} catch(HectorException e) {
				e.printStackTrace();
			}
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return output;
	}
}

