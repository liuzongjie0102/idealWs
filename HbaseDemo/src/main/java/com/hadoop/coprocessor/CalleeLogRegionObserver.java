package com.hadoop.coprocessor;

import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Durability;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.coprocessor.BaseRegionObserver;
import org.apache.hadoop.hbase.coprocessor.ObserverContext;
import org.apache.hadoop.hbase.coprocessor.RegionCoprocessorEnvironment;
import org.apache.hadoop.hbase.regionserver.wal.WALEdit;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;

public class CalleeLogRegionObserver extends BaseRegionObserver {

    @Override
    public void postPut(ObserverContext<RegionCoprocessorEnvironment> e, Put put, WALEdit edit, Durability durability) throws IOException {
        super.postPut(e, put, edit, durability);
        TableName callLong = TableName.valueOf("calllogs");
        TableName tname = e.getEnvironment().getRegion().getRegionInfo().getTable();
        if (!callLong.equals(tname)){
            return;
        }


        /**
         * 得到主叫的rowkey
         * xx, callerid, time, direction, calleeid, duration
         * 被叫：calleeid, time
         */

        String rowkye = Bytes.toString(put.getRow());
        String[] arr = rowkye.split(",");


    }

}
