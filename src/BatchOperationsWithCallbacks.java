import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

/**
 使用回调函数进行批处理操作
 */
public class BatchOperationsWithCallbacks {
    public static void main(String[] args) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("testtable"));
        List<Row> batch = new ArrayList<Row>();
        byte[] ROW= Bytes.toBytes("10000");
        byte[] ROW1=Bytes.toBytes("10000");
        byte[] ROW2=Bytes.toBytes("10000");
        byte[] COLFAM2=Bytes.toBytes("colfam1");
        byte[] COLFAM1=Bytes.toBytes("colfam1");
        byte[] QUAL1=Bytes.toBytes("company");
        byte[] QUAL2=Bytes.toBytes("company2");
        Put put = new Put(ROW2);
        put.addColumn(COLFAM2, QUAL1, 4, Bytes.toBytes("val5"));
        batch.add(put);
        Get get1 = new Get(ROW1);
        get1.addColumn(COLFAM1, QUAL1);
        batch.add(get1);
        Delete delete = new Delete(ROW1);
        delete.addColumns(COLFAM1, QUAL2);
        batch.add(delete);
        Get get2 = new Get(ROW2);
        get2.addFamily(Bytes.toBytes("BOGUS"));
        batch.add(get2);
        Object[] results = new Object[batch.size()];
        try {
            table.batchCallback(batch, results, new Batch.Callback<Result>() {
                @Override
                public void update(byte[] region, byte[] row, Result result) {
                    System.out.println("Received callback for row[" + Bytes.toString(row) + "] -> " + result);
                } });
        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
        for (int i = 0; i < results.length; i++) {
            System.out.println("Result[" + i + "]: type = " +
                    results[i].getClass().getSimpleName() + "; " + results[i]);
        }
        table.close();
        connection.close();
    }
}
