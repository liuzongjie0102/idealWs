package com.hadoop.hdfs.maxtemp.secondarysort;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MaxReducer extends Reducer<ComboKey, NullWritable, IntWritable, IntWritable>{
    @Override
    protected void reduce(ComboKey key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {
        System.out.println("==============>reduce");
        for(NullWritable v : values){
            System.out.println(key.getYear() + " : " + key.getTemp());
        }
        context.write(new IntWritable(key.getYear()), new IntWritable(key.getTemp()));
    }
}
