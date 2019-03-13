package com.hadoop.hdfs.maxtemp.secondarysort;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;

import java.io.IOException;

public class MaxMaper extends Mapper<LongWritable, Text, ComboKey, NullWritable> {
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String[] arr = value.toString().split(" ");
        ComboKey keyout = new ComboKey();
        FileSplit fis = (FileSplit) context.getInputSplit();
        System.out.println(fis.getPath().toString());
        keyout.setYear(Integer.valueOf(arr[0]));
        keyout.setTemp(Integer.valueOf(arr[1]));
        context.write(keyout, NullWritable.get());
    }
}