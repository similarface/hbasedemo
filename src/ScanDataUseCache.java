import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.metrics.ScanMetrics;

import java.io.IOException;

/**
 * 使用缓存加载数据
 * RPCs = (Rows * Cols per Row) / Min(Cols per Row, Batch Size) / Scanner Caching
 * RPC请求次数=(行数*每行的列数)/Min(每行的列数,批量大小)/扫描缓存
 *
 --> scan中的setCaching与setBatch方法的区别是什么呢？
 setCaching设置的值为每次rpc的请求记录数，默认是1；cache大可以优化性能，但是太大了会花费很长的时间进行一次传输。
 setBatch设置每次取的column size；有些row特别大，所以需要分开传给client，就是一次传一个row的几个column。
 batch和caching和hbase table column size共同决意了rpc的次数。
 */
public class ScanDataUseCache {
    private static Table table=null;
    public static Table getTable() {
        if(table==null){
            try {
                Configuration configuration = HBaseConfiguration.create();
                Connection connection = ConnectionFactory.createConnection(configuration);
                //建立表的连接
                return connection.getTable(TableName.valueOf("testtable"));
            }catch (IOException e){
                return table;
            }
        }
        return table;
    }
    private static void scan(int caching,int batch,boolean small) {
        int count=0;
        //setCaching 设置的值为每次rpc的请求记录数，默认是1；cache大可以优化性能，但是太大了会花费很长的时间进行一次传输。
        //setBatch 设置每次取的column size；有些row特别大，所以需要分开传给client，就是一次传一个row的几个column。
        //setSmall 是否为小扫描
        //setScanMetricsEnabled 使用了集合
        Scan scan = new Scan().setCaching(caching).setBatch(batch).setSmall(small).setScanMetricsEnabled(true);
        ResultScanner scanner=null;
        try {
            scanner = getTable().getScanner(scan);
        }catch (IOException e){
            System.out.println(e);
        }
        if (scanner!=null){
            for (Result result:scanner){
                count++;
            }
        scanner.close();
        ScanMetrics metrics = scan.getScanMetrics();
        System.out.println("Caching: " + caching + ", Batch: " + batch + ", Small: " + small + ", Results: " + count + ", RPCs: " + metrics.countOfRPCcalls);
        }
        else {
            System.out.println("Error");
        }
    }

    public static void main(String[] args) throws IOException {
        // Caching: 1, Batch: 1, Small: false, Results: 9, RPCs: 12
        scan(1, 1, false);

        //Caching: 1, Batch: 0, Small: false, Results: 4, RPCs: 7
        scan(1, 0, false);

        // Caching: 1, Batch: 0, Small: true, Results: 4, RPCs: 0
        scan(1, 0, true);

        //Caching: 200, Batch: 1, Small: false, Results: 9, RPCs: 3
        scan(200, 1, false);

        //Caching: 200, Batch: 0, Small: false, Results: 4, RPCs: 3
        scan(200, 0, false);

        //Caching: 200, Batch: 0, Small: true, Results: 4, RPCs: 0
        scan(200, 0, true);

        // Caching: 2000, Batch: 100, Small: false, Results: 4, RPCs: 3
        scan(2000, 100, false);

        // Caching: 2, Batch: 100, Small: false, Results: 4, RPCs: 5
        scan(2, 100, false);

        // Caching: 2, Batch: 10, Small: false, Results: 4, RPCs: 5
        scan(2, 10, false);

        // Caching: 2, Batch: 10, Small: false, Results: 4, RPCs: 5
        scan(5, 100, false);

        // Caching: 5, Batch: 100, Small: false, Results: 4, RPCs: 3
        scan(5, 20, false);

        // Caching: 10, Batch: 10, Small: false, Results: 4, RPCs: 3
        scan(10, 10, false);
    }
}

/**
 Caching: 1, Batch: 0, Small: false, Results: 5, RPCs: 8
 Caching: 1, Batch: 0, Small: true, Results: 5, RPCs: 0
 Caching: 200, Batch: 1, Small: false, Results: 1009, RPCs: 8
 Caching: 200, Batch: 0, Small: false, Results: 5, RPCs: 3
 Caching: 200, Batch: 0, Small: true, Results: 5, RPCs: 0
 Caching: 2000, Batch: 100, Small: false, Results: 14, RPCs: 3
 Caching: 2, Batch: 100, Small: false, Results: 14, RPCs: 10
 Caching: 2, Batch: 10, Small: false, Results: 104, RPCs: 55
 Caching: 5, Batch: 100, Small: false, Results: 14, RPCs: 5
 Caching: 5, Batch: 20, Small: false, Results: 54, RPCs: 13
 Caching: 10, Batch: 10, Small: false, Results: 104, RPCs: 13
 **/