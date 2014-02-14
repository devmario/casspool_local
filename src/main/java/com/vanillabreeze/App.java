package com.vanillabreeze;

import me.prettyprint.cassandra.serializers.StringSerializer;
import me.prettyprint.hector.api.Cluster;
import me.prettyprint.hector.api.Keyspace;
import me.prettyprint.hector.api.beans.HColumn;
import me.prettyprint.hector.api.exceptions.HectorException;
import me.prettyprint.hector.api.factory.HFactory;
import me.prettyprint.hector.api.mutation.Mutator;
import me.prettyprint.hector.api.query.ColumnQuery;
import me.prettyprint.hector.api.query.QueryResult;

public class App 
{
	public static void main( String[] args )
	{
		Cluster cluster = HFactory.getOrCreateCluster("DessertTown", "localhost:9160");
		Keyspace keyspaceOperator = HFactory.createKeyspace("DessertTown", cluster);
		try {
		   Mutator<String> mutator = HFactory.createMutator(keyspaceOperator, StringSerializer.get());
		   mutator.insert("hector", "User", HFactory.createStringColumn("java", "success"));
		    
		   ColumnQuery<String, String, String> columnQuery = HFactory.createStringColumnQuery(keyspaceOperator);
		   columnQuery.setColumnFamily("User").setKey("hector").setName("java");
		   QueryResult<HColumn<String, String>> result = columnQuery.execute();
		    
		   System.out.println("Read HColumn " + result.get());            
            
		} catch (HectorException e) {
		   e.printStackTrace();
		}
		cluster.getConnectionManager().shutdown();
		System.out.println( "Hello Maven cassandra hector!" );
    	}
}
