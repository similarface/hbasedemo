import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 给定一系列的Get实例返回数据集
 *
 */
public class GetDataWithListGet {
    public static void main(String args[]) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("testtable"));
        //列族
        byte[] cf1 = Bytes.toBytes("colfam1");
        //列限定符
        byte[] qf1 = Bytes.toBytes("qual1");
        //列限定符
        byte[] qf2 = Bytes.toBytes("qual2");
        //行键
        byte[] row1 = Bytes.toBytes("10010");
        //行键
        byte[] row2 = Bytes.toBytes("10086");
        List<Get> gets = new ArrayList<Get>();
        Get get1 = new Get(row1);
        get1.addColumn(cf1, qf1);
        gets.add(get1);
        Get get2 = new Get(row2);
        get2.addColumn(cf1, qf1);
        gets.add(get2);
        Get get3 = new Get(row2);
        get3.addColumn(cf1, qf2);
        gets.add(get3);

        Result[] results = table.get(gets);

        System.out.println("First iteration...");
        for (Result result : results) {
            String row = Bytes.toString(result.getRow());
            System.out.print("Row: " + row + " ");
            byte[] val = null;
            //包含列族colfam1和列分隔符qual1的
            if (result.containsColumn(cf1, qf1)) {
                val = result.getValue(cf1, qf1);
                System.out.println("Value: " + Bytes.toString(val));
            }
            //包含列族colfam1和列分隔符qual2的
            if (result.containsColumn(cf1, qf2)) {
                val = result.getValue(cf1, qf2);
                System.out.println("Value: " + Bytes.toString(val));
            }
        }
        System.out.println("\nSecond iteration...");
        for (Result result : results) {
            if (!result.isEmpty()) {
                for (Cell cell : result.listCells()) {
                    System.out.println("Row: " + Bytes.toString(cell.getRowArray(), cell.getRowOffset(), cell.getRowLength()) + " Value: " + Bytes.toString(CellUtil.cloneValue(cell)));
                }
            }
        }
        System.out.println("Third iteration...");
        for (Result result : results) {
            System.out.println(result);
        }
    }
}

/**
result:
 First iteration...
 Row: 10010 Value: 中国联通
 Row: 10086 Value: 中国移动
 Row: null
 Second iteration...
 Row: 10010 Value: 中国联通
 Row: 10086 Value: 中国移动
 Third iteration...
 keyvalues={10010/colfam1:qual1/1471836722159/Put/vlen=12/seqid=0}
 keyvalues={10086/colfam1:qual1/1471836722159/Put/vlen=12/seqid=0}
 keyvalues=NONE
**/