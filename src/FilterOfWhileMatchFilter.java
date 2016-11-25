import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * WhileMatchFilter 全匹配过滤器 [当匹配到就中断扫描]
 * similarface
 * similarface@outlook.com
 */
public class FilterOfWhileMatchFilter {
    public static void main(String args[]) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf("user"));
        //匹配 510824118261011172 的行
        Filter filter1=new RowFilter(CompareFilter.CompareOp.NOT_EQUAL,new BinaryComparator(Bytes.toBytes("510824118261011172")));
        Scan scan = new Scan();
        scan.setFilter(filter1);
        ResultScanner results = table.getScanner(scan);
        for (Result result:results) {
            for (Cell cell : result.rawCells()) {
                System.out.println("Cell: " + cell + ", Value: " +
                        Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                                cell.getValueLength()));
            }
        }
        //  匹配 510824118261011172 的行马上中断本次扫描操作
        Filter filter2 = new WhileMatchFilter(filter1);
        Scan scan1 = new Scan();
        scan1.setFilter(filter2);
        ResultScanner scanner2= table.getScanner(scan1);
        for(Result result:scanner2){
            for (Cell cell : result.rawCells()) {
                System.out.println("WhileMatchFilter Cell: " + cell + ", Value: " +
                        Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                                cell.getValueLength()));
            }
        }
        results.close();
        scanner2.close();
        table.close();
        connection.close();
    }
}

/**
 Cell: 224382618261914241/info:age/1472196211169/Put/vlen=2/seqid=0, Value: 24
 Cell: 224382618261914241/info:height/1472196211234/Put/vlen=3/seqid=0, Value: 158
 ...
 Cell: 524382618264914241/ship:addr/1472196195270/Put/vlen=7/seqid=0, Value: beijing
 Cell: 524382618264914241/ship:email/1472196195371/Put/vlen=13/seqid=0, Value: sina@sina.com
 ...
 Cell: 813782218261011172/ship:salary/1472196212840/Put/vlen=5/seqid=0, Value: 10000
 WhileMatchFilter Cell: 224382618261914241/info:age/1472196211169/Put/vlen=2/seqid=0, Value: 24
 ...
 WhileMatchFilter Cell: 224382618261914241/ship:salary/1472196211594/Put/vlen=4/seqid=0, Value: 5000
**/