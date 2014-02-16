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

public class App {
	public static void main(String[] args) {
		try {
			Cluster cluster = HFactory.getOrCreateCluster("DessertTown", new CassandraHostConfigurator(args[0]));
			Keyspace keyspace = HFactory.createKeyspace("DessertTown", cluster);

			ServerSocket listener = new ServerSocket(6666);
			Socket server;
			boolean loop = true;
			while(loop) {
				server = listener.accept();
				CassCon conn_c = new CassCon(server, keyspace);
				Thread t = new Thread(conn_c);
				t.start();
			}

			cluster.getConnectionManager().shutdown();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
}
