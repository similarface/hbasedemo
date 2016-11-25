import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * Hbase表状态描述
 */
public class VarCallsStatuInHBase {
    public static void main(String args[]) throws IOException{
        Configuration conf = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(conf);
        Admin admin = connection.getAdmin();
        //表的名称
        TableName tableName = TableName.valueOf("T_admin");
        //获取表描述
        HTableDescriptor desc = new HTableDescriptor(tableName);
        //获取列族描述
        HColumnDescriptor coldef = new HColumnDescriptor(Bytes.toBytes("info"));
        //加入列族
        desc.addFamily(coldef);
        //创建表
        admin.createTable(desc);

        try{
            //删除表 删除表前应该先disabled表
            admin.deleteTable(tableName);
        }catch (IOException e){
            System.err.println("Error deleting table: " + e.getMessage());
        }
        //disable表后可用删除
        admin.disableTable(tableName);
        //是否disable状态
        boolean isDisabled=admin.isTableDisabled(tableName);
        // Table is disabled: true
        System.out.println("Table is disabled: " + isDisabled);
        //表是否可用
        boolean avail1 = admin.isTableAvailable(tableName);
        // Table available: true
        System.out.println("Table available: " + avail1);
        //删除表
        admin.deleteTable(tableName);
        //表是否可用
        boolean avail2 = admin.isTableAvailable(tableName);
        //Table available: false
        System.out.println("Table available: " + avail2);
        //再次创建
        admin.createTable(desc);

        boolean isEnabled = admin.isTableEnabled(tableName);
        //Table is enabled: true*
        System.out.println("Table is enabled: " + isEnabled);
    }
}
/**
 Error deleting table: T_admin
 Table is disabled: true
 Table available: true
 Table available: false
 Table is enabled: true*
 */
