import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.protobuf.generated.FilterProtos;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

/**
 * 自定义过滤器
 * similarface
 * similarface@outlook.com
 */


public class FilterOfCustomer extends FilterBase{
    @Override
    public ReturnCode filterKeyValue(Cell cell) throws IOException {
        if(CellUtil.matchingValue(cell,value)){
            filterRow=false;
        }
        return ReturnCode.INCLUDE;
    }

    //需要比对的值
    private byte[] value = null;

    private boolean filterRow=true;

    public FilterOfCustomer(){
        super();
    }

    public FilterOfCustomer(byte[] value){
        this.value=value;
    }

    //每当有新行添加时重置过滤器的标示位
    @Override
    public void reset(){
        this.filterRow=true;
    }

    @Override
    public boolean filterRow() {
        return filterRow;
    }



    public static void main(String args[]) throws IOException{
        Configuration configuration = HBaseConfiguration.create();
        Connection connection = ConnectionFactory.createConnection(configuration);
        Table table = connection.getTable(TableName.valueOf("user"));
        table.close();
        connection.close();
    }
}