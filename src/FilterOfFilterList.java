import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * FilterList 过滤器列表[多个过滤器组合在一起]
 * similarface
 * similarface@outlook.com
 */
public class FilterOfFilterList {
    public static void main(String args[]) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf("user"));
        List<Filter> filters = new ArrayList<Filter>();
        Filter filter1=new RowFilter(CompareFilter.CompareOp.GREATER_OR_EQUAL,
                new BinaryComparator(Bytes.toBytes("524382618264914241")));
        filters.add(filter1);

        Filter filter2=new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL,
                new BinaryComparator(Bytes.toBytes("813782218261011172")));

        filters.add(filter2);
        //列名过滤器
        Filter filter3=new QualifierFilter(CompareFilter.CompareOp.EQUAL,
                new RegexStringComparator("age"));

        filters.add(filter3);
        //行键 Between "524382618264914241" and "813782218261011172"  and column="age"
        FilterList filterList1 = new FilterList(filters);

        Scan scan = new Scan();
        scan.setFilter(filterList1);

        ResultScanner results = table.getScanner(scan);
        for (Result result:results) {
            for (Cell cell : result.rawCells()) {
                System.out.println("Cell: " + cell + ", Value: " +
                        Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                                cell.getValueLength()));
            }
        }
        results.close();
        //行键 Between "524382618264914241" or "813782218261011172"  or column="age"
        FilterList filterList2 = new FilterList(FilterList.Operator.MUST_PASS_ONE,filters);
        scan.setFilter(filterList2);
        ResultScanner scanner2= table.getScanner(scan);
        for(Result result:scanner2){
            for (Cell cell : result.rawCells()) {
                System.out.println("MUST_PASS_ONE Cell: " + cell + ", Value: " +
                        Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                                cell.getValueLength()));
            }
        }
        scanner2.close();
        table.close();
        connection.close();
    }
}

/**

 Cell: 524382618264914241/info:age/1472196193913/Put/vlen=2/seqid=0, Value: 30
 Cell: 673782618261019142/info:age/1472196211733/Put/vlen=2/seqid=0, Value: 19
 Cell: 813782218261011172/info:age/1472196212550/Put/vlen=2/seqid=0, Value: 19
 MUST_PASS_ONE Cell: 224382618261914241/info:age/1472196211169/Put/vlen=2/seqid=0, Value: 24
 MUST_PASS_ONE Cell: 224382618261914241/info:height/1472196211234/Put/vlen=3/seqid=0, Value: 158
 MUST_PASS_ONE Cell: 224382618261914241/info:name/1472196211088/Put/vlen=4/seqid=0, Value: lisi
 MUST_PASS_ONE Cell: 224382618261914241/info:phone/1472196211427/Put/vlen=11/seqid=0, Value: 13213921424
 MUST_PASS_ONE Cell: 224382618261914241/info:weight/1472196211386/Put/vlen=3/seqid=0, Value: 128
 MUST_PASS_ONE Cell: 224382618261914241/ship:addr/1472196211487/Put/vlen=7/seqid=0, Value: chengdu
 MUST_PASS_ONE Cell: 224382618261914241/ship:email/1472196211530/Put/vlen=11/seqid=0, Value: qq@sina.com
 MUST_PASS_ONE Cell: 224382618261914241/ship:salary/1472196211594/Put/vlen=4/seqid=0, Value: 5000
 MUST_PASS_ONE Cell: 510824118261011172/info:age/1472196213020/Put/vlen=2/seqid=0, Value: 18
 MUST_PASS_ONE Cell: 510824118261011172/info:height/1472196213056/Put/vlen=3/seqid=0, Value: 188
 MUST_PASS_ONE Cell: 510824118261011172/info:name/1472196212942/Put/vlen=8/seqid=0, Value: yangyang
 MUST_PASS_ONE Cell: 510824118261011172/info:phone/1472196213237/Put/vlen=11/seqid=0, Value: 18013921626
 MUST_PASS_ONE Cell: 510824118261011172/info:weight/1472196213169/Put/vlen=3/seqid=0, Value: 138
 MUST_PASS_ONE Cell: 510824118261011172/ship:addr/1472196213328/Put/vlen=8/seqid=0, Value: shanghai
 MUST_PASS_ONE Cell: 510824118261011172/ship:email/1472196213422/Put/vlen=12/seqid=0, Value: 199@sina.com
 MUST_PASS_ONE Cell: 510824118261011172/ship:salary/1472196214963/Put/vlen=5/seqid=0, Value: 50000
 MUST_PASS_ONE Cell: 524382618264914241/info:age/1472196193913/Put/vlen=2/seqid=0, Value: 30
 MUST_PASS_ONE Cell: 524382618264914241/info:height/1472196194783/Put/vlen=3/seqid=0, Value: 168
 MUST_PASS_ONE Cell: 524382618264914241/info:name/1472196193255/Put/vlen=8/seqid=0, Value: zhangsan
 MUST_PASS_ONE Cell: 524382618264914241/info:phone/1472196195125/Put/vlen=11/seqid=0, Value: 13212321424
 MUST_PASS_ONE Cell: 524382618264914241/info:weight/1472196194970/Put/vlen=3/seqid=0, Value: 168
 MUST_PASS_ONE Cell: 524382618264914241/ship:addr/1472196195270/Put/vlen=7/seqid=0, Value: beijing
 MUST_PASS_ONE Cell: 524382618264914241/ship:email/1472196195371/Put/vlen=13/seqid=0, Value: sina@sina.com
 MUST_PASS_ONE Cell: 524382618264914241/ship:salary/1472196195485/Put/vlen=4/seqid=0, Value: 3000
 MUST_PASS_ONE Cell: 673782618261019142/info:age/1472196211733/Put/vlen=2/seqid=0, Value: 19
 MUST_PASS_ONE Cell: 673782618261019142/info:height/1472196211761/Put/vlen=3/seqid=0, Value: 178
 MUST_PASS_ONE Cell: 673782618261019142/info:name/1472196211678/Put/vlen=7/seqid=0, Value: zhaoliu
 MUST_PASS_ONE Cell: 673782618261019142/info:phone/1472196211956/Put/vlen=11/seqid=0, Value: 17713921424
 MUST_PASS_ONE Cell: 673782618261019142/info:weight/1472196211841/Put/vlen=3/seqid=0, Value: 188
 MUST_PASS_ONE Cell: 673782618261019142/ship:addr/1472196212059/Put/vlen=8/seqid=0, Value: shenzhen
 MUST_PASS_ONE Cell: 673782618261019142/ship:email/1472196212176/Put/vlen=12/seqid=0, Value: 126@sina.com
 MUST_PASS_ONE Cell: 673782618261019142/ship:salary/1472196212284/Put/vlen=4/seqid=0, Value: 8000
 MUST_PASS_ONE Cell: 813782218261011172/info:age/1472196212550/Put/vlen=2/seqid=0, Value: 19
 MUST_PASS_ONE Cell: 813782218261011172/info:height/1472196212605/Put/vlen=3/seqid=0, Value: 158
 MUST_PASS_ONE Cell: 813782218261011172/info:name/1472196212480/Put/vlen=8/seqid=0, Value: wangmazi
 MUST_PASS_ONE Cell: 813782218261011172/info:phone/1472196212713/Put/vlen=11/seqid=0, Value: 12713921424
 MUST_PASS_ONE Cell: 813782218261011172/info:weight/1472196212651/Put/vlen=3/seqid=0, Value: 118
 MUST_PASS_ONE Cell: 813782218261011172/ship:addr/1472196212762/Put/vlen=4/seqid=0, Value: xian
 MUST_PASS_ONE Cell: 813782218261011172/ship:email/1472196212802/Put/vlen=12/seqid=0, Value: 139@sina.com
 MUST_PASS_ONE Cell: 813782218261011172/ship:salary/1472196212840/Put/vlen=5/seqid=0, Value: 10000
**/