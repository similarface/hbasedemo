import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 基于行键上的过滤器
 */
public class FilterInHbase {
    public static void main(String[] args) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立user表的连接
        Table table =connection.getTable(TableName.valueOf("user"));
        Scan scan=new Scan();
        //扫描列族info 列age
        scan.addColumn(Bytes.toBytes("info"),Bytes.toBytes("age"));

        System.out.println("行过滤器");
        //比较过滤器
        //这儿是指找出行小于或者等于"510824118261011172"的所有行
        Filter filter1 = new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL, new BinaryComparator(Bytes.toBytes("813782218261011172")));
        //添加过滤器到扫描器中
        scan.setFilter(filter1);
        ResultScanner scanner1 = table.getScanner(scan);
        for(Result res:scanner1){
            System.out.println(res);
        }
        scanner1.close();

        System.out.println("正则过滤器");
        //正则过滤器
        //过滤行键以2结束的
        Filter filter2 = new RowFilter(CompareFilter.CompareOp.EQUAL,
                new RegexStringComparator(".*2$")
                );
        scan.setFilter(filter2);
        ResultScanner scanner2 = table.getScanner(scan);
        for (Result res:scanner2){
            System.out.println(res);
        }
        scanner2.close();

        //子串过滤器
        //过滤行键中包含了"61826"这个字符串
        System.out.println("子串过滤器");
        Scan scan3=new Scan();
        //扫描列族info 列age
        scan3.addColumn(Bytes.toBytes("info"),Bytes.toBytes("age"));
        Filter filter3=new RowFilter(CompareFilter.CompareOp.EQUAL,
                new SubstringComparator("61826")
                );
        scan3.setFilter(filter3);
        ResultScanner scanner3=table.getScanner(scan3);
        for(Result res:scanner3){
            System.out.println(res);
        }
        scanner3.close();

        table.close();
        connection.close();
    }
}

/**
Result:

 行过滤器
 keyvalues={224382618261914241/info:age/1472196211169/Put/vlen=2/seqid=0}
 keyvalues={510824118261011172/info:age/1472196213020/Put/vlen=2/seqid=0}
 keyvalues={524382618264914241/info:age/1472196193913/Put/vlen=2/seqid=0}
 keyvalues={673782618261019142/info:age/1472196211733/Put/vlen=2/seqid=0}
 keyvalues={813782218261011172/info:age/1472196212550/Put/vlen=2/seqid=0}
 正则过滤器
 keyvalues={510824118261011172/info:age/1472196213020/Put/vlen=2/seqid=0}
 keyvalues={673782618261019142/info:age/1472196211733/Put/vlen=2/seqid=0}
 keyvalues={813782218261011172/info:age/1472196212550/Put/vlen=2/seqid=0}
 子串过滤器
 keyvalues={224382618261914241/info:age/1472196211169/Put/vlen=2/seqid=0}
 keyvalues={524382618264914241/info:age/1472196193913/Put/vlen=2/seqid=0}
 keyvalues={673782618261019142/info:age/1472196211733/Put/vlen=2/seqid=0}
 **/