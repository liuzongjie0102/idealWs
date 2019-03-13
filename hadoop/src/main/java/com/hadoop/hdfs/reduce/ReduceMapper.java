package com.hadoop.hdfs.reduce;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class ReduceMapper extends Mapper<LongWritable, Text, ComboKey2, NullWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        FileSplit fis = (FileSplit) context.getInputSplit();
        String path = fis.getPath().toString();
        ComboKey2 keyout = new ComboKey2();
        String info = value.toString();
        if(path.indexOf("customer") != -1){
            keyout.setType(0);
            String cid = info.substring(0, info.indexOf(','));
            keyout.setCid(Integer.valueOf(cid));
            keyout.setCustomerInfo(info);
        }else{
            String cid = info.substring(info.lastIndexOf(',') + 1);
            String orderInfo = info.substring(0, info.lastIndexOf(','));
            String oid = info.substring(0, info.indexOf(','));
            keyout.setType(1);
            keyout.setCid(Integer.valueOf(cid));
            keyout.setOid(Integer.valueOf(oid));
            keyout.setOrderInfo(orderInfo);
        }
        context.write(keyout, NullWritable.get());
    }
}
