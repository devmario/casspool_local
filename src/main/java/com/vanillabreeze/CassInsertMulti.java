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

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

class CassInsertMulti {
	private Keyspace keyspace;
	private JSONObject input;

	CassInsertMulti(Keyspace keyspace, JSONObject input) {
		this.keyspace = keyspace;
		this.input = input;
	}

	public JSONObject execute() {
		JSONObject output = null;
		try {
			output = new JSONObject();
			String where = (String)this.input.get("where");
			JSONObject data = (JSONObject)this.input.get("data");

			try {
				Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
			
				Iterator it_data = data.keys();
				while(it_data.hasNext()) {
					Set<HColumn<String, String>> colums = new HashSet<HColumn<String,String>>();
					String k_data = (String)it_data.next();
					JSONObject v_data = (JSONObject)data.get(k_data);
					Iterator it_inner = v_data.keys();
					while(it_inner.hasNext()) {
						String k_inner = (String)it_inner.next();
						String v_inner = (String)v_data.get(k_inner);
						colums.add(HFactory.createStringColumn(k_inner, v_inner));
					}
					for(HColumn<String, String> column : colums) {
						mutator.addInsertion(k_data, where, column);
					}
				}
				mutator.execute();
			} catch(HectorException e) {
				e.printStackTrace();
			}
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return output;
	}
}

