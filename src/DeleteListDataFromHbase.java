import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 在hbase中删除数据
 */
public class DeleteListDataFromHbase {
    public static void main(String args[]) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("testtable"));
        List<Delete> deletes = new ArrayList<Delete>();
        Delete delete1 = new Delete(Bytes.toBytes("row1"));
        delete1.setTimestamp(4);
        deletes.add(delete1);
        Delete delete2 = new Delete(Bytes.toBytes("row2"));
        delete2.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"));
        delete2.addColumns(Bytes.toBytes("colfam2"), Bytes.toBytes("qual3"), 5);
        deletes.add(delete2);
        Delete delete3 = new Delete(Bytes.toBytes("row3"));
        delete3.addFamily(Bytes.toBytes("colfam1"));
        delete3.addFamily(Bytes.toBytes("colfam2"), 3);
        deletes.add(delete3);
        table.delete(deletes);
    }
}
