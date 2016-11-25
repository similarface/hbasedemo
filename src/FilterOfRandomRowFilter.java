import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.RandomRowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * RandomRowFilter 随机行过滤器
 */
public class FilterOfRandomRowFilter {
    public static void main(String args[]) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf("user"));
        //该构造参数0-1之间,如果为负数全部过滤,大于1全部通过 0.2f表的该行数据20%的概率通过
        Filter filter = new RandomRowFilter(0.2f);
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
        table.close();
        connection.close();
    }
}

/**
 Cell: 510824118261011172/info:age/1472196213020/Put/vlen=2/seqid=0, Value: 18
 Cell: 510824118261011172/info:height/1472196213056/Put/vlen=3/seqid=0, Value: 188
 Cell: 510824118261011172/info:name/1472196212942/Put/vlen=8/seqid=0, Value: yangyang
 Cell: 510824118261011172/info:phone/1472196213237/Put/vlen=11/seqid=0, Value: 18013921626
 Cell: 510824118261011172/info:weight/1472196213169/Put/vlen=3/seqid=0, Value: 138
 Cell: 510824118261011172/ship:addr/1472196213328/Put/vlen=8/seqid=0, Value: shanghai
 Cell: 510824118261011172/ship:email/1472196213422/Put/vlen=12/seqid=0, Value: 199@sina.com
 Cell: 510824118261011172/ship:salary/1472196214963/Put/vlen=5/seqid=0, Value: 50000
**/