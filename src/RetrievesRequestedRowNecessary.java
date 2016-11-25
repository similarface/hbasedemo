import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Created by similarface on 16/8/22.
 */
public class RetrievesRequestedRowNecessary {
    public static void main(String args[]) throws IOException {
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("testtable"));

        //获取行键 企图获取一个不存在的行
        Get get1 = new Get(Bytes.toBytes("95599"));
        //添加查询的列族和列限定符号
        get1.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"));
        //获取结果级
        Result result1 = table.get(get1);
        //结果集是否为空   ==> Get 1 isEmpty: true
        System.out.println("Get 1 isEmpty: " + result1.isEmpty());
        //根据result获取游标
        CellScanner scanner1 = result1.cellScanner();
        //循环遍历结果集的数据 当然是没有任何数据
        while (scanner1.advance()) {
            System.out.println("Get 1 Cell: " + scanner1.current());
        }

        //95599行键的值 数据库不存在这行数据
        Get get2 = new Get(Bytes.toBytes("95599"));
        //指定列族和列限定符
        get2.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"));
        //95599这行是在数据库中没有的 setClosestRowBefore 会试图去取前面的行如果有必要的话
        get2.setClosestRowBefore(true);
        //获取数据集 ==> Get 2 isEmpty: false
        Result result2 = table.get(get2);
        System.out.println("Get 2 isEmpty: " + result2.isEmpty());
        //继续获取游标
        CellScanner scanner2 = result2.cellScanner();
        //遍历游标获取数据         ==> Get 2 Cell: 10086/colfam1:qual1/1471836722159/Put/vlen=12/seqid=0
        while (scanner2.advance()) {
            System.out.println("Get 2 Cell: " + scanner2.current());
        }

        //这行数据库是存在的
        Get get3 = new Get(Bytes.toBytes("10010"));
        get3.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual2"));
        //依然+了setClosestRowBefore
        get3.setClosestRowBefore(true);
        //        ==> Get 3 isEmpty: false
        Result result3 = table.get(get3);
        System.out.println("Get 3 isEmpty: " + result3.isEmpty());
        CellScanner scanner3 = result3.cellScanner();
        //如果存在行还是取出的该行的数据
        while (scanner3.advance()) {
            //叫了setClosestRowBefore 的作用在这儿是列族中的所有列分隔的字段都要取出来
            // Get 3 Cell: 10010/colfam1:qual1/1471836722159/Put/vlen=12/seqid=0
            // Get 3 Cell: 10010/colfam1:qual2/1471836722159/Put/vlen=18/seqid=0
            System.out.println("Get 3 Cell: " + scanner3.current());
        }

        Get get4 = new Get(Bytes.toBytes("10086"));
        get4.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"));
        //Get 4 isEmpty: false
        Result result4 = table.get(get4);
        System.out.println("Get 4 isEmpty: " + result4.isEmpty());
        CellScanner scanner4 = result4.cellScanner();
        while (scanner4.advance()) {
            //Get 4 Cell: 10086/colfam1:qual1/1471836722159/Put/vlen=12/seqid=0
            System.out.println("Get 4 Cell: " + scanner4.current());
        }
    }
}
/**
 Get 1 isEmpty: false
 Get 1 Cell: 10086/colfam1:qual1/1471836722159/Put/vlen=12/seqid=0
 Get 2 isEmpty: false
 Get 2 Cell: 10086/colfam1:qual1/1471836722159/Put/vlen=12/seqid=0
 Get 3 isEmpty: false
 Get 3 Cell: 10010/colfam1:qual1/1471836722159/Put/vlen=12/seqid=0
 Get 3 Cell: 10010/colfam1:qual2/1471836722159/Put/vlen=18/seqid=0
 Get 4 isEmpty: false
 Get 4 Cell: 10086/colfam1:qual1/1471836722159/Put/vlen=12/seqid=0

**/