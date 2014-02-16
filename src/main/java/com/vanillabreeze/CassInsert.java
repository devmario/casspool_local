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

class CassInsert {
	private Keyspace keyspace;
	private JSONObject input;

	CassInsert(Keyspace keyspace, JSONObject input) {
		this.keyspace = keyspace;
		this.input = input;
	}

	public JSONObject execute() {
		JSONObject output = null;
		try {
			output = new JSONObject();
			String where = (String)this.input.get("where");
			String key = (String)this.input.get("key");

			try {
				Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
				Set<HColumn<String, String>> colums = new HashSet<HColumn<String,String>>();
				
				JSONObject data = (JSONObject)this.input.get("data");
				Iterator iter = data.keys();
				while(iter.hasNext()) {
					String row_key = (String)iter.next();
					colums.add(HFactory.createStringColumn(row_key, (String)data.get(row_key)));
				}
				
				for(HColumn<String, String> column : colums) {
					mutator.addInsertion(key, where, column);
				}
				mutator.execute();
				output = data;
			} catch(HectorException e) {
				e.printStackTrace();
			}
		} catch(JSONException e) {
			e.printStackTrace();
		}
		return output;
	}
}

