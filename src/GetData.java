import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;

import java.io.IOException;

/**
 * Created by similarface on 16/8/22.
 */
public class GetData {
    public static void main(String args[]) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("testtable"));
        //获取行键
        Get get = new Get(Bytes.toBytes("5702"));
        //获取列族 和 列限定符号
        get.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual0998"));
        //
        Result result = table.get(get);
        //取得指定列的值
        byte[] val = result.getValue(Bytes.toBytes("colfam1"),Bytes.toBytes("qual0998"));
        //
        System.out.println("Value: "+Bytes.toString(val));
        //
        table.close();

        connection.close();
    }
}
