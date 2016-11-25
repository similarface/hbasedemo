import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;


/**
 * Hbase 创建表
 */
public class CreateTableInHbase {
    public static void main(String[] args) throws IOException {
        Configuration conf = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(conf);
        //管理员对象
        Admin admin = connection.getAdmin();
        //表名
        TableName tableName = TableName.valueOf("testtable2");
        //表描述
        HTableDescriptor desc = new HTableDescriptor(tableName);
        //列族描述
        HColumnDescriptor coldef = new HColumnDescriptor(Bytes.toBytes("colfam1"));
        //表加入列族
        desc.addFamily(coldef);
        //创建表
        admin.createTable(desc);
        //校验表是否可用
        boolean avail = admin.isTableAvailable(tableName);
        System.out.println("Table available: "+avail);
    }
}

/**
 preHell:
 hbase(main):001:0> list
 TABLE
 user
 1 row(s) in 0.2670 seconds

 => ["user"]
====================================>
Java output:

 Table available: true
========
 nextShell:
 hbase(main):005:0> list
 TABLE
 testtable2
 user
 2 row(s) in 0.0070 seconds

 hbase(main):006:0> desc 'testtable2'
 Table testtable2 is ENABLED
 testtable2
 COLUMN FAMILIES DESCRIPTION
 {NAME => 'colfam1', DATA_BLOCK_ENCODING => 'NONE', BLOOMFILTER => 'ROW', REPLICATION_SCOPE => '0', VERSIONS => '1', COMPRESSION => 'NONE', MIN_VERSIONS => '0',
 TTL => 'FOREVER', KEEP_DELETED_CELLS => 'FALSE', BLOCKSIZE => '65536', IN_MEMORY => 'false', BLOCKCACHE => 'true
 **/