import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.RegionLocator;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.util.Pair;

import java.io.IOException;

/**

 void createTable(HTableDescriptor desc)
 void createTable(HTableDescriptor desc, byte[] startKey,byte[] endKey, int numRegions)
 void createTable(HTableDescriptor desc, byte[][] splitKeys)
 void createTableAsync(HTableDescriptor desc, byte[][] splitKeys)

 这四个函数的相同点是都是根据表描述符来创建表。其中一个不同是前三个函数式同步创建（也就是表没创建完，函数不返回）。
 而带Async的这个函数式异步的（后台自动创建表）。
 第一个函数相对简单，就是创建一个表，这个表没有任何region。
 后三个函数是创建表的时候帮你分配好指定数量的region（提前分配region的好处，了解HBase的人都清楚，为了减少Split，这样能节省不少时间）
 第二个函数是使用者指定表的“起始行键”、“末尾行键”和region的数量，这样系统自动给你划分region。根据的region数，来均分所有的行键。
 这个方法的问题是如果你的表的行键不是连续的，那样的话就导致有些region的行键不会用到，有些region是全满的。
 所以HBase很人性的给了第三种和第四种方法。这两个函数是用户需要自己region的划分。
 这个函数的参数splitKeys是一个二维字节数据，行的最大数表示region划分数+1，列就表示region和region之间的行键
 */
public class PredefinedRegionWhenCreateTable {
    private static Configuration configuration = null;
    private static Connection connection = null;

    private static void printTableRegions(String tableName) throws IOException{
        System.out.println("Printing regions of table: " + tableName);
        TableName tn = TableName.valueOf(tableName);
        RegionLocator locator = connection.getRegionLocator(tn);
        Pair<byte[][] ,byte[][]> pair = locator.getStartEndKeys();
        for (int n = 0; n < pair.getFirst().length; n++) {
            byte[] sk = pair.getFirst()[n];
            byte[] ek = pair.getSecond()[n];
            System.out.println("["+(n+1)+"]"+" start key:"+(sk.length==8? Bytes.toLong(sk):Bytes.toStringBinary(sk))
            +
                    ",end key: "+(ek.length==8?Bytes.toLong(ek):Bytes.toStringBinary(ek)));
    }
        locator.close();
    }
    public static void main(String args[]) throws  IOException{
        Configuration conf = HBaseConfiguration.create();
        connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        HTableDescriptor desc = new HTableDescriptor(TableName.valueOf("testtablex1"));
        HColumnDescriptor coldef = new HColumnDescriptor(Bytes.toBytes("colfam3"));
        desc.addFamily(coldef);

        admin.createTable(desc ,Bytes.toBytes(1L),Bytes.toBytes(100L),10);

        printTableRegions("testtablex1");
        byte[][] regions = new byte[][] {
                Bytes.toBytes("A"),
                Bytes.toBytes("D"),
                Bytes.toBytes("G"),
                Bytes.toBytes("K"),
                Bytes.toBytes("O"),
                Bytes.toBytes("T")
        };
        HTableDescriptor desc2 = new HTableDescriptor(
                TableName.valueOf("testtablex2"));
        desc2.addFamily(coldef);
        admin.createTable(desc2, regions);
        printTableRegions("testtablex2");

    }
}
