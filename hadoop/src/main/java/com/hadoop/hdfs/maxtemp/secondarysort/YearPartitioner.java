package com.hadoop.hdfs.maxtemp.secondarysort;

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class YearPartitioner extends Partitioner<ComboKey,NullWritable> {
    public int getPartition(ComboKey comboKey, NullWritable nullWritable, int numPartitions) {
        int year = comboKey.getYear();
        return year % numPartitions;
    }
}
