import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by similarface on 16/8/22.
 * 校验数据是否存在
 *
 */
public class CheckExistSpecificData {
    public static void main(String args[]) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        //建立表的连接
        Table table = connection.getTable(TableName.valueOf("testtable"));
        //
        List<Put> puts = new ArrayList<Put>();
        //
//        Put put1 = new Put(Bytes.toBytes("10086"));
//        put1.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("中国移动"));
//        puts.add(put1);
//
//        Put put2 = new Put(Bytes.toBytes("10010"));
//        put2.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"), Bytes.toBytes("中国联通"));
//        puts.add(put2);

        Put put3 = new Put(Bytes.toBytes("10010"));
        put3.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual3"), Bytes.toBytes("中国联通客服"));
        puts.add(put3);
        //插入两行数据
        table.put(puts);

        Get get1 = new Get(Bytes.toBytes("10010"));
        get1.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"));
        //Only check for existence of data, but do not return any of it.
        //校验数据是否存在但是不返回任何数据  ==> Get 1 Exists: true
        get1.setCheckExistenceOnly(true);
        //检查存在的第一个数据。==>Get 1 Size: 0
        Result result1 = table.get(get1);
        //这个地方val并没有值 ==>Get 1 Value: null
        byte[] val = result1.getValue(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"));
        System.out.println("Get 1 Exists: " + result1.getExists());
        System.out.println("Get 1 Size: " + result1.size());
        System.out.println("Get 1 Value: " + Bytes.toString(val));
        //获取行键
        Get get2 = new Get(Bytes.toBytes("10010"));
        //获取列族
        get2.addFamily(Bytes.toBytes("colfam1"));
        //设置校验是否存在 ==>Get 2 Exists: true
        get2.setCheckExistenceOnly(true);
        //获取数据集 ==>Get 2 Size: 0
        Result result2 = table.get(get2);

        System.out.println("Get 2 Exists: " + result2.getExists());
        System.out.println("Get 2 Size: " + result2.size());

        //获取行键
        Get get3 = new Get(Bytes.toBytes("10010"));
        //获取列族 但是错误的列限定符
        get3.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual9999"));
        //校验是否存在 ==>Get 3 Exists: false
        get3.setCheckExistenceOnly(true);
        //Get 3 Size: 0
        Result result3 = table.get(get3);

        System.out.println("Get 3 Exists: " + result3.getExists());
        System.out.println("Get 3 Size: " + result3.size());
        //获取行键100010
        Get get4 = new Get(Bytes.toBytes("10010"));
        //获取正确的列族 错误的列限定符
        get4.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual9999"));
        //获取正确的列族 正确的列限定符 ==> Get 4 Exists: true
        get4.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qual1"));
        get4.setCheckExistenceOnly(true);
        //Get 4 Size: 0
        Result result4 = table.get(get4);
        System.out.println("Get 4 Exists: " + result4.getExists());
        System.out.println("Get 4 Size: " + result4.size());
    }
}

/**
 result:
 Get 1 Exists: true
 Get 1 Size: 0
 Get 1 Value: null
 Get 2 Exists: true
 Get 2 Size: 0
 Get 3 Exists: false
 Get 3 Size: 0
 Get 4 Exists: true
 Get 4 Size: 0
 *
 */
