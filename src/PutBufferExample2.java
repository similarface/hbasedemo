/**
 * Created by similarface on 16/8/16.
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 批量插入的时候 如果其中一行有问题 该行实效其余的会入库
 */
public class PutBufferExample2 {
    public static void main(String[] args) throws IOException {
        //获取陪着参数
        Configuration config = HBaseConfiguration.create();
        //建立连接
        Connection connection = ConnectionFactory.createConnection(config);
        try {
            //连接表 获取表对象
            Table t = connection.getTable(TableName.valueOf("testtable"));
            try {
                Put p = new Put(Bytes.toBytes("myrow-11111222"));
                //p.add(); 这个地方的add 是个过期的方法然而我并不知道Cell的用法是什么
                //p.add(Bytes.toBytes("colfam1"), Bytes.toBytes("name1"), Bytes.toBytes("zhangsan1"));
                p.addColumn(Bytes.toBytes("colfam1"),Bytes.toBytes("qual1sdsd00000"), Bytes.toBytes("val1100001222"));
                Put put2 = new Put(Bytes.toBytes("row21222"));
                put2.addColumn(Bytes.toBytes("colfam11"), Bytes.toBytes("qual1"),Bytes.toBytes("val221121222"));
                Put put3 = new Put(Bytes.toBytes("row31222"));
                put3.addColumn(Bytes.toBytes("colfam1"), Bytes.toBytes("qualsdsd110001"), Bytes.toBytes("val3100001222"));
                //table.put(p);
                List<Put> puts = new ArrayList<Put>();
                puts.add(p);
                puts.add(put2);
                puts.add(put3);
                try {
                    t.put(puts);
                }catch (RetriesExhaustedWithDetailsException e){
                    int numErrors=e.getNumExceptions();
                    System.out.println("Number of exceptions: "+numErrors);
                    for (int n=0;n<numErrors;n++){
                        System.out.println("Cause[" + n + "]: " + e.getCause(n)); System.out.println("Hostname[" + n + "]: " + e.getHostnamePort(n));
                        System.out.println("Row[" + n + "]: " + e.getRow(n));
                    }
                    System.out.println("Cluster issues: " + e.mayHaveClusterIssues());
                    System.out.println("Description: " + e.getExhaustiveDescription());
                }

                // Close your table and cluster connection.
                Get get=new Get(Bytes.toBytes("myrow-11111"));
                Result result=t.get(get);
                for(Cell cell:result.rawCells()){
                    System.out.print("行健: "+new String(CellUtil.cloneRow(cell)));
                    System.out.print("\t列簇: "+new String(CellUtil.cloneFamily(cell)));
                    System.out.print("\t列: "+new String(CellUtil.cloneQualifier(cell)));
                    System.out.print("\t值: "+new String(CellUtil.cloneValue(cell)));
                    System.out.println("\t时间戳: "+cell.getTimestamp());
                }
                System.out.print(">>>>end");
            } finally {
                if (t != null) t.close();
            }
        } finally {
            connection.close();
        }

    }
}