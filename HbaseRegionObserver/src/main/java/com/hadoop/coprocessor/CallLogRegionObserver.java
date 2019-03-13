package com.hadoop.coprocessor;

import com.hadoop.util.CallLogUtils;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.InternalScanner;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CallLogRegionObserver extends BaseRegionObserver {

    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
        String tname = TableName.valueOf("ns1:calllog").getNameAsString();
        String ename = e.getEnvironment().getRegion().getRegionInfo().getTable().getNameAsString();
        if (!tname.equals(ename)){
            super.postPut(e, put, edit, durability);
        }else{

            String rowKey = Bytes.toString(put.getRow());
            String[] list = rowKey.split(",");
            String caller = list[1];
            String calldate = list[2];
            String flag = list[3];
            String callee = list[4];

            if (!flag.equals("0")) {
                super.postPut(e, put, edit, durability);
            }else {
                String date = calldate.substring(0,6);
                String hash = CallLogUtils.hashCode(callee, date);
                String outRowkey = hash + "," + callee + "," + calldate + ",1," + caller;

                Put outPut = new Put(Bytes.toBytes(outRowkey));
                outPut.addColumn(Bytes.toBytes("f2"), Bytes.toBytes("logKey"), Bytes.toBytes(rowKey));
                Table table = e.getEnvironment().getTable(TableName.valueOf(tname));
                table.put(outPut);
            }
        }
    }
    @Override
    public void postGetOp(ObserverContext<RegionCoprocessorEnvironment> e, Get get, List<Cell> results) throws IOException {
        TableName tname = TableName.valueOf("ns1:calllog");
        TableName ename = e.getEnvironment().getRegion().getRegionInfo().getTable();
        if (!tname.equals(ename)){
            super.postGetOp(e, get, results);
        }else {
            String rowKey = Bytes.toString(get.getRow());
            String[] list = rowKey.split(",");
            //String caller = list[1];
            //String calldate = list[2];
            String flag = list[3];
            //String callee = list[4];
            if (!flag.equals("1")){
                super.postGetOp(e, get, results);
            }else {
                byte[] refRowKey = CellUtil.cloneValue(results.get(0));
                Table table = e.getEnvironment().getTable(tname);
                Get outGet = new Get(refRowKey);
                Result result = table.get(outGet);
                results.clear();
                results.addAll(result.listCells());
            }

        }
    }

    @Override
    public boolean postScannerNext(ObserverContext<RegionCoprocessorEnvironment> e, InternalScanner s, List<Result> results, int limit, boolean hasMore) throws IOException {
        TableName tname = TableName.valueOf("ns1:calllog");
        TableName ename = e.getEnvironment().getRegion().getRegionInfo().getTable();
        Table table = e.getEnvironment().getTable(tname);
        boolean ret = super.postScannerNext(e, s, results, limit, hasMore);
        List<Result> newList = new ArrayList<Result>();
        if (tname.equals(ename)){
            for (Result r : results) {
                String rowKey = Bytes.toString(r.getRow());
                String flag = rowKey.split(",")[3];
                if (!flag.equals("1")){
                    newList.add(r);
                }else {
                    byte[] refRowKey = r.getValue(Bytes.toBytes("f2"), Bytes.toBytes("logKey"));
                    Get get = new Get(refRowKey);
                    newList.add(table.get(get));
                }
            }
            results.clear();
            results.addAll(newList);
        }
        return ret;
    }
}
