package com.hadoop.hdfs.mysql;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class WCMapper extends Mapper<LongWritable, MyWritable, Text, IntWritable> {
    @Override
    protected void map(LongWritable key, MyWritable value, Context context) throws IOException, InterruptedException {
        String[] arr = value.getTxt().split(" ");
        for (String s : arr
             ) {
            context.write(new Text(s), new IntWritable(1));
        }
    }
}
