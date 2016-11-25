import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by similarface on 16/8/22.
 * 这儿实现了一个类似于MySQL的Limit的功能 + 版本号控制
 */
public class RetrievesPartsRowWithOffsetLimit2 {
    public static void main(String args[]) throws IOException ,InterruptedException {
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("testtable"));
        //三个版本的数据
        for (int version = 1; version <= 3; version++) {
            Put put = new Put(Bytes.toBytes("5702"));
            //插入1000个列
            for (int n = 1; n <= 1000; n++) {
                String num = String.format("%04d", n);
                put.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual" + num), Bytes.toBytes("val" + num));
            }
            System.out.println("Writing version: " + version);
            table.put(put);
            Thread.currentThread().sleep(1000);
        }

        Get get0 = new Get(Bytes.toBytes("5702"));
        get0.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual0001"));
        //获取最大的版本
        get0.setMaxVersions();
        Result result0 = table.get(get0);
        CellScanner scanner0 = result0.cellScanner();
        while (scanner0.advance()) {
            System.out.println("Get 0 Cell: " + scanner0.current());
        }

        Get get1=new Get(Bytes.toBytes("5702"));
        //1-10
        get1.setMaxResultsPerColumnFamily(10);
        Result result1 = table.get(get1);
        CellScanner scanner1 = result1.cellScanner();
        while (scanner1.advance()) {
            System.out.println("Get 1 Cell: " + scanner1.current());
        }

        Get get2 = new Get(Bytes.toBytes("5702"));
        //get2.setMaxResultsPerColumnFamily(10);
        //设置版本号 发现最后的结果没有版本区分
        get2.setMaxVersions(3);
        Result result2 = table.get(get2);
        CellScanner scanner2 = result2.cellScanner();
        while (scanner2.advance()) {
            System.out.println("Get 2 Cell: " + scanner2.current());
        }
    }
}

//get2.setMaxVersions(3); 最后的结果集确没有版本的控制.

/**
 result:
 Writing version: 1
 Writing version: 2
 Writing version: 3
 Get 0 Cell: 5702/colfam1:qual0001/1471844438322/Put/vlen=7/seqid=0
 Get 1 Cell: 5702/colfam1:qual0001/1471844438322/Put/vlen=7/seqid=0
 Get 1 Cell: 5702/colfam1:qual0002/1471844438322/Put/vlen=7/seqid=0
 Get 1 Cell: 5702/colfam1:qual0003/1471844438322/Put/vlen=7/seqid=0
 Get 1 Cell: 5702/colfam1:qual0004/1471844438322/Put/vlen=7/seqid=0
 Get 1 Cell: 5702/colfam1:qual0005/1471844438322/Put/vlen=7/seqid=0
 Get 1 Cell: 5702/colfam1:qual0006/1471844438322/Put/vlen=7/seqid=0
 Get 1 Cell: 5702/colfam1:qual0007/1471844438322/Put/vlen=7/seqid=0
 Get 1 Cell: 5702/colfam1:qual0008/1471844438322/Put/vlen=7/seqid=0
 Get 1 Cell: 5702/colfam1:qual0009/1471844438322/Put/vlen=7/seqid=0
 Get 1 Cell: 5702/colfam1:qual0010/1471844438322/Put/vlen=7/seqid=0
 Get 2 Cell: 5702/colfam1:qual0001/1471844438322/Put/vlen=7/seqid=0
 Get 2 Cell: 5702/colfam1:qual0002/1471844438322/Put/vlen=7/seqid=0
 Get 2 Cell: 5702/colfam1:qual0003/1471844438322/Put/vlen=7/seqid=0
 Get 2 Cell: 5702/colfam1:qual0004/1471844438322/Put/vlen=7/seqid=0
 Get 2 Cell: 5702/colfam1:qual0005/1471844438322/Put/vlen=7/seqid=0
 Get 2 Cell: 5702/colfam1:qual0006/1471844438322/Put/vlen=7/seqid=0
 Get 2 Cell: 5702/colfam1:qual0007/1471844438322/Put/vlen=7/seqid=0
 Get 2 Cell: 5702/colfam1:qual0008/1471844438322/Put/vlen=7/seqid=0
 Get 2 Cell: 5702/colfam1:qual0009/1471844438322/Put/vlen=7/seqid=0
 Get 2 Cell: 5702/colfam1:qual0010/1471844438322/Put/vlen=7/seqid=0
 **/

/**
 problem:

-----
 get2.setMaxVersions(3); 最后的结果集确没有版本的控制.


 shell存在的:
 5702                                            column=colfam1:qual0999, timestamp=1471844438322, value=val0999
 5702                                            column=colfam1:qual0999, timestamp=1471844437199, value=val0999
 5702                                            column=colfam1:qual0999, timestamp=1471844436037, value=val0999
 5702                                            column=colfam1:qual1000, timestamp=1471844438322, value=val1000
 5702                                            column=colfam1:qual1000, timestamp=1471844437199, value=val1000
 5702                                            column=colfam1:qual1000, timestamp=1471844436037, value=val1000
-----
 **/