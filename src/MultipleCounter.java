import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;

/**
 * 复合计数器
 * similarface
 * similarface@outlook.com
 */
public class MultipleCounter {
    public static void main(String args[]) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf("counters"));
        Increment increment1 = new Increment(Bytes.toBytes("20160101"));
        increment1.addColumn(Bytes.toBytes("daily"),Bytes.toBytes("clicks"),1);
        increment1.addColumn(Bytes.toBytes("daily"),Bytes.toBytes("hits"),1);
        increment1.addColumn(Bytes.toBytes("weekly"),Bytes.toBytes("clicks"),10);
        increment1.addColumn(Bytes.toBytes("weekly"),Bytes.toBytes("hits"),10);

        Result result = table.increment(increment1);
        for(Cell cell:result.rawCells()){
            System.out.println("Cell: " + cell +
                    " Value: " + Bytes.toLong(cell.getValueArray(), cell.getValueOffset(),cell.getValueLength()));
        }

        Increment increment2 = new Increment(Bytes.toBytes("20160101"));
        increment2.addColumn(Bytes.toBytes("daily"),Bytes.toBytes("clicks"), 5);
        increment2.addColumn(Bytes.toBytes("daily"),Bytes.toBytes("hits"), 1);
        increment2.addColumn(Bytes.toBytes("weekly"),Bytes.toBytes("clicks"), 0);
        increment2.addColumn(Bytes.toBytes("weekly"),Bytes.toBytes("hits"), -5);
        Result result2 = table.increment(increment2);
        for (Cell cell : result2.rawCells()) {
            System.out.println("Cell: " + cell +
                    " Value: " + Bytes.toLong(cell.getValueArray(),
                    cell.getValueOffset(), cell.getValueLength()));
        }

        table.close();
        connection.close();
    }
}
/**
 Cell: 20160101/daily:clicks/1473057324875/Put/vlen=8/seqid=0 Value: 1
 Cell: 20160101/daily:hits/1473057324875/Put/vlen=8/seqid=0 Value: 1
 Cell: 20160101/weekly:clicks/1473057324875/Put/vlen=8/seqid=0 Value: 10
 Cell: 20160101/weekly:hits/1473057324875/Put/vlen=8/seqid=0 Value: 10
 Cell: 20160101/daily:clicks/1473057324886/Put/vlen=8/seqid=0 Value: 6
 Cell: 20160101/daily:hits/1473057324886/Put/vlen=8/seqid=0 Value: 2
 Cell: 20160101/weekly:clicks/1473057324886/Put/vlen=8/seqid=0 Value: 10
 Cell: 20160101/weekly:hits/1473057324886/Put/vlen=8/seqid=0 Value: 5
 **/