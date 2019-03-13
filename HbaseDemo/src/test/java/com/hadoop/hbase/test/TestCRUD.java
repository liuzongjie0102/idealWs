package com.hadoop.hbase.test;

import com.hadoop.util.Utils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

import java.text.DecimalFormat;
import java.util.*;

public class TestCRUD {

    @Test
    public void put() throws Exception{
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t1");

        Table table = conn.getTable(tname);

        Put put = new Put("row2".getBytes());
        byte[] f = Bytes.toBytes("f1");
        byte[] id = Bytes.toBytes("id");
        byte[] value = Bytes.toBytes(100);
        put.addColumn(f,id,value);

        table.put(put);
    }

    @Test
    public void putCallLog() throws Exception{

        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:calllog");

        String msg = "18301589111,13341109504,20160516 004827,490";
        String[] list = msg.split(",");
        String caller = list[0];
        String callee = list[1];
        String calldate = list[2];
        String date = calldate.substring(0,6);
        String calldur = list[3];

        Table table = conn.getTable(tname);
        String rowkey = Utils.hashCode(caller,date) + "," + caller + "," + calldate + ",0," + callee;
        Put put = new Put(Bytes.toBytes(rowkey));
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("caller"), Bytes.toBytes(caller));
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("callee"), Bytes.toBytes(callee));
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("calldate"), Bytes.toBytes(calldate));
        put.addColumn(Bytes.toBytes("f1"), Bytes.toBytes("calldur"), Bytes.toBytes(calldur));
        table.put(put);

    }

    @Test
    public void puts() throws Exception{

        long start = System.currentTimeMillis();
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t3");

        HTable table = (HTable) conn.getTable(tname);
        table.setAutoFlush(false);

        DecimalFormat format = new DecimalFormat("00");

        for (int i = 1; i < 100; i++){
            Put put = new Put(Bytes.toBytes("row" + format.format(i)));
            put.setWriteToWAL(false);
            put.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("id"),Bytes.toBytes(i));
            put.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("name"),Bytes.toBytes("tom" + i));
            put.addColumn(Bytes.toBytes("f1"),Bytes.toBytes("age"),Bytes.toBytes(i % 100+""));
            table.put(put);
            if(i % 2000 == 0){
                table.flushCommits();
            }
        }

        table.flushCommits();
        System.out.println(System.currentTimeMillis() - start );
    }

    @Test
    public void get() throws Exception {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);

        TableName tname = TableName.valueOf("ns1:t1");
        Table table = conn.getTable(tname);

        Get get = new Get("row2".getBytes());
        Result r = table.get(get);
        byte[] value = r.getValue("f1".getBytes(),"id".getBytes());

        System.out.println(Bytes.toInt(value));
    }

    @Test
    public void getWithVersions() throws Exception {
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);

        TableName tname = TableName.valueOf("ns1:t2");
        Table table = conn.getTable(tname);

        Get get = new Get("row1".getBytes());
        get.setMaxVersions();
        Result r = table.get(get);

        List<Cell> list = r.getColumnCells(Bytes.toBytes("f1"), Bytes.toBytes("id"));
        for (Cell c : list) {
            String f = Bytes.toString(c.getFamily());
            String col = Bytes.toString(c.getQualifier());
            long ts = c.getTimestamp();
            String value = Bytes.toString(c.getValue());
            
            System.out.println(f + ":" + col + ":" + ts + ":" + value);
        }

    }

    @Test
    public void getScan() throws Exception{
        long start = System.currentTimeMillis();
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf(Bytes.toBytes("ns1:t1"));
        Table table = conn.getTable(tname);

        Scan scan = new Scan();
        scan.setCaching(50);
        ResultScanner rs = table.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()){
            Result r = it.next();
            Cell c = r.getColumnLatestCell(Bytes.toBytes("f1"), Bytes.toBytes("id"));
            String f = Bytes.toString(c.getFamily());
            String col = Bytes.toString(c.getQualifier());
            long ts = c.getTimestamp();
            int value = Bytes.toInt(c.getValue());

            System.out.println(f + ":" + col + ":" + ts + ":" + value);
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void getScan2() throws Exception{
        long start = System.currentTimeMillis();
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf(Bytes.toBytes("ns1:t3"));
        Table table = conn.getTable(tname);

        Scan scan = new Scan();
        scan.setCaching(50);
        ResultScanner rs = table.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()){
            Result r = it.next();
            System.out.println("========================================");
            //得到一行的所有map,key=f1,value=Map<Col,Map<Timestamp,value>>
            NavigableMap<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> map =  r.getMap();
            for(Map.Entry<byte[], NavigableMap<byte[], NavigableMap<Long, byte[]>>> entyr : map.entrySet()){
                String f = Bytes.toString(entyr.getKey());
                Map<byte[], NavigableMap<Long, byte[]>> colDataMap = entyr.getValue();
                for (Map.Entry<byte[], NavigableMap<Long, byte[]>> ets : colDataMap.entrySet()){
                    String c = Bytes.toString(ets.getKey());
                    Map<Long,byte[]> tsValueMap = ets.getValue();
                    for (Map.Entry<Long,byte[]> e : tsValueMap.entrySet()){
                        Long ts = e.getKey();
                        String value = Bytes.toString(e.getValue());
                        System.out.print(f + "/" + c + "/" + ts + "=" + value + ",");
                    }
                }
            }
            System.out.println();
        }
        System.out.println(System.currentTimeMillis() - start);
    }

    @Test
    public void testFilter() throws Exception{
        Configuration conf = HBaseConfiguration.create();
        Connection conn = ConnectionFactory.createConnection(conf);
        TableName tname = TableName.valueOf("ns1:t3");
        Table table = conn.getTable(tname);

        Scan scan = new Scan();
        //用于过滤row,less 表示显示小于该row数据
        //RowFilter filter = new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL, new BinaryComparator(Bytes.toBytes("row50")));
        //用于过滤列族,less 表示显示小于该列族数据
        //FamilyFilter filter = new FamilyFilter(CompareFilter.CompareOp.LESS, new BinaryComparator(Bytes.toBytes("f2")));
        //列过滤器，只显示该列信息
        //QualifierFilter filter = new QualifierFilter(CompareFilter.CompareOp.EQUAL, new BinaryComparator(Bytes.toBytes("name")));
        //过滤value的值，含有指定的字符子串
        //ValueFilter filter = new ValueFilter(CompareFilter.CompareOp.EQUAL, new SubstringComparator("1"));

        //没说明，不会用
        /*
        DependentColumnFilter filter = new DependentColumnFilter(Bytes.toBytes("f1"),
                Bytes.toBytes("name"),
                true,
                CompareFilter.CompareOp.EQUAL,
                new BinaryComparator(Bytes.toBytes("tom1"))
        );*/

        SingleColumnValueFilter filter = new SingleColumnValueFilter(Bytes.toBytes("f1"),
                Bytes.toBytes("name"),
                CompareFilter.CompareOp.EQUAL,
                Bytes.toBytes("tom2"));

        scan.setFilter(filter);

        ResultScanner rs = table.getScanner(scan);
        Iterator<Result> it = rs.iterator();
        while (it.hasNext()){
            Result r = it.next();
            Cell c = r.getColumnLatestCell(Bytes.toBytes("f1"), Bytes.toBytes("name"));
            System.out.print(c);
            System.out.print(",value=");
            System.out.print(Bytes.toString(c.getValue()));
            System.out.println();
        }
    }

    /*复杂查询
    FilterList ft = new FilterList(FilterList.Operator.MUST_PASS_ALL);
    FilterList fall = new FilterList(FilterList.Operator.MUST_PASS_ONE);
    ft.addFilter(ftl);
    ft.addFilter(ftr);
     */



    //计数器
}
