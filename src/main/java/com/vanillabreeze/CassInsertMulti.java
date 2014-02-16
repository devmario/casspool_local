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
			boolean is_super = false;
			boolean is_counter = false;
			if(this.input.has("is_counter"))
				is_counter = this.input.getBoolean("is_counter");

			try {
				Mutator<String> mutator = HFactory.createMutator(keyspace, StringSerializer.get());
				Iterator it_data = data.keys();
				while(it_data.hasNext()) {
					if(is_super == false && is_counter == false) {
						Set<HColumn<String, String>> colums = new HashSet<HColumn<String,String>>();
						String k_data = (String)it_data.next();
						JSONObject v_data = (JSONObject)data.get(k_data);
						Iterator it_inner = v_data.keys();
						while(it_inner.hasNext()) {
							String k_inner = (String)it_inner.next();
							String v_inner = v_data.get(k_inner).toString();
							colums.add(HFactory.createStringColumn(k_inner, v_inner));
						}
						for(HColumn<String, String> column : colums) {
							mutator.addInsertion(k_data, where, column);
						}
					} else if(is_super == false && is_counter == true) {
						Set<HCounterColumn<String>> set = new HashSet<HCounterColumn<String>>();
						String k_data = (String)it_data.next();
						JSONObject v_data = (JSONObject)data.get(k_data);
						Iterator it_inner = v_data.keys();
						while(it_inner.hasNext()) {
							String k_inner = (String)it_inner.next();
							long v_inner = v_data.getLong(k_inner);
							set.add(HFactory.createCounterColumn(k_inner, v_inner)); 
						}
						for(HCounterColumn<String> val : set) {
							mutator.addCounter(k_data, where, val);
						}
					} else if(is_super == true && is_counter == true) {

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

