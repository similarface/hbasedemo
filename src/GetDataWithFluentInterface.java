import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by similarface on 16/8/22.
 */
public class GetDataWithFluentInterface {
    public static void main(String args[]) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("testtable"));
        //获取行键
        Get get = new Get(Bytes.toBytes("10000"))
                .setId("GetFluentExample")
                .setMaxVersions()
                .setTimeStamp(1)
                .addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("company2"))
                .addFamily(Bytes.toBytes("colfam1"));
        Result result = table.get(get);
        System.out.println("Result: " + result);
        //
        table.close();

        connection.close();
    }
}
