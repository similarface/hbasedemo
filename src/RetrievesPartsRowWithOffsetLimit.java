import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by similarface on 16/8/22.
 * 这儿实现了一个类似于MySQL的Limit的功能
 */
public class RetrievesPartsRowWithOffsetLimit {
    public static void main(String args[]) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("testtable"));
        Put put = new Put(Bytes.toBytes("5701"));
        for (int n = 1; n <= 1000; n++) {
            String num = String.format("%04d", n);
            put.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual"+num), Bytes.toBytes("val" + num));
        }
        table.put(put);
        Get get1 = new Get(Bytes.toBytes("5701"));
        //要求最多返回10个Cell
        get1.setMaxResultsPerColumnFamily(10);
        Result result1 = table.get(get1);
        CellScanner scanner1 = result1.cellScanner();
        //返回1-10的数据集
        while (scanner1.advance()) {
            System.out.println("Get 1 Cell: " + scanner1.current());
        }

        Get get2 = new Get(Bytes.toBytes("5701"));
        //要求最多返回10行
        get2.setMaxResultsPerColumnFamily(10);
        //跳过前面100
        get2.setRowOffsetPerColumnFamily(100);
        Result result2 = table.get(get2);
        CellScanner scanner2 = result2.cellScanner();
        //返回101-110的数据
        while (scanner2.advance()) {
            System.out.println("Get 2 Cell: " + scanner2.current());
        }
    }
}
/**
 result:
 Get 1 Cell: 5701/colfam1:qual0001/1471842173521/Put/vlen=7/seqid=0
 Get 1 Cell: 5701/colfam1:qual0002/1471842173521/Put/vlen=7/seqid=0
 Get 1 Cell: 5701/colfam1:qual0003/1471842173521/Put/vlen=7/seqid=0
 Get 1 Cell: 5701/colfam1:qual0004/1471842173521/Put/vlen=7/seqid=0
 Get 1 Cell: 5701/colfam1:qual0005/1471842173521/Put/vlen=7/seqid=0
 Get 1 Cell: 5701/colfam1:qual0006/1471842173521/Put/vlen=7/seqid=0
 Get 1 Cell: 5701/colfam1:qual0007/1471842173521/Put/vlen=7/seqid=0
 Get 1 Cell: 5701/colfam1:qual0008/1471842173521/Put/vlen=7/seqid=0
 Get 1 Cell: 5701/colfam1:qual0009/1471842173521/Put/vlen=7/seqid=0
 Get 1 Cell: 5701/colfam1:qual0010/1471842173521/Put/vlen=7/seqid=0
 Get 2 Cell: 5701/colfam1:qual0101/1471842173521/Put/vlen=7/seqid=0
 Get 2 Cell: 5701/colfam1:qual0102/1471842173521/Put/vlen=7/seqid=0
 Get 2 Cell: 5701/colfam1:qual0103/1471842173521/Put/vlen=7/seqid=0
 Get 2 Cell: 5701/colfam1:qual0104/1471842173521/Put/vlen=7/seqid=0
 Get 2 Cell: 5701/colfam1:qual0105/1471842173521/Put/vlen=7/seqid=0
 Get 2 Cell: 5701/colfam1:qual0106/1471842173521/Put/vlen=7/seqid=0
 Get 2 Cell: 5701/colfam1:qual0107/1471842173521/Put/vlen=7/seqid=0
 Get 2 Cell: 5701/colfam1:qual0108/1471842173521/Put/vlen=7/seqid=0
 Get 2 Cell: 5701/colfam1:qual0109/1471842173521/Put/vlen=7/seqid=0
 Get 2 Cell: 5701/colfam1:qual0110/1471842173521/Put/vlen=7/seqid=0
 **/