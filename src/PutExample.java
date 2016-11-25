/**
 * Created by similarface on 16/8/17.
 */
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class PutExample {
    public static void main(String[] args) throws IOException{
        //创建必要的参数
        Configuration conf = HBaseConfiguration.create();
        //初始化客户端
        Connection connection=ConnectionFactory.createConnection(conf);
        Table table=connection.getTable(TableName.valueOf("testtable"));
        //对指定的列创建put对象
        for (int i=3000;i<5000;i++){
            Put put = new Put(Bytes.toBytes(Integer.toString(i)));
            put.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual1"),Bytes.toBytes(Integer.toString(i)));
            put.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual2"),Bytes.toBytes(Integer.toString(i)));
            table.put(put);
            System.out.print(i);
        }
        //关闭表
        table.close();
        //关闭连接
        connection.close();
    }
}
