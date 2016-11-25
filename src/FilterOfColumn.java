import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;


/**
 * 列过滤器
 * 根据列名进行筛选
 */
public class FilterOfColumn {
    public static void main(String[] args) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("user"));
        //现在表有很多个列 addr,age, email, height, name, phone, salary, weight,
        //取"height" 那么 应该出现[addr,age, email, height]
        Filter filter = new QualifierFilter(CompareFilter.CompareOp.LESS_OR_EQUAL,
                new BinaryComparator(Bytes.toBytes("height"))
                );

        //
        Filter filter1 = new FamilyFilter(CompareFilter.CompareOp.GREATER, new BinaryComparator(Bytes.toBytes("kiss")));
        Scan scan = new Scan();
        scan.setFilter(filter1);
        ResultScanner scanner = table.getScanner(scan);
        for (Result result : scanner){
            System.out.println(result);
        }
        scanner.close();

        Get get1 = new Get(Bytes.toBytes("673782618261019142"));
        get1.setFilter(filter1);
        Result result1=table.get(get1);
        System.out.println("Result of get1(): " + result1);

        //添加列族过滤器 info
        Filter filter2= new FamilyFilter(CompareFilter.CompareOp.EQUAL,new BinaryComparator(Bytes.toBytes("info")));
        //获取一行数据
        Get get2 = new Get(Bytes.toBytes("673782618261019142"));
        //但是get列族ship 那么==>> Result of get():keyvalues=NONE  [本身冲突 所以无数据]
        //如果get列族info 那么==>> Result of get2():keyvalues={673782618261019142/...
        get2.addFamily(Bytes.toBytes("ship"));
        get2.setFilter(filter2);
        Result result2 =table.get(get2);
        System.out.println("Result of get2():"+result2);

        scanner.close();
        table.close();
        connection.close();

    }
}
/**
 LESS "kiss"
 keyvalues={224382618261914241/info:age/1472196211169/Put/vlen=2/seqid=0, 224382618261914241/info:height/1472196211234/Put/vlen=3/seqid=0, 224382618261914241/info:name/1472196211088/Put/vlen=4/seqid=0, 224382618261914241/info:phone/1472196211427/Put/vlen=11/seqid=0, 224382618261914241/info:weight/1472196211386/Put/vlen=3/seqid=0}
 keyvalues={510824118261011172/info:age/1472196213020/Put/vlen=2/seqid=0, 510824118261011172/info:height/1472196213056/Put/vlen=3/seqid=0, 510824118261011172/info:name/1472196212942/Put/vlen=8/seqid=0, 510824118261011172/info:phone/1472196213237/Put/vlen=11/seqid=0, 510824118261011172/info:weight/1472196213169/Put/vlen=3/seqid=0}
 keyvalues={524382618264914241/info:age/1472196193913/Put/vlen=2/seqid=0, 524382618264914241/info:height/1472196194783/Put/vlen=3/seqid=0, 524382618264914241/info:name/1472196193255/Put/vlen=8/seqid=0, 524382618264914241/info:phone/1472196195125/Put/vlen=11/seqid=0, 524382618264914241/info:weight/1472196194970/Put/vlen=3/seqid=0}
 keyvalues={673782618261019142/info:age/1472196211733/Put/vlen=2/seqid=0, 673782618261019142/info:height/1472196211761/Put/vlen=3/seqid=0, 673782618261019142/info:name/1472196211678/Put/vlen=7/seqid=0, 673782618261019142/info:phone/1472196211956/Put/vlen=11/seqid=0, 673782618261019142/info:weight/1472196211841/Put/vlen=3/seqid=0}
 keyvalues={813782218261011172/info:age/1472196212550/Put/vlen=2/seqid=0, 813782218261011172/info:height/1472196212605/Put/vlen=3/seqid=0, 813782218261011172/info:name/1472196212480/Put/vlen=8/seqid=0, 813782218261011172/info:phone/1472196212713/Put/vlen=11/seqid=0, 813782218261011172/info:weight/1472196212651/Put/vlen=3/seqid=0}

 GREATER "kiss"
 keyvalues={224382618261914241/ship:addr/1472196211487/Put/vlen=7/seqid=0, 224382618261914241/ship:email/1472196211530/Put/vlen=11/seqid=0, 224382618261914241/ship:salary/1472196211594/Put/vlen=4/seqid=0}
 keyvalues={510824118261011172/ship:addr/1472196213328/Put/vlen=8/seqid=0, 510824118261011172/ship:email/1472196213422/Put/vlen=12/seqid=0, 510824118261011172/ship:salary/1472196214963/Put/vlen=5/seqid=0}
 keyvalues={524382618264914241/ship:addr/1472196195270/Put/vlen=7/seqid=0, 524382618264914241/ship:email/1472196195371/Put/vlen=13/seqid=0, 524382618264914241/ship:salary/1472196195485/Put/vlen=4/seqid=0}
 keyvalues={673782618261019142/ship:addr/1472196212059/Put/vlen=8/seqid=0, 673782618261019142/ship:email/1472196212176/Put/vlen=12/seqid=0, 673782618261019142/ship:salary/1472196212284/Put/vlen=4/seqid=0}
 keyvalues={813782218261011172/ship:addr/1472196212762/Put/vlen=4/seqid=0, 813782218261011172/ship:email/1472196212802/Put/vlen=12/seqid=0, 813782218261011172/ship:salary/1472196212840/Put/vlen=5/seqid=0}

 APPEND(LESS "kiss")
 Result of get(): keyvalues={673782618261019142/info:age/1472196211733/Put/vlen=2/seqid=0, 673782618261019142/info:height/1472196211761/Put/vlen=3/seqid=0, 673782618261019142/info:name/1472196211678/Put/vlen=7/seqid=0, 673782618261019142/info:phone/1472196211956/Put/vlen=11/seqid=0, 673782618261019142/info:weight/1472196211841/Put/vlen=3/seqid=0}
 APPEND(GREATER "kiss")
 Result of get1(): keyvalues={673782618261019142/ship:addr/1472196212059/Put/vlen=8/seqid=0, 673782618261019142/ship:email/1472196212176/Put/vlen=12/seqid=0, 673782618261019142/ship:salary/1472196212284/Put/vlen=4/seqid=0}

 //filter "info" get "ship"
 Result of get():keyvalues=NONE
 //filter "info" get "info"
 Result of get2():keyvalues={673782618261019142/info:age/1472196211733/Put/valen=2/seqid=0, 673782618261019142/info:height/1472196211761/Put/vlen=3/seqid=0, 673782618261019142/info:name/1472196211678/Put/vlen=7/seqid=0, 673782618261019142/info:phone/1472196211956/Put/vlen=11/seqid=0, 673782618261019142/info:weight/1472196211841/Put/vlen=3/seqid=0}

 **/