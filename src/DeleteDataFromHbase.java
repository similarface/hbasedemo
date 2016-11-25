import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 在hbase中删除数据
 */
public class DeleteDataFromHbase {
    public static void main(String args[]) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("testtable"));
        //穿件删除指定的行
        Delete delete = new Delete(Bytes.toBytes("5701"));
        //Set timestamp for row deletes.
        delete.setTimestamp(1);
        //只删除最后版本的一列
        delete.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual1000"));
        //删除指定版本的一列
        delete.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual1000"),3);
        //删除指定版本列的所有数据
        delete.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual1000"));
        //删除给定而且老的版本的列
        delete.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual1000"),2);
        //删除所有的列族
        delete.addFamily(Bytes.toBytes("colfam1"));
        //删除给定版本的列族数据
        delete.addFamily(Bytes.toBytes("colfam1"), 3);
        delete.addFamily(Bytes.toBytes("colfam1"), 2);
        delete.addFamily(Bytes.toBytes("colfam1"), 1);
        //删除操作
        table.delete(delete);
    }
}
/**
---
 5702                                            column=colfam1:qual0997, timestamp=1471844438322, value=val0997
 5702                                            column=colfam1:qual0998, timestamp=1471844438322, value=val0998
 5702                                            column=colfam1:qual0999, timestamp=1471844438322, value=val0999
 convert <== delete.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual0998"));
 5702                                            column=colfam1:qual0997, timestamp=1471844438322, value=val0997
 5702                                            column=colfam1:qual0998, timestamp=1471844438322, value=val0998
 5702                                            column=colfam1:qual0999, timestamp=1471844438322, value=val0999
 convert <== delete.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual0999"),3);
 5702                                            column=colfam1:qual0997, timestamp=1471844438322, value=val0997
 5702                                            column=colfam1:qual0998, timestamp=1471844438322, value=val0998
 5702                                            column=colfam1:qual0999, timestamp=1471844438322, value=val0999
 convert <==
 ---
 5702                                            column=colfam1:qual0998, timestamp=1, type=Delete
 5702                                            column=colfam1:qual0999, timestamp=1471844438322, value=val0999
 5702                                            column=colfam1:qual0999, timestamp=3, type=Delete

 *
 *
 */
