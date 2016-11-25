import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueExcludeFilter;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.filter.SubstringComparator;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;

/**
 * SingleColumnValueFilter 单列过滤器
 */
public class FilterOfSingleColumnValueFilter {
    public static void main(String args[]) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf("user"));

        //  510824118261011172 column=ship:email, timestamp=1472196213422, value=199@sina.com
        SingleColumnValueExcludeFilter singleColumnValueExcludeFilter = new SingleColumnValueExcludeFilter(Bytes.toBytes("ship"),Bytes.toBytes("email"), CompareFilter.CompareOp.EQUAL,new SubstringComparator("199@sina.com"));
        singleColumnValueExcludeFilter.setFilterIfMissing(true);

        Scan scan = new Scan();
        scan.setFilter(singleColumnValueExcludeFilter);
        ResultScanner results = table.getScanner(scan);
        for (Result result:results){
            for (Cell cell :result.rawCells()){
                System.out.println("Cell: "+cell+",Value:" + Bytes.toString(cell.getValueArray(),cell.getValueOffset(), cell.getValueLength()));
            }
        }
        results.close();
        // 224382618261914241 column=ship:email, timestamp=1472196211530, value=qq@sina.com
        Get get = new Get(Bytes.toBytes("224382618261914241"));
        get.setFilter(singleColumnValueExcludeFilter);
        Result result = table.get(get);
        System.out.println("Result of get: ");
        for (Cell cell : result.rawCells()) {
            System.out.println("Cell: " + cell + ", Value: " +
                    Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                            cell.getValueLength()));
        }

        Get get1 = new Get(Bytes.toBytes("510824118261011172"));
        get1.setFilter(singleColumnValueExcludeFilter);
        Result result1 = table.get(get1);
        System.out.println("Result of get1: ");
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
 Cell: 510824118261011172/info:age/1472196213020/Put/vlen=2/seqid=0,Value:18
 Cell: 510824118261011172/info:height/1472196213056/Put/vlen=3/seqid=0,Value:188
 Cell: 510824118261011172/info:name/1472196212942/Put/vlen=8/seqid=0,Value:yangyang
 Cell: 510824118261011172/info:phone/1472196213237/Put/vlen=11/seqid=0,Value:18013921626
 Cell: 510824118261011172/info:weight/1472196213169/Put/vlen=3/seqid=0,Value:138
 Cell: 510824118261011172/ship:addr/1472196213328/Put/vlen=8/seqid=0,Value:shanghai
 Cell: 510824118261011172/ship:email/1472196213422/Put/vlen=12/seqid=0,Value:199@sina.com
 Cell: 510824118261011172/ship:salary/1472196214963/Put/vlen=5/seqid=0,Value:50000
 Result of get:
 Result of get1:
 Cell: 510824118261011172/info:age/1472196213020/Put/vlen=2/seqid=0, Value: 18
 Cell: 510824118261011172/info:height/1472196213056/Put/vlen=3/seqid=0, Value: 188
 Cell: 510824118261011172/info:name/1472196212942/Put/vlen=8/seqid=0, Value: yangyang
 Cell: 510824118261011172/info:phone/1472196213237/Put/vlen=11/seqid=0, Value: 18013921626
 Cell: 510824118261011172/info:weight/1472196213169/Put/vlen=3/seqid=0, Value: 138
 Cell: 510824118261011172/ship:addr/1472196213328/Put/vlen=8/seqid=0, Value: shanghai
 Cell: 510824118261011172/ship:email/1472196213422/Put/vlen=12/seqid=0, Value: 199@sina.com
 Cell: 510824118261011172/ship:salary/1472196214963/Put/vlen=5/seqid=0, Value: 50000
**/