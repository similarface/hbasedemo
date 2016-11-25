import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;

/**
 * SkipFilter 跳过(忽略)过滤器
 * similarface
 * similarface@outlook.com
 */
public class FilterOfSkipFilter {
    public static void main(String args[]) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf("user"));
        //510824118261011172                                     column=info:height, timestamp=1472196213056, value=188
        //673782618261019142                                     column=info:weight, timestamp=1472196211841, value=188
        //如果列值中含有188这个数值,那么这列将会跳过
        Filter filter = new ValueFilter(CompareFilter.CompareOp.NOT_EQUAL,new BinaryComparator(Bytes.toBytes("188")));

        Scan scan = new Scan();
        scan.setFilter(filter);
        ResultScanner results = table.getScanner(scan);
        for (Result result:results) {
            for (Cell cell : result.rawCells()) {
                System.out.println("Cell: " + cell + ", Value: " +
                        Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                                cell.getValueLength()));
            }
        }
        Scan scan1 = new Scan();
        //skipfilter的构造参数时filter
        //  filter==>如果列值中含有188这个数值,那么这列将会跳过 filter2==>就是如果列值中含有188这个数值那么整个行都会被跳过 表示不会出现[510824118261011172,673782618261019142]
        Filter filter2 = new SkipFilter(filter);
        scan1.setFilter(filter2);
        ResultScanner scanner2= table.getScanner(scan1);
        for(Result result:scanner2){
            for (Cell cell : result.rawCells()) {
                System.out.println("SKIP Cell: " + cell + ", Value: " +
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
 Cell: 224382618261914241/info:name/1472196211088/Put/vlen=4/seqid=0, Value: lisi
 Cell: 224382618261914241/info:phone/1472196211427/Put/vlen=11/seqid=0, Value: 13213921424
 Cell: 224382618261914241/info:weight/1472196211386/Put/vlen=3/seqid=0, Value: 128
 Cell: 224382618261914241/ship:addr/1472196211487/Put/vlen=7/seqid=0, Value: chengdu
 Cell: 224382618261914241/ship:email/1472196211530/Put/vlen=11/seqid=0, Value: qq@sina.com
 Cell: 224382618261914241/ship:salary/1472196211594/Put/vlen=4/seqid=0, Value: 5000
 Cell: 510824118261011172/info:age/1472196213020/Put/vlen=2/seqid=0, Value: 18
 Cell: 510824118261011172/info:name/1472196212942/Put/vlen=8/seqid=0, Value: yangyang
 Cell: 510824118261011172/info:phone/1472196213237/Put/vlen=11/seqid=0, Value: 18013921626
 Cell: 510824118261011172/info:weight/1472196213169/Put/vlen=3/seqid=0, Value: 138
 Cell: 510824118261011172/ship:addr/1472196213328/Put/vlen=8/seqid=0, Value: shanghai
 Cell: 510824118261011172/ship:email/1472196213422/Put/vlen=12/seqid=0, Value: 199@sina.com
 Cell: 510824118261011172/ship:salary/1472196214963/Put/vlen=5/seqid=0, Value: 50000
 Cell: 524382618264914241/info:age/1472196193913/Put/vlen=2/seqid=0, Value: 30
 Cell: 524382618264914241/info:height/1472196194783/Put/vlen=3/seqid=0, Value: 168
 Cell: 524382618264914241/info:name/1472196193255/Put/vlen=8/seqid=0, Value: zhangsan
 Cell: 524382618264914241/info:phone/1472196195125/Put/vlen=11/seqid=0, Value: 13212321424
 Cell: 524382618264914241/info:weight/1472196194970/Put/vlen=3/seqid=0, Value: 168
 Cell: 524382618264914241/ship:addr/1472196195270/Put/vlen=7/seqid=0, Value: beijing
 Cell: 524382618264914241/ship:email/1472196195371/Put/vlen=13/seqid=0, Value: sina@sina.com
 Cell: 524382618264914241/ship:salary/1472196195485/Put/vlen=4/seqid=0, Value: 3000
 Cell: 673782618261019142/info:age/1472196211733/Put/vlen=2/seqid=0, Value: 19
 Cell: 673782618261019142/info:height/1472196211761/Put/vlen=3/seqid=0, Value: 178
 Cell: 673782618261019142/info:name/1472196211678/Put/vlen=7/seqid=0, Value: zhaoliu
 Cell: 673782618261019142/info:phone/1472196211956/Put/vlen=11/seqid=0, Value: 17713921424
 Cell: 673782618261019142/ship:addr/1472196212059/Put/vlen=8/seqid=0, Value: shenzhen
 Cell: 673782618261019142/ship:email/1472196212176/Put/vlen=12/seqid=0, Value: 126@sina.com
 Cell: 673782618261019142/ship:salary/1472196212284/Put/vlen=4/seqid=0, Value: 8000
 Cell: 813782218261011172/info:age/1472196212550/Put/vlen=2/seqid=0, Value: 19
 Cell: 813782218261011172/info:height/1472196212605/Put/vlen=3/seqid=0, Value: 158
 Cell: 813782218261011172/info:name/1472196212480/Put/vlen=8/seqid=0, Value: wangmazi
 Cell: 813782218261011172/info:phone/1472196212713/Put/vlen=11/seqid=0, Value: 12713921424
 Cell: 813782218261011172/info:weight/1472196212651/Put/vlen=3/seqid=0, Value: 118
 Cell: 813782218261011172/ship:addr/1472196212762/Put/vlen=4/seqid=0, Value: xian
 Cell: 813782218261011172/ship:email/1472196212802/Put/vlen=12/seqid=0, Value: 139@sina.com
 Cell: 813782218261011172/ship:salary/1472196212840/Put/vlen=5/seqid=0, Value: 10000

 SKIP Cell: 224382618261914241/info:age/1472196211169/Put/vlen=2/seqid=0, Value: 24
 SKIP Cell: 224382618261914241/info:height/1472196211234/Put/vlen=3/seqid=0, Value: 158
 SKIP Cell: 224382618261914241/info:name/1472196211088/Put/vlen=4/seqid=0, Value: lisi
 SKIP Cell: 224382618261914241/info:phone/1472196211427/Put/vlen=11/seqid=0, Value: 13213921424
 SKIP Cell: 224382618261914241/info:weight/1472196211386/Put/vlen=3/seqid=0, Value: 128
 SKIP Cell: 224382618261914241/ship:addr/1472196211487/Put/vlen=7/seqid=0, Value: chengdu
 SKIP Cell: 224382618261914241/ship:email/1472196211530/Put/vlen=11/seqid=0, Value: qq@sina.com
 SKIP Cell: 224382618261914241/ship:salary/1472196211594/Put/vlen=4/seqid=0, Value: 5000
 SKIP Cell: 524382618264914241/info:age/1472196193913/Put/vlen=2/seqid=0, Value: 30
 SKIP Cell: 524382618264914241/info:height/1472196194783/Put/vlen=3/seqid=0, Value: 168
 SKIP Cell: 524382618264914241/info:name/1472196193255/Put/vlen=8/seqid=0, Value: zhangsan
 SKIP Cell: 524382618264914241/info:phone/1472196195125/Put/vlen=11/seqid=0, Value: 13212321424
 SKIP Cell: 524382618264914241/info:weight/1472196194970/Put/vlen=3/seqid=0, Value: 168
 SKIP Cell: 524382618264914241/ship:addr/1472196195270/Put/vlen=7/seqid=0, Value: beijing
 SKIP Cell: 524382618264914241/ship:email/1472196195371/Put/vlen=13/seqid=0, Value: sina@sina.com
 SKIP Cell: 524382618264914241/ship:salary/1472196195485/Put/vlen=4/seqid=0, Value: 3000
 SKIP Cell: 813782218261011172/info:age/1472196212550/Put/vlen=2/seqid=0, Value: 19
 SKIP Cell: 813782218261011172/info:height/1472196212605/Put/vlen=3/seqid=0, Value: 158
 SKIP Cell: 813782218261011172/info:name/1472196212480/Put/vlen=8/seqid=0, Value: wangmazi
 SKIP Cell: 813782218261011172/info:phone/1472196212713/Put/vlen=11/seqid=0, Value: 12713921424
 SKIP Cell: 813782218261011172/info:weight/1472196212651/Put/vlen=3/seqid=0, Value: 118
 SKIP Cell: 813782218261011172/ship:addr/1472196212762/Put/vlen=4/seqid=0, Value: xian
 SKIP Cell: 813782218261011172/ship:email/1472196212802/Put/vlen=12/seqid=0, Value: 139@sina.com
 SKIP Cell: 813782218261011172/ship:salary/1472196212840/Put/vlen=5/seqid=0, Value: 10000
**/