package com.hadoop.hdfs.maxtemp;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class MaxReducer extends Reducer<IntWritable, IntWritable, IntWritable, IntWritable>{
    @Override
    protected void reduce(IntWritable key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int max = Integer.MIN_VALUE;
        for (IntWritable a: values
             ) {
            max = max > a.get() ? max : a.get();
        }
        context.write(key, new IntWritable(max));
    }
}
