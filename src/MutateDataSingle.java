import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;

/**
 * 修改数据
 */
public class MutateDataSingle {
    public static void main(String[] args) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("testtable"));
        //获取put实例
        Put put = new Put(Bytes.toBytes("10086"));
        put.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual1"),4,Bytes.toBytes("china mobile 1"));
        put.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual4"),4,Bytes.toBytes("china mobile 4"));
        //删除
        Delete delete = new Delete(Bytes.toBytes("10086"));
        delete.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual1"));
        //更新实例
        RowMutations mutations = new RowMutations(Bytes.toBytes("10086"));
        mutations.add(put);
        mutations.add(delete);
        table.mutateRow(mutations);
    }
}
//olddata
/**
 10086                                           column=colfam1:qual1, timestamp=1471836722159, value=\xE4\xB8\xAD\xE5\x9B\xBD\xE7\xA7\xBB\xE5\x8A\xA8
 */
//newdata
/**
 10086                                           column=colfam1:qual1, timestamp=4, value=china mobile 1
 10086                                           column=colfam1:qual4, timestamp=4, value=china mobile 4
**/