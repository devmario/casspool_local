package com.vanillabreeze;

import java.io.*;
import java.net.*;

import me.prettyprint.cassandra.service.CassandraHostConfigurator;
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

import com.vanillabreeze.CassCon;

import java.util.*;

public class App {
	public static void main(String[] args) {
		try {
			ArrayList<Cluster> list_cluster = new ArrayList<Cluster>();
			CassandraHostConfigurator conf = new CassandraHostConfigurator(args[0]);
			conf.setMaxActive(100);
			list_cluster.add(HFactory.getOrCreateCluster("DessertTown", conf));
			list_cluster.add(HFactory.getOrCreateCluster("DessertTown", conf));
	
			ArrayList<Keyspace> list_keyspace = new ArrayList<Keyspace>();
			list_keyspace.add(HFactory.createKeyspace("DessertTown", list_cluster.get(0)));
			list_keyspace.add(HFactory.createKeyspace("DessertTown", list_cluster.get(1)));

			int i = 0;
			ServerSocket listener = new ServerSocket(6666);
			Socket server;
			boolean loop = true;
			while(loop) {
				server = listener.accept();
				CassCon conn_c = new CassCon(server, list_keyspace.get(i));
				Thread t = new Thread(conn_c);
				t.start();
				i++;
				if(i >= list_keyspace.size())
					i = 0;
			}

			for(int n = 0; n < list_cluster.size(); n++)
				list_cluster.get(n).getConnectionManager().shutdown();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
