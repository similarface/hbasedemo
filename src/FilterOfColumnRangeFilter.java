import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.ColumnRangeFilter;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * ColumnRangeFilter 列范围过滤器
 */
public class FilterOfColumnRangeFilter {
    public static void main(String args[]) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf("user"));
        //minColumn - minimum value for the column range. If if it's null, there is no lower bound.
        //minColumnInclusive - if true, include minColumn in the range. 如果是true 就要包含minColumn
        //maxColumn - maximum value for the column range. If it's null,
        //maxColumnInclusive - if true, include maxColumn in the range. there is no upper bound.
        Filter filter = new ColumnRangeFilter(Bytes.toBytes("email"), true, Bytes.toBytes("phone"), false);
        Get get = new Get(Bytes.toBytes("224382618261914241"));
        get.setFilter(filter);
        Result result = table.get(get);
        System.out.println("Result of ColumnRangeFilter get: ");
        for (Cell cell : result.rawCells()) {
            System.out.println("Cell: " + cell + ", Value: " +
                    Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                            cell.getValueLength()));
        }

        Get get1 = new Get(Bytes.toBytes("224382618261914241"));
        Result result1 = table.get(get1);
        System.out.println("Result of get: ");
        for (Cell cell : result1.rawCells()) {
            System.out.println("Cell: " + cell + ", Value: " +
                    Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                            cell.getValueLength()));
        }
        table.close();
        connection.close();
    }
}

/**
 Result of ColumnRangeFilter get:
 Cell: 224382618261914241/info:height/1472196211234/Put/vlen=3/seqid=0, Value: 158
 Cell: 224382618261914241/info:name/1472196211088/Put/vlen=4/seqid=0, Value: lisi
 Cell: 224382618261914241/ship:email/1472196211530/Put/vlen=11/seqid=0, Value: qq@sina.com
 Result of get:
 Cell: 224382618261914241/info:age/1472196211169/Put/vlen=2/seqid=0, Value: 24
 Cell: 224382618261914241/info:height/1472196211234/Put/vlen=3/seqid=0, Value: 158
 Cell: 224382618261914241/info:name/1472196211088/Put/vlen=4/seqid=0, Value: lisi
 Cell: 224382618261914241/info:phone/1472196211427/Put/vlen=11/seqid=0, Value: 13213921424
 Cell: 224382618261914241/info:weight/1472196211386/Put/vlen=3/seqid=0, Value: 128
 Cell: 224382618261914241/ship:addr/1472196211487/Put/vlen=7/seqid=0, Value: chengdu
 Cell: 224382618261914241/ship:email/1472196211530/Put/vlen=11/seqid=0, Value: qq@sina.com
 Cell: 224382618261914241/ship:salary/1472196211594/Put/vlen=4/seqid=0, Value: 5000
 2016-08-31 17:53:28,394 INFO  [main] client.ConnectionManager$HConnectionImplementati
**/