import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;


/**
 * Hbase namespace
 */
public class NameSpaceInHbase {
    public static void main(String[] args) throws IOException {

        Configuration conf = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(conf);
        //管理员对象
        Admin admin = connection.getAdmin();
        //创建命名空间
        NamespaceDescriptor namespaceDescriptor = NamespaceDescriptor.create("datastrom3").build();
        admin.createNamespace(namespaceDescriptor);
        //表名
        TableName tableName = TableName.valueOf("datastrom3","testtable3");
        //表描述
        HTableDescriptor desc = new HTableDescriptor(tableName);
        //列族描述datastrom
        HColumnDescriptor coldef = new HColumnDescriptor(Bytes.toBytes("colfam3"));
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
 ========
 nextShell:
 hbase(main):005:0> list
 TABLE
 testtable2
 user
 2 row(s) in 0.0070 seconds

java output:
 =====
 Table available: true

shell:
 =====
 hbase(main):022:0> list
 TABLE
 datastrom3:testtable3
 ...
  **/