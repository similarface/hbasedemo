import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 修改数据实现原子性
 *
 * 重点:
 * checkAndMutate
 */
public class UpdateDataWithAtomic {
    public static void main(String[] args) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("testtable"));
        //获取put实例
        Put put = new Put(Bytes.toBytes("10086"));
        put.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual7"),4,Bytes.toBytes("UpdateDataWithAtomic"));
        put.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual8"),4,Bytes.toBytes("UpdateDataWithAtomicTest"));
        //删除
        Delete delete = new Delete(Bytes.toBytes("10086"));
        delete.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual1"));
        //更新实例
        RowMutations mutations = new RowMutations(Bytes.toBytes("10086"));
        mutations.add(put);
        mutations.add(delete);
        //Mutate 1 successful: false
        //checkAndMutate (行键,列族,列分隔) =>比对操作<= (期望值) ====> T mutations F not mutations
        //(china mobile 1)<(val1) 为假没有进行更新
        boolean res1 = table.checkAndMutate(Bytes.toBytes("10086"), Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), CompareFilter.CompareOp.LESS, Bytes.toBytes("val1"), mutations);
        System.out.println("Mutate 1 successful: " + res1);
        //这儿插入了一行数据会导致10086-colfam1-qual1的val2比期望的val1大
        Put put2 = new Put(Bytes.toBytes("10086"));
        put2.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), 4, Bytes.toBytes("val2"));
        table.put(put2);
        //Mutate 2 successful: true
        //(val2)>(val1) 为真 进行了跟新
        boolean res2 = table.checkAndMutate(Bytes.toBytes("10086"), Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), CompareFilter.CompareOp.LESS, Bytes.toBytes("val1"), mutations);
        System.out.println("Mutate 2 successful: " + res2);
    }
}
//olddata
/**
 10086                                           column=colfam1:qual1, timestamp=4, value=china mobile 1
 10086                                           column=colfam1:qual4, timestamp=4, value=china mobile 4
 */
//newdata
/**
 10086                                           column=colfam1:qual4, timestamp=4, value=china mobile 4
 10086                                           column=colfam1:qual7, timestamp=4, value=UpdateDataWithAtomic
 10086                                           column=colfam1:qual8, timestamp=4, value=UpdateDataWithAtomicTest
**/