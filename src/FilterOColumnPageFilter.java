import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.ColumnPaginationFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * ColumnPageFilter 列分页过滤器
 */
public class FilterOColumnPageFilter {
    public static void main(String args[]) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf("user"));
        //限制返回的列数 从第4列开始取 取3列数据
        ColumnPaginationFilter columnPaginationFilter = new ColumnPaginationFilter(3,4);
        Get get = new Get(Bytes.toBytes("224382618261914241"));
        get.setFilter(columnPaginationFilter);
        Result result = table.get(get);
        System.out.println("Result of ColumnPageFilter get: ");
        for (Cell cell : result.rawCells()) {
            System.out.println("Cell: " + cell + ", Value: " +
                    Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                            cell.getValueLength()));
        }

        //限制返回的列数 从第name列开始取 取3列数据
        ColumnPaginationFilter columnPaginationFilter2 = new ColumnPaginationFilter(3,Bytes.toBytes("name"));
        Get get2 = new Get(Bytes.toBytes("224382618261914241"));
        get2.setFilter(columnPaginationFilter2);
        Result result2 = table.get(get2);
        System.out.println("Result of ColumnPageFilter get: ");
        for (Cell cell : result2.rawCells()) {
            System.out.println("Cell: " + cell + ", Value: " +
                    Bytes.toString(cell.getValueArray(), cell.getValueOffset(),
                            cell.getValueLength()));
        }

        Get get1 = new Get(Bytes.toBytes("224382618261914241"));
        Result result1 = table.get(get1);
        System.out.println("Result of get: ");
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
 Result of ColumnPageFilter get:
 Cell: 224382618261914241/info:weight/1472196211386/Put/vlen=3/seqid=0, Value: 128
 Cell: 224382618261914241/ship:addr/1472196211487/Put/vlen=7/seqid=0, Value: chengdu
 Cell: 224382618261914241/ship:email/1472196211530/Put/vlen=11/seqid=0, Value: qq@sina.com
 Result of ColumnPageFilter get:
 Cell: 224382618261914241/info:name/1472196211088/Put/vlen=4/seqid=0, Value: lisi
 Cell: 224382618261914241/info:phone/1472196211427/Put/vlen=11/seqid=0, Value: 13213921424
 Cell: 224382618261914241/info:weight/1472196211386/Put/vlen=3/seqid=0, Value: 128
 Result of get:
 Cell: 224382618261914241/info:age/1472196211169/Put/vlen=2/seqid=0, Value: 24
 Cell: 224382618261914241/info:height/1472196211234/Put/vlen=3/seqid=0, Value: 158
 Cell: 224382618261914241/info:name/1472196211088/Put/vlen=4/seqid=0, Value: lisi
 Cell: 224382618261914241/info:phone/1472196211427/Put/vlen=11/seqid=0, Value: 13213921424
 Cell: 224382618261914241/info:weight/1472196211386/Put/vlen=3/seqid=0, Value: 128
 Cell: 224382618261914241/ship:addr/1472196211487/Put/vlen=7/seqid=0, Value: chengdu
 Cell: 224382618261914241/ship:email/1472196211530/Put/vlen=11/seqid=0, Value: qq@sina.com
 Cell: 224382618261914241/ship:salary/1472196211594/Put/vlen=4/seqid=0, Value: 5000
**/