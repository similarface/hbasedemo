/**
 * Created by similarface on 16/8/12.
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class HbaseExp01 {
    public static void main(String[] args) throws IOException {
        Configuration config = HBaseConfiguration.create();
        //创建连接
        Connection connection = ConnectionFactory.createConnection(config);
        try {
            //建立表连接
            Table table = connection.getTable(TableName.valueOf("testtable"));
            try {
                //添加行使用Put对象 加入了时间戳
                Put p = new Put(Bytes.toBytes("myrow-1"));
                //p.add(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("value11"));
                //table.put(p);
                // Close your table and cluster connection.

                Get g = new Get(Bytes.toBytes("myrow-1"));
                Result r = table.get(g);
                byte[] value = r.getValue(Bytes.toBytes("colfam1"),Bytes.toBytes("qual1"));
                String valueStr = Bytes.toString(value);
                System.out.println("GET: " + valueStr);


            } finally {
                if (table != null) table.close();
            }
        } finally {
            connection.close();
        }
    }
}
//http://hbase.apache.org/apidocs/index.html
/**
 result:
 hbase(main):007:0> scan 'testtable'
 ROW                                              COLUMN+CELL
 myrow-1                                         column=colfam1:q1, timestamp=1470907110406, value=value-1
 myrow-1                                         column=colfam1:q2, timestamp=1470907141104, value=value-2
 myrow-1                                         column=colfam1:q3, timestamp=1470909076625, value=value-3
 myrow-1                                         column=colfam1:q4, timestamp=1470995913079, value=value-4
 myrow-1                                         column=colfam1:q5, timestamp=1470989870343, value=value-5
 myrow-1                                         column=colfam1:qual1, timestamp=1470997376498, value=value11
 1 row(s) in 0.1430 seconds
 **/