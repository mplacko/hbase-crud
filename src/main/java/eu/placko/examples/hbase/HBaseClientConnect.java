package eu.placko.examples.hbase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.MasterNotRunningException;
import org.apache.hadoop.hbase.client.*;

public class HBaseClientConnect {
	public static void main(String[] args) throws IOException {
        new HBaseClientConnect().connect();
    }
	
	private void connect() throws IOException {
        Configuration config = HBaseConfiguration.create();

        try {
            HBaseAdmin.available(config);
            System.out.println("\n*** HBase is running. ***");
        } catch (MasterNotRunningException ex) {
            System.out.println("\n*** HBase is not running. ***" + ex.getMessage());
            return;
        }

        HBaseClientOperations HBaseClientOperations = new HBaseClientOperations();
        HBaseClientOperations.run(config);
    }
}