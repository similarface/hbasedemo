import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 尝试获取数据使用错误的列族
 */
public class GetDataWithErrorColFamily {
    public static void main(String args[]) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("testtable"));
        List<Get> gets = new ArrayList<Get>();
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
        Get get1 = new Get(row1);
        get1.addColumn(cf1,qf1);
        gets.add(get1);

        Get get2 = new Get(row2);
        get2.addColumn(cf1,qf1);
        gets.add(get2);

        Get get3 = new Get(row2);
        get3.addColumn(cf1,qf2);
        gets.add(get3);

        Get get4 = new Get(row2);
        //这个列族根本不存在
        get4.addColumn(Bytes.toBytes("BOGUS"), qf2);
        gets.add(get4);

        //抛出异常 进程中断
        //Exception in thread "main" org.apache.hadoop.hbase.client.RetriesExhaustedWithDetailsException: Failed 1 action: org.apache.hadoop.hbase.regionserver.NoSuchColumnFamilyException: Column family BOGUS does not exist in region testtable,,1470907049676.32bcdb9d8df5829ae7eda1ae06cc9dc0. in table 'testtable', {NAME => 'colfam1', DATA_BLOCK_ENCODING => 'NONE', BLOOMFILTER => 'ROW', REPLICATION_SCOPE => '0', COMPRESSION => 'NONE', VERSIONS => '1', TTL => 'FOREVER', MIN_VERSIONS => '0', KEEP_DELETED_CELLS => 'FALSE', BLOCKSIZE => '65536', IN_MEMORY => 'false', BLOCKCACHE => 'true'}
        Result[] results = table.get(gets);
        //下面的打印到达并了
        System.out.println("Result count: " + results.length);
    }
}
