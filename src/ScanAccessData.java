import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;

/**
 * 遍历获取数据
 */
public class ScanAccessData {
    public static void main(String[] args) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("testtable"));
        //创建一个空的Scan实例
        Scan scan1 = new Scan();
        //在行上获取遍历器
        ResultScanner scanner1 = table.getScanner(scan1);
        //打印行的值
        for (Result res : scanner1) {
            System.out.println(res);
        }
        //关闭释放资源
        scanner1.close();

        Scan scan2 = new Scan();
        //添加限定列族
        scan2.addFamily(Bytes.toBytes("colfam1"));
        ResultScanner scanner2 = table.getScanner(scan2);
        for (Result res : scanner2) {
            System.out.println(res);
        }
        scanner2.close();

        Scan scan3 = new Scan();
        //添加限定列族 列分隔 行偏移
        scan3.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("col-5")).
                addColumn(Bytes.toBytes("colfam2"), Bytes.toBytes("col-33")).
                setStartRow(Bytes.toBytes("row-10")).
                setStopRow(Bytes.toBytes("row-20"));
        ResultScanner scanner3 = table.getScanner(scan3);
        for (Result res : scanner3) {
            System.out.println(res);
        }
        scanner3.close();

        Scan scan4 = new Scan();
        scan4.addColumn(Bytes.toBytes("colfam1"),
                Bytes.toBytes("col-5")).
                setStartRow(Bytes.toBytes("row-10")).
                setStopRow(Bytes.toBytes("row-20"));
        ResultScanner scanner4 = table.getScanner(scan4);
        for (Result res : scanner4) {
            System.out.println(res);
        }
        scanner4.close();

        Scan scan5 = new Scan();
        //添加倒序
        scan5.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("col-5")).
                setStartRow(Bytes.toBytes("row-20")).
                setStopRow(Bytes.toBytes("row-10")).
                setReversed(true);
        ResultScanner scanner5 = table.getScanner(scan5);
        for (Result res : scanner5) {
            System.out.println(res);
        }
        scanner5.close();
    }
}
