import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.TimestampsFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * TimestampsFilter 时间过滤器
 */
public class FilterOfTimestampsFilter {

    public static void main(String args[]) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf("user"));
        List<Long> ts = new ArrayList<Long>();
        ts.add(new Long("1472196195270"));
        ts.add(new Long("1472196212480"));
        ts.add(new Long(15));
        Filter filter = new TimestampsFilter(ts);
        Scan scan1 = new Scan();
        scan1.setFilter(filter);
        ResultScanner scanner1 = table.getScanner(scan1);
        for(Result result:scanner1){
            System.out.println(result);
        }
        scanner1.close();
        Scan scan2 = new Scan();
        scan2.setFilter(filter);
        //加了时间范围 故意多加了1s 1472196212480+1=1472196212481
        scan2.setTimeRange(1472196195271L, 1472196212481L);
        ResultScanner scanner2 = table.getScanner(scan2);
        System.out.println("Add time range:");
        for (Result result : scanner2) {
            System.out.println(result);
        }
        scanner2.close();
    }
}
/**
 keyvalues={524382618264914241/ship:addr/1472196195270/Put/vlen=7/seqid=0}
 keyvalues={813782218261011172/info:name/1472196212480/Put/vlen=8/seqid=0}
 Add time range:
 keyvalues={813782218261011172/info:name/1472196212480/Put/vlen=8/seqid=0}
**/