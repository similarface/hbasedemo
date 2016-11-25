import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HConstants;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;

import java.io.IOException;

/**
 * Created by similarface on 16/8/23.
 */
public class ScanTableWhenTimeoutOper {
    public static void main(String args[]) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("testtable"));
        Scan scan = new Scan();
        ResultScanner scanner = table.getScanner(scan);
        int scannerTimeout = (int) configuration.getLong(
                HConstants.HBASE_CLIENT_SCANNER_TIMEOUT_PERIOD, -1);
        try {
            Thread.sleep(scannerTimeout + 5000);
        } catch (InterruptedException e) {

        }
        while (true) {
            try {
                Result result = scanner.next();
                if (result == null) break;
                System.out.println(result);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
        scanner.close();
        table.close();
        connection.close();
    }
}
/**
 * org.apache.hadoop.hbase.client.ScannerTimeoutException: 65489ms passed since the last invocation, timeout is currently set to 60000
 */
