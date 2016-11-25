/**
 * Created by similarface on 16/8/16.
 */

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.io.compress.Compression.Algorithm;

public class example {
    private static final String TABLE_NAME = "MY_TABLE_NAME_TOO";
    private static final String CF_DEFAULT = "DEFAULT_COLUMN_FAMILY";

    public static void createOrOverwrite(Admin admin, HTableDescriptor table) throws IOException {
        if (admin.tableExists(table.getTableName())) {
            admin.disableTable(table.getTableName());
            admin.deleteTable(table.getTableName());
        }
        admin.createTable(table);
    }

    public static void createSchemaTables(Configuration config) throws IOException {
        //
        try (Connection connection = ConnectionFactory.createConnection(config);
             Admin admin = connection.getAdmin()) {
            //
            HTableDescriptor table = new HTableDescriptor(TableName.valueOf(TABLE_NAME));
            //添加列族DEFAULT_COLUMN_FAMILY 加入了SNAPPY算法
            table.addFamily(new HColumnDescriptor(CF_DEFAULT).setCompressionType(Algorithm.GZ));
            System.out.print("Creating table. ");
            //创建表
            createOrOverwrite(admin, table);
            System.out.println(" Done.");
        }
    }
    public static void modifySchema (Configuration config) throws IOException {
        //获取连接
        try (Connection connection = ConnectionFactory.createConnection(config);
             Admin admin = connection.getAdmin()) {
            //
            TableName tableName = TableName.valueOf(TABLE_NAME);
            //
            if (admin.tableExists(tableName)) {
                System.out.println("Table does not exist.");
                System.exit(-1);
            }

            HTableDescriptor table = new HTableDescriptor(tableName);

            // Update existing table
            HColumnDescriptor newColumn = new HColumnDescriptor("NEWCF");
            //新行的压缩算法是gz压缩
            newColumn.setCompactionCompressionType(Algorithm.GZ);
            //保存的副本
            newColumn.setMaxVersions(HConstants.ALL_VERSIONS);
            //添加列
            admin.addColumn(tableName, newColumn);

            // Update existing column family
            HColumnDescriptor existingColumn = new HColumnDescriptor(CF_DEFAULT);
            existingColumn.setCompactionCompressionType(Algorithm.GZ);
            existingColumn.setMaxVersions(HConstants.ALL_VERSIONS);
            table.modifyFamily(existingColumn);
            admin.modifyTable(tableName, table);

            // Disable an existing table
            admin.disableTable(tableName);

            // Delete an existing column family
            admin.deleteColumn(tableName, CF_DEFAULT.getBytes("UTF-8"));

            // Delete a table (Need to be disabled first)
            admin.deleteTable(tableName);
        }
    }
    public static void main(String... args) throws IOException {
        Configuration config = HBaseConfiguration.create();

        //Add any necessary configuration files (hbase-site.xml, core-site.xml)
        //config.addResource(new Path(System.getenv("HBASE_CONF_DIR"), "hbase-site.xml"));
        //config.addResource(new Path(System.getenv("HADOOP_CONF_DIR"), "core-site.xml"));
        createSchemaTables(config);
        //modifySchema(config);
    }
}
