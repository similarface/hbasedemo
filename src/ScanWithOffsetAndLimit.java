import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.metrics.ScanMetrics;

import java.io.IOException;

/**
 * Created by similarface on 16/8/24.
 */
public class ScanWithOffsetAndLimit {
    private static Table table = null;

    public static Table getTable() {
        if (table == null) {
            try {
                Configuration configuration = HBaseConfiguration.create();
                Connection connection = ConnectionFactory.createConnection(configuration);
                //建立表的连接
                return connection.getTable(TableName.valueOf("testtable"));
            } catch (IOException e) {
                return table;
            }
        }
        return table;
    }

    /**
     * 遍历访问数据
     * @param num 运行次序
     * @param caching
     * @param batch
     * @param offset
     * @param maxResults
     * @param maxResultSize
     * @param dump
     * @throws IOException
     */
    private static void scan(int num, int caching, int batch, int offset, int maxResults, int maxResultSize, boolean dump
    ) throws IOException {
        int count = 0;
        Scan scan = new Scan().setCaching(caching).setBatch(batch)
                .setRowOffsetPerColumnFamily(offset)
                .setMaxResultsPerColumnFamily(maxResults)
                .setMaxResultSize(maxResultSize)
                .setScanMetricsEnabled(true);
        ResultScanner scanner = getTable().getScanner(scan);
        System.out.println("Scan #" + num + " running...");
        for (Result result : scanner) {
            count++;
            if (dump)
                System.out.println("Result [" + count + "]:" + result);
        }
        scanner.close();
        ScanMetrics metrics = scan.getScanMetrics();
        System.out.println("Caching: " + caching + ", Batch: " + batch +
                ", Offset: " + offset + ", maxResults: " + maxResults +
                ", maxSize: " + maxResultSize + ", Results: " + count +
                ", RPCs: " + metrics.countOfRPCcalls);
    }

    public static void main(String[] args) throws IOException {
        //偏移为0 最大2个cell 所以会扫描到列1 和列2
        scan(1, 11, 0, 0, 2, -1, true);
        //偏移为4 最大2个cell 所以会扫描到列5 和列6
        scan(2, 11, 0, 4, 2, -1, true);
        //
        scan(3, 5, 0, 0, 2, -1, false);
        scan(4, 11, 2, 0, 5, -1, true);
        scan(5, 11, -1, -1, -1, 1, false);
        scan(6, 11, -1, -1, -1, 10000, false);
    }
}

/**
 Caching: 11, Batch: 0, Offset: 0, maxResults: 2, maxSize: -1, Results: 5005, RPCs: 458
 Caching: 11, Batch: 0, Offset: 4, maxResults: 2, maxSize: -1, Results: 1, RPCs: 3
 Caching: 5, Batch: 0, Offset: 0, maxResults: 2, maxSize: -1, Results: 5005, RPCs: 1004
 Caching: 11, Batch: 2, Offset: 0, maxResults: 5, maxSize: -1, Results: 5009, RPCs: 458
 Caching: 11, Batch: -1, Offset: -1, maxResults: -1, maxSize: 1, Results: 5005, RPCs: 11012
 Caching: 11, Batch: -1, Offset: -1, maxResults: -1, maxSize: 10000, Results: 5005, RPCs: 469
**/