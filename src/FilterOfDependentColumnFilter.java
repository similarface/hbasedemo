import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;


/**
 * 参考列过滤器
 * 根据列名进行筛选
 */
public class FilterOfDependentColumnFilter {
    private static Table table=null;
    public static Table getTable() {
        if(table==null){
            try {
                Configuration configuration = HBaseConfiguration.create();
                Connection connection = ConnectionFactory.createConnection(configuration);
                //建立表的连接
                return connection.getTable(TableName.valueOf("user"));
            }catch (IOException e){
                return table;
            }
        }
        return table;
    }

    public static void filter(boolean drop,CompareFilter.CompareOp oper,ByteArrayComparable comparable) throws IOException {
        Filter filter;
        if (comparable != null) {
            filter = new DependentColumnFilter(Bytes.toBytes("info"), Bytes.toBytes("phone"), drop, oper, comparable);
        } else {
            filter = new DependentColumnFilter(Bytes.toBytes("info"), Bytes.toBytes("phone"), drop);
        }
        Scan scan = new Scan();
        scan.setFilter(filter);
        ResultScanner scanner = getTable().getScanner(scan);
        for (Result result : scanner) {
            for (Cell cell : result.rawCells()) {
                System.out.println("Cell: " + cell + ", Value: " +
                        Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                                cell.getValueLength()));
            }
        }
        scanner.close();
        Get get = new Get(Bytes.toBytes("673782618261019142"));
        get.setFilter(filter);
        Result result = getTable().get(get);
        for (Cell cell : result.rawCells()) {
            System.out.println("Cell: " + cell + ", Value: " +
                    Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                            cell.getValueLength()));
        }
    }




    public static void main(String[] args) throws IOException {
        filter(true, CompareFilter.CompareOp.NO_OP, null);
        filter(false, CompareFilter.CompareOp.NO_OP, null);
        filter(true, CompareFilter.CompareOp.EQUAL,
                new BinaryPrefixComparator(Bytes.toBytes("17713921424")));
        filter(false, CompareFilter.CompareOp.EQUAL,
                new BinaryPrefixComparator(Bytes.toBytes("17713921424")));
        filter(true, CompareFilter.CompareOp.EQUAL,
                new RegexStringComparator(".*\\.5"));
        filter(false, CompareFilter.CompareOp.EQUAL,
                new RegexStringComparator(".*\\.5"));
    }
}
/**

 **/