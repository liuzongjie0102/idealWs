package com.hadoop.hdfs.reduce;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.Iterator;

public class ReduceReducer extends Reducer<ComboKey2, NullWritable, Text, NullWritable> {
    @Override
    protected void reduce(ComboKey2 key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        Iterator it = values.iterator();
        it.next();
        //System.out.println(key.getType());
        String customerInfo = key.getCustomerInfo();
        while (it.hasNext()){
            it.next();
            String outInfo = customerInfo + "," + key.getOrderInfo();
            //System.out.println("outInfo:" + outInfo);
            context.write(new Text(outInfo), NullWritable.get());
        }
    }
}
