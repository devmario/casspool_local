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
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.*;
import me.prettyprint.hector.api.beans.*;
import me.prettyprint.hector.api.beans.CounterSuperSlice;

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
			JSONArray json_rows = null;
			String start = "";
			String finish = "";
			boolean reversed = false;
			int count = 1000;	
			boolean is_super = false;
			boolean is_counter = false;
			if(this.input.has("rows")) {
				json_rows = (JSONArray)this.input.get("rows");
			} else {
				if(this.input.has("start"))
					start = (String)this.input.get("start");
				if(this.input.has("finish"))
					finish = (String)this.input.get("finish");
				if(this.input.has("reversed"))
					reversed = (boolean)this.input.getBoolean("reversed");
				if(this.input.has("count"))
					count = (int)this.input.getInt("count");
			}
			if(this.input.has("is_super"))
				is_super = this.input.getBoolean("is_super");
			if(this.input.has("is_counter"))
				is_counter = this.input.getBoolean("is_counter");

			try {
				ArrayList list_key = new ArrayList<String>();
				for(int n = 0; n < keys.length(); n++)
					list_key.add((String)keys.get(n));
				String list_row[] = null;
				if(json_rows != null && json_rows.length() > 0) {
					list_row = new String[json_rows.length()];
					for(int n = 0; n < json_rows.length(); n++)
						list_row[n] = (String)json_rows.get(n);
				}
						
				if(is_counter == false && is_super == false) {
					MultigetSliceQuery<String, String, String> multigetSliceQuery = HFactory.createMultigetSliceQuery(this.keyspace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
					multigetSliceQuery.setColumnFamily(where);
					multigetSliceQuery.setKeys(list_key);

					if(json_rows == null) {
						multigetSliceQuery.setRange(start, finish, reversed, count);
					} else {
						multigetSliceQuery.setColumnNames(list_row);
					}

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
				} else if(is_counter == true && is_super == false) {
					MultigetSliceCounterQuery<String, String> query = HFactory.createMultigetSliceCounterQuery(this.keyspace, StringSerializer.get(), StringSerializer.get());
					query.setColumnFamily(where).setKeys(list_key);
					if(json_rows == null)
						query.setRange(start, finish, reversed, count);
					else
						query.setColumnNames(list_row);
					QueryResult<CounterRows<String, String>> result = query.execute();
					CounterRows<String, String> rows = result.get();
					for(CounterRow<String, String> row : rows) {
						JSONObject row_json = new JSONObject();
						CounterSlice<String> counters = row.getColumnSlice();
						for(HCounterColumn<String> counter : counters.getColumns()) {
							row_json.put(counter.getName(), counter.getValue());
						}
						output.put(row.getKey(), row_json);
					}
				} else if(is_counter == true && is_super == true) {
					MultigetSuperSliceCounterQuery<String, String, String> query = HFactory.createMultigetSuperSliceCounterQuery(this.keyspace, StringSerializer.get(), StringSerializer.get(), StringSerializer.get());
					query.setColumnFamily(where).setKeys(list_key);
					if(json_rows == null)
						query.setRange(start, finish, reversed, count);
					else
						query.setColumnNames(list_row);
					QueryResult<CounterSuperRows<String, String, String>> result = query.execute();
					CounterSuperRows<String, String, String> rows = result.get();
					for(CounterSuperRow<String, String, String> row : rows) {
						JSONObject row_json = new JSONObject();
						CounterSuperSlice<String, String> columns = row.getSuperSlice();
						List<HCounterSuperColumn<String, String>> inner_columns = columns.getSuperColumns();
						
						for(HCounterSuperColumn<String, String> inner_column : inner_columns) {
							JSONObject super_row_json = new JSONObject();
							List<HCounterColumn<String>> list_super = inner_column.getColumns();
							for(HCounterColumn<String> value_super : list_super) {
								super_row_json.put(value_super.getName(), value_super.getValue());
							}
							row_json.put(inner_column.getName(), super_row_json);
						}
						output.put(row.getKey(), row_json);
					}
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

