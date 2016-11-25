import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 单计数器
 * similarface
 * similarface@outlook.com
 */
public class SingleCounter {
    public static void main(String args[]) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf("counters"));
        //incrementColumnValue(行号,列族,列,步长)
        long cnt1=table.incrementColumnValue(Bytes.toBytes("20150105"),Bytes.toBytes("daily"),Bytes.toBytes("hits"),1L);
        System.out.println(cnt1);
        long cnt2=table.incrementColumnValue(Bytes.toBytes("20150105"),Bytes.toBytes("daily"),Bytes.toBytes("hits"),1);
        System.out.println(cnt2);
        long current=table.incrementColumnValue(Bytes.toBytes("20150105"),Bytes.toBytes("daily"),Bytes.toBytes("hits"),0);
        System.out.println(current);
        long cnt3=table.incrementColumnValue(Bytes.toBytes("20150105"),Bytes.toBytes("daily"),Bytes.toBytes("hits"),-1);
        System.out.println(cnt3);
        table.close();
        connection.close();
    }
}
/**
 1
 2
 2
 1
 **/