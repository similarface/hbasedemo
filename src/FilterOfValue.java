import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;

/**
 * 值过滤器
 * 根据值进行筛选 可以联合RegexStringComparator 进行设计
 */
public class FilterOfValue {
    public static void main(String[] args) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("user"));

        //值中包含了177的过滤器
        Filter filter = new ValueFilter(CompareFilter.CompareOp.EQUAL,
                new SubstringComparator("1771392142")
                );

        Scan scan = new Scan();
        scan.setFilter(filter);
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner){
            for (Cell cell:result.rawCells()){
                System.out.println("Cell: "+cell+", Value: "+Bytes.toString(cell.getValueArray(),cell.getValueLength()));
            }
        }
        scanner.close();

        Get get1 = new Get(Bytes.toBytes("673782618261019142"));
        get1.setFilter(filter);
        Result result1=table.get(get1);
        for (Cell cell : result1.rawCells()) {
            System.out.println("Get1 Cell: " + cell + ", Value: " +
                    Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                            cell.getValueLength()));
        }

        Get get2 = new Get(Bytes.toBytes("813782218261011172"));
        get2.setFilter(filter);
        Result result2=table.get(get2);
        for (Cell cell : result2.rawCells()) {
            System.out.println("Get2 Cell: " + cell + ", Value: " +
                    Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                            cell.getValueLength()));
        }

        table.close();
        connection.close();

    }
}


/**
 原数据:
 673782618261019142                              column=info:phone, timestamp=1472196211956, value=17713921424
 813782218261011172                              column=info:phone, timestamp=1472196212713, value=12713921424
 *输出结果:
 Cell: 673782618261019142/info:phone/1472196211956/Put/vlen=11/seqid=0, Value: 73782618261019142infophone  VŻt�17713921424
 Get1 Cell: 673782618261019142/info:phone/1472196211956/Put/vlen=11/seqid=0, Value: 17713921424
**/