package com.hadoop.hbaseconsumer;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.text.DecimalFormat;
import java.util.List;

public class HbaseDao {

    private DecimalFormat df = new DecimalFormat("00");

    /**
     * rowkye结构设计
     * xx, callerid, time
     */
    public void put(String msg) throws Exception {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:calllog");
        Table table = conn.getTable(tname);
        String[] list = msg.split(",");
        String caller = list[0];
        String callee = list[1];
        String calldate = list[2];
        String date = calldate.substring(0,6);
        String calldur = list[3];

        String rowkey = hashCode(caller,date) + "," + caller + "," + calldate + ",0," + callee;
        Put put = new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("caller"), Bytes.toBytes(caller));
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("callee"), Bytes.toBytes(callee));
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("calldate"), Bytes.toBytes(calldate));
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("calldur"), Bytes.toBytes(calldur));
        table.put(put);
    }

    public String hashCode(String caller, String date){
        int hash = ((caller+date).hashCode() & Integer.MAX_VALUE) % 100;

        return df.format(hash );
    }
}
