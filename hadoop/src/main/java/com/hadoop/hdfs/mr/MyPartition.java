package com.hadoop.hdfs.mr;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class MyPartition extends Partitioner<Text, IntWritable> {
    public int getPartition(Text text, IntWritable intWritable, int numPartitions) {
        System.out.println("MyPartition");
        return 0;
    }
}
