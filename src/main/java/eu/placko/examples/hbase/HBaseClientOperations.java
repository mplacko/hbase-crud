package eu.placko.examples.hbase;

import java.io.IOException;
import java.util.Map;
import java.util.NavigableMap;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

public class HBaseClientOperations {
	//         cf:cq1      cf:cq2         cf:cq3   cf:cq4      cf:cq5  cf:cq6
	//row_key  category    product        size_eu  color       sex     price_eur
	//1        cell:shoes  cell:productA  cell:42  cell:black  cell:m  cell:44.50
	//2        cell:shoes  cell:productA  cell:42  cell:white  cell:m  cell:40.50
	//1        cell:shoes  cell:productA  cell:42  cell:black  cell:m  cell:42.50
	
	private static final TableName tb = TableName.valueOf("shop");
    private static final byte[] cf = Bytes.toBytes("shop");
    private static final byte[] rk1 = Bytes.toBytes("1");
    private static final byte[] rk2 = Bytes.toBytes("2");
    private static final byte[] cq1 = Bytes.toBytes("category");
    private static final byte[] cq2 = Bytes.toBytes("product");
    private static final byte[] cq3 = Bytes.toBytes("size_eu");
    private static final byte[] cq4 = Bytes.toBytes("color");
    private static final byte[] cq5 = Bytes.toBytes("sex");
    private static final byte[] cq6 = Bytes.toBytes("price_eu");
	
	public void run(final Configuration config) throws IOException {
        try (final Connection connection = ConnectionFactory.createConnection(config)) {
            final Admin admin = connection.getAdmin();
            deleteTable(admin);
            createTable(admin);
            
            final Table table = connection.getTable(tb);
            put(table);
            get(table);
            update(table);
            delete(admin);
            
            connection.close();
        }
    }
	
	public static void deleteTable(final Admin admin) throws IOException {
        if (admin.tableExists(tb)) {
            admin.disableTable(tb);
            admin.deleteTable(tb);
        }
    }
	
	public static void createTable(final Admin admin) throws IOException {
        if(!admin.tableExists(tb)) {
            TableDescriptor desc = TableDescriptorBuilder.newBuilder(tb)
                    .setColumnFamily(ColumnFamilyDescriptorBuilder.of(cf))
                    .build();
            admin.createTable(desc);
        }
    }
	
	public static void put(final Table table) throws IOException {
		System.out.println("\n*** Create/Insert - BEGIN ***");
		
		table.put(new Put(rk1).addColumn(cf, cq1, Bytes.toBytes("shoes")));
		table.put(new Put(rk1).addColumn(cf, cq2, Bytes.toBytes("productA")));
		table.put(new Put(rk1).addColumn(cf, cq3, Bytes.toBytes("42")));
		table.put(new Put(rk1).addColumn(cf, cq4, Bytes.toBytes("black")));
		table.put(new Put(rk1).addColumn(cf, cq5, Bytes.toBytes("m")));
		table.put(new Put(rk1).addColumn(cf, cq6, Bytes.toBytes("44.50")));
		
		table.put(new Put(rk2).addColumn(cf, cq1, Bytes.toBytes("shoes")));
		table.put(new Put(rk2).addColumn(cf, cq2, Bytes.toBytes("productA")));
		table.put(new Put(rk2).addColumn(cf, cq3, Bytes.toBytes("42")));
		table.put(new Put(rk2).addColumn(cf, cq4, Bytes.toBytes("white")));
		table.put(new Put(rk2).addColumn(cf, cq5, Bytes.toBytes("m")));
		table.put(new Put(rk2).addColumn(cf, cq6, Bytes.toBytes("40.50")));
		
		System.out.println("OK");
		
		System.out.println("*** Create/Insert - END ***");
    }
	
	public static void get(final Table table) throws IOException {
        System.out.println("\n*** Read/Select - BEGIN ***");

        //System.out.println(table.get(new Get(Bytes.toBytes("1"))));
        //System.out.println(table.get(new Get(Bytes.toBytes("2"))));
        
        for (int i = 1; i < 3; i++) {
        	Get get = new Get(Bytes.toBytes(Integer.toString(i)));
        	Result result = table.get(get);
        	String row = Bytes.toString(result.getRow());
        	//String specificValue = Bytes.toString(result.getValue(Bytes.toBytes(Bytes.toString(cf)), Bytes.toBytes(Bytes.toString(cq1))));
        	//System.out.println("latest cell value in shoes:category for row 1 is: " + specificValue);
        
        	// Traverse entire returned rows: 1 and 2
        	System.out.println(row);
        	NavigableMap<byte[], NavigableMap<byte[],NavigableMap<Long,byte[]>>> map = result.getMap();
        	for (Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> navigableMapEntry : map.entrySet()) {
        		String family = Bytes.toString(navigableMapEntry.getKey());
        		System.out.println("\t" + family);
        		NavigableMap<byte[], NavigableMap<Long, byte[]>> familyContents = navigableMapEntry.getValue();
        		for (Map.Entry<byte[], NavigableMap<Long, byte[]>> mapEntry : familyContents.entrySet()) {
        			String qualifier = Bytes.toString(mapEntry.getKey());
        			System.out.println("\t\t" + qualifier);
        			NavigableMap<Long, byte[]> qualifierContents = mapEntry.getValue();
        			for (Map.Entry<Long, byte[]> entry : qualifierContents.entrySet()) {
        				Long timestamp = entry.getKey();
        				String value = Bytes.toString(entry.getValue());
        				System.out.printf("\t\t\t%s, %d\n", value, timestamp);
        			}
        		}
        	}
        }
        
        System.out.println("*** Read/Select - End ***");
    }
	
	public static void update(final Table table) throws IOException {
        System.out.println("\n*** Update - BEGIN ***");

        table.put(new Put(rk1).addColumn(cf, cq1, Bytes.toBytes("shoes")));
		table.put(new Put(rk1).addColumn(cf, cq2, Bytes.toBytes("productA")));
		table.put(new Put(rk1).addColumn(cf, cq3, Bytes.toBytes("42")));
		table.put(new Put(rk1).addColumn(cf, cq4, Bytes.toBytes("black")));
		table.put(new Put(rk1).addColumn(cf, cq5, Bytes.toBytes("m")));
		table.put(new Put(rk1).addColumn(cf, cq6, Bytes.toBytes("42.50")));
		
		System.out.println("OK");
		get(table);
		
        System.out.println("*** Update - End ***");
    }
	
	public static void delete(final Admin admin) throws IOException {
        System.out.println("\n*** Delete - BEGIN ***");

        deleteTable(admin);
        System.out.println("OK");
        
        System.out.println("*** Delete - End ***");
    }
}