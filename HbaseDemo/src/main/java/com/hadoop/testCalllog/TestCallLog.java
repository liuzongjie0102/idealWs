package com.hadoop.testCalllog;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.util.Bytes;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestCallLog {
    
    public static void main(String[] args){

        try {
            put();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * rowkye结构设计
     * 区号，主叫或被叫，时间 , 方向 主-0 被-1 ， 对方号码， 时长
     * xx, callerid, time, direction, calleeid, duration
     */
    public static void put() throws  Exception{
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:calllogs");
        Table table = conn.getTable(tname);

        String callerId = "13559185296";
        String calleeId = "13235913014";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String callTime = sdf.format(new Date());
        int duration = 100;
        DecimalFormat dff = new DecimalFormat("00000");
        String durStr = dff.format(duration);

        int hash = (calleeId + callTime.substring(0,6)).hashCode() % 100;
        DecimalFormat dft = new DecimalFormat("00");
        String regNo = dft.format(hash & Integer.MAX_VALUE);

        String rowKey = regNo + "," + callerId + "," + callTime + "," + "0," + durStr;
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("callerPos"), Bytes.toBytes("河南"));
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("calleePos"), Bytes.toBytes("河南"));

        table.put(put);

    }
}
