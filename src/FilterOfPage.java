import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;


/**
 * 分页过滤器
 */
public class FilterOfPage {
    private static final byte[] POSTFIX = new byte[] { 0x00 };


    public static void main(String[] args) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("user"));
        //分页大小
        Filter filter = new PageFilter(3);

        int totalRows = 0;
        byte[] lastRow = null;

        while (true) {
            Scan scan = new Scan();
            scan.setFilter(filter);
            if (lastRow != null) {
                byte[] startRow = Bytes.add(lastRow, POSTFIX);
                System.out.println("start row: " +
                        Bytes.toStringBinary(startRow));
                scan.setStartRow(startRow);
            }
            ResultScanner scanner = table.getScanner(scan);
            int localRows = 0;
            Result result;
            while ((result = scanner.next()) != null) {
                System.out.println(localRows++ + ": " + result);
                totalRows++;
                lastRow = result.getRow();
            }
            scanner.close();
            if (localRows == 0) break;
        }
        System.out.println("total rows: " + totalRows);

        table.close();
        connection.close();

    }
}
/**
 0: keyvalues={224382618261914241/info:age/1472196211169/Put/vlen=2/seqid=0, 224382618261914241/info:height/1472196211234/Put/vlen=3/seqid=0, 224382618261914241/info:name/1472196211088/Put/vlen=4/seqid=0, 224382618261914241/info:phone/1472196211427/Put/vlen=11/seqid=0, 224382618261914241/info:weight/1472196211386/Put/vlen=3/seqid=0, 224382618261914241/ship:addr/1472196211487/Put/vlen=7/seqid=0, 224382618261914241/ship:email/1472196211530/Put/vlen=11/seqid=0, 224382618261914241/ship:salary/1472196211594/Put/vlen=4/seqid=0}
 1: keyvalues={510824118261011172/info:age/1472196213020/Put/vlen=2/seqid=0, 510824118261011172/info:height/1472196213056/Put/vlen=3/seqid=0, 510824118261011172/info:name/1472196212942/Put/vlen=8/seqid=0, 510824118261011172/info:phone/1472196213237/Put/vlen=11/seqid=0, 510824118261011172/info:weight/1472196213169/Put/vlen=3/seqid=0, 510824118261011172/ship:addr/1472196213328/Put/vlen=8/seqid=0, 510824118261011172/ship:email/1472196213422/Put/vlen=12/seqid=0, 510824118261011172/ship:salary/1472196214963/Put/vlen=5/seqid=0}
 2: keyvalues={524382618264914241/info:age/1472196193913/Put/vlen=2/seqid=0, 524382618264914241/info:height/1472196194783/Put/vlen=3/seqid=0, 524382618264914241/info:name/1472196193255/Put/vlen=8/seqid=0, 524382618264914241/info:phone/1472196195125/Put/vlen=11/seqid=0, 524382618264914241/info:weight/1472196194970/Put/vlen=3/seqid=0, 524382618264914241/ship:addr/1472196195270/Put/vlen=7/seqid=0, 524382618264914241/ship:email/1472196195371/Put/vlen=13/seqid=0, 524382618264914241/ship:salary/1472196195485/Put/vlen=4/seqid=0}
 start row: 524382618264914241\x00
 0: keyvalues={673782618261019142/info:age/1472196211733/Put/vlen=2/seqid=0, 673782618261019142/info:height/1472196211761/Put/vlen=3/seqid=0, 673782618261019142/info:name/1472196211678/Put/vlen=7/seqid=0, 673782618261019142/info:phone/1472196211956/Put/vlen=11/seqid=0, 673782618261019142/info:weight/1472196211841/Put/vlen=3/seqid=0, 673782618261019142/ship:addr/1472196212059/Put/vlen=8/seqid=0, 673782618261019142/ship:email/1472196212176/Put/vlen=12/seqid=0, 673782618261019142/ship:salary/1472196212284/Put/vlen=4/seqid=0}
 1: keyvalues={813782218261011172/info:age/1472196212550/Put/vlen=2/seqid=0, 813782218261011172/info:height/1472196212605/Put/vlen=3/seqid=0, 813782218261011172/info:name/1472196212480/Put/vlen=8/seqid=0, 813782218261011172/info:phone/1472196212713/Put/vlen=11/seqid=0, 813782218261011172/info:weight/1472196212651/Put/vlen=3/seqid=0, 813782218261011172/ship:addr/1472196212762/Put/vlen=4/seqid=0, 813782218261011172/ship:email/1472196212802/Put/vlen=12/seqid=0, 813782218261011172/ship:salary/1472196212840/Put/vlen=5/seqid=0}
 start row: 813782218261011172\x00
 2016-08-29 17:17:57,197 INFO  [main] client.ConnectionManager$HConnectionImplementation: Closing zookeeper sessionid=0x56c13890bf003a
 total rows: 5
**/