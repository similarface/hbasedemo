import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Pair;

import java.io.IOException;

/**
 * 修改表结构
 */
public class ModifyStructOfTable {
    public static void main(String args[]) throws IOException{
        Configuration conf = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        //
        TableName tableName=TableName.valueOf("testtable2b");

        //列族
        HColumnDescriptor coldef1 = new HColumnDescriptor("colfam1");
        //表
        HTableDescriptor desc  = new HTableDescriptor(tableName).addFamily(coldef1).setValue("Description", "ModifyTableExample: Original Table");

        //Create the table with the original structure and 50 regions.
        admin.createTable(desc, Bytes.toBytes(1L),Bytes.toBytes(10000L),50);
        //表描述
        HTableDescriptor htd1 = admin.getTableDescriptor(tableName);
        //列族2
        HColumnDescriptor coldef2 = new HColumnDescriptor("colfam2");
        //
        htd1.addFamily(coldef2).setMaxFileSize(1024*1024*1024L).setValue("Description","ModifyTableExample: Modified Table");
        admin.disableTable(tableName);
        //更改表
        admin.modifyTable(tableName,htd1);

        Pair<Integer, Integer> status = new Pair<Integer, Integer>() {{
            setFirst(50);
            setSecond(50);
        }};

        for (int i = 0; status.getFirst() != 0 && i < 500; i++) {
            status = admin.getAlterStatus(desc.getTableName());
            if (status.getSecond() != 0) {
                int pending = status.getSecond() - status.getFirst();
                System.out.println(pending + " of " + status.getSecond() + " regions updated.");
                try {
                    Thread.sleep(1 * 1000l);
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }
            } else {
                System.out.println("All regions updated.");
                break; }
        }
        if (status.getFirst() != 0) {
            throw new IOException("Failed to update regions after 500 sec‐ onds.");
        }
        admin.enableTable(tableName);
        HTableDescriptor htd2 = admin.getTableDescriptor(tableName);
        System.out.println("Equals: " + htd1.equals(htd2));
        System.out.println("New schema: " + htd2);

    }
}
/**
 50 of 50 regions updated.
 2016-08-30 17:08:08,541 INFO  [main] client.HBaseAdmin: Started enable of testtable2b
 2016-08-30 17:08:10,869 INFO  [main] client.HBaseAdmin: Enabled testtable2b
 Equals: true
 New schema: 'testtable2b', {TABLE_ATT


 shell:
 =========
 hbase(main):048:0> desc 'testtable2b'
 Table testtable2b is ENABLED
 testtable2b, {TABLE_ATTRIBUTES => {MAX_FILESIZE => '1073741824', METADATA => {'Description' => 'ModifyTableExample: Modified Table'}}
 COLUMN FAMILIES DESCRIPTION
 {NAME => 'colfam1', DATA_BLOCK_ENCODING => 'NONE', BLOOMFILTER => 'ROW', REPLICATION_SCOPE => '0', VERSIONS => '1', COMPRESSION => 'NONE', MIN_VERSIONS => '0', TTL => 'FOREVER', KEEP_D
 ELETED_CELLS => 'FALSE', BLOCKSIZE => '65536', IN_MEMORY => 'false', BLOCKCACHE => 'true'}
 {NAME => 'colfam2', DATA_BLOCK_ENCODING => 'NONE', BLOOMFILTER => 'ROW', REPLICATION_SCOPE => '0', COMPRESSION => 'NONE', VERSIONS => '1', TTL => 'FOREVER', MIN_VERSIONS => '0', KEEP_D
 ELETED_CELLS => 'FALSE', BLOCKSIZE => '65536', IN_MEMORY => 'false', BLOCKCACHE => 'true'}
 2 row(s) in 0.0340 seconds

 **/