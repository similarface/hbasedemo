import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Delete;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 删除数据具有原子性
 */
public class DeleteWithAtomic {
    public static void main(String args[]) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("testtable"));
        //行键 10000
        Delete delete1 = new Delete(Bytes.toBytes("10000"));
        //添加列族colfam1 和 列限定符company
        delete1.addColumns(Bytes.toBytes("colfam1"), Bytes.toBytes("company"));
        //检查如果列不存在，并执行可选的删除操作。
        boolean res1 = table.checkAndDelete(Bytes.toBytes("10000"), Bytes.toBytes("colfam1"), Bytes.toBytes("company"), null, delete1);
        //Print out the result, should be “Delete successful: false”.
        System.out.println("Delete 1 successful: " + res1);

//        //删除对象
//        Delete delete2 = new Delete(Bytes.toBytes("10000"));
//        //手工列删除检查
//        delete2.addColumns(Bytes.toBytes("colfam2"), Bytes.toBytes("company"));
//        //
//        table.delete(delete2);
//        //尝试再次删除该cell
//        boolean res2 = table.checkAndDelete(Bytes.toBytes("10000"), Bytes.toBytes("colfam2"), Bytes.toBytes("company"), null, delete1);
//        //Print out the result, should be “Delete successful: true” since the checked column now is gone.
//        System.out.println("Delete 2 successful: " + res2);
//
//        Delete delete3 = new Delete(Bytes.toBytes("10086"));
//        //Create yet another Delete instance, but using a different row.
//        //创建一个10086的删除实例
//        delete3.addFamily(Bytes.toBytes("colfam1"));
//        try{
//            //Try to delete while checking a different row.
//            //尝试删除
//            boolean res4 = table.checkAndDelete(Bytes.toBytes("10000"), Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("val1"), delete3);
//            //We will not get here as an exception is thrown beforehand!
//            //提前获取异常
//            System.out.println("Delete 3 successful: " + res4);
//        } catch (Exception e) {
//            System.err.println("Error: " + e.getMessage());
       }
}

/**
 ROW                                              COLUMN+CELL
 10000                                           column=colfam1:company, timestamp=1471492514452, value=dianxin
 10000                                           column=colfam1:company2, timestamp=1471492516716, value=dx
 10010                                           column=colfam1:qual1, timestamp=1471836722159, value=\xE4\xB8\xAD\xE5\x9B\xBD\xE8\x81\x94\xE9\x80\x9A
 10010                                           column=colfam1:qual2, timestamp=1471836722159, value=\xE4\xB8\xAD\xE5\x9B\xBD\xE8\x81\x94\xE9\x80\x9A\xE5\xAE\xA2\xE6\x9C\x8D
 10010                                           column=colfam1:qual3, timestamp=1471841524122, value=\xE4\xB8\xAD\xE5\x9B\xBD\xE8\x81\x94\xE9\x80\x9A\xE5\xAE\xA2\xE6\x9C\x8D\xE7\x94\xB5\xE8
 \xAF\x9D
 10086                                           column=colfam1:qual1, timestamp=1471836722159, value=\xE4\xB8\xAD\xE5\x9B\xBD\xE7\xA7\xBB\xE5\x8A\xA8
 110                                             column=colfam1:qual1, timestamp=1471849856718, value=val1
 110                                             column=colfam1:qual2, timestamp=1471849856718, value=val2



**/