/**
 * Created by similarface on 16/8/16.
 */

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;

import java.util.List;
import java.util.ArrayList;

/**
 * 批量插入的时候 如果其中一行有问题 该行实效其余的会入库
 */
public class PutDataWithAtomic {
    public static void main(String[] args) throws IOException {
        //获取陪着参数
        Configuration config = HBaseConfiguration.create();
        //建立连接
        Connection connection = ConnectionFactory.createConnection(config);

        //连接表 获取表对象
        Table table = connection.getTable(TableName.valueOf("testtable"));
        //创建Put实例
        Put put1 = new Put(Bytes.toBytes("10000"));
        put1.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("company"), Bytes.toBytes("dianxin"));
        //校验列是否存在进行插入操作
        boolean res1 = table.checkAndPut(Bytes.toBytes("10000"), Bytes.toBytes("colfam1"), Bytes.toBytes("company"), null, put1);
        //是否成功插入
        System.out.println("Put 1a applied: " + res1);
        //试图在插入同样的数据
        boolean res2 = table.checkAndPut(Bytes.toBytes("10000"), Bytes.toBytes("colfam1"), Bytes.toBytes("company"), null, put1);
        //该列已经存在所以返回了false
        System.out.println("Put 1b applied: " + res2);
        //创建另外一个put实例 但是使用不同的列标签
        Put put2 = new Put(Bytes.toBytes("10000"));
        put2.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("company2"), Bytes.toBytes("dx"));
        //存储新的数据，如果以前的数据已被保存。
        boolean res3 = table.checkAndPut(Bytes.toBytes("10000"), Bytes.toBytes("colfam1"), Bytes.toBytes("company"), Bytes.toBytes("dianxin"), put2);
        System.out.println("Put 2 applied: " + res3);
        //使用不同行
        Put put3 = new Put(Bytes.toBytes("10086"));
        put3.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("company"), Bytes.toBytes("yidong"));
        //
        boolean res4 = table.checkAndPut(Bytes.toBytes("10000"), Bytes.toBytes("colfam1"), Bytes.toBytes("company"), Bytes.toBytes("dianxin"), put3);
        //抛出异常
        System.out.println("Put 3 applied: " + res4);
        connection.close();

    }
}