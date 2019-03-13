package com.hadoop.hdfs.maxtemp;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class MaxMaper extends Mapper<IntWritable, IntWritable, IntWritable, IntWritable> {
    @Override
    protected void map(IntWritable key, IntWritable value, Context context) throws IOException, InterruptedException {
        context.write(key,value);
    }
}