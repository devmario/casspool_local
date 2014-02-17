package com.vanillabreeze;

import java.util.*;
import java.io.*;
import java.net.*;

import me.prettyprint.cassandra.serializers.*;
import me.prettyprint.cassandra.service.template.*;
import me.prettyprint.hector.api.*;
import me.prettyprint.hector.api.beans.*;
import me.prettyprint.hector.api.exceptions.*;
import me.prettyprint.hector.api.factory.*;
import me.prettyprint.hector.api.mutation.*;
import me.prettyprint.hector.api.query.*;

import org.json.*;

class CassRemoveMulti {
	private Keyspace keyspace;
	private JSONObject input;

	CassRemoveMulti(Keyspace keyspace, JSONObject input) {
		this.keyspace = keyspace;
		this.input = input;
	}

	public JSONObject execute() {
		JSONObject output = null;
		try {
			output = new JSONObject();
			String where = (String)this.input.get("where");
			JSONArray keys = (JSONArray)this.input.get("keys"); 
			JSONArray json_rows = null;
			if(this.input.has("rows")) {
				json_rows = (JSONArray)this.input.get("rows");
			}
			boolean is_counter = false;
			if(this.input.has("is_counter"))
				is_counter = this.input.getBoolean("is_counter");
			try {
				ArrayList<String> list_key = new ArrayList<String>();
				for(int n = 0; n < keys.length(); n++)
					list_key.add((String)keys.get(n));
				String list_row[] = null;
				if(json_rows != null && json_rows.length() > 0) {
					list_row = new String[json_rows.length()];
					for(int n = 0; n < json_rows.length(); n++)
						list_row[n] = (String)json_rows.get(n);
				}
				Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
				if(list_row == null)
					mutator.addDeletion(list_key, where);
				else {
					for(String val_k : list_key) {
						for(int n = 0; n < json_rows.length(); n++)
							mutator.addDeletion(val_k, where, list_row[n], StringSerializer.get());
					}
				}
				mutator.execute();
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

