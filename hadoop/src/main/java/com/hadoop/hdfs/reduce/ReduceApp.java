package com.hadoop.hdfs.reduce;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class ReduceApp {
    public static void main(String[] args) throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","file:///");

        FileSystem.get(conf).delete(new Path("G:\\mr\\reduce\\out"),true);

        Job job = Job.getInstance(conf);
        job.setInputFormatClass(TextInputFormat.class);

        FileInputFormat.setInputPaths(job, new Path("G:\\mr\\reduce"));
        FileOutputFormat.setOutputPath(job, new Path("G:\\mr\\reduce\\out"));

        job.setMapperClass(ReduceMapper.class);
        job.setReducerClass(ReduceReducer.class);
        job.setNumReduceTasks(2);

        job.setMapOutputKeyClass(ComboKey2.class);
        job.setMapOutputValueClass(NullWritable.class);
        job.setPartitionerClass(ReducePartitioner.class);
        job.setGroupingComparatorClass(ReduceGroupComparator.class);
        job.setSortComparatorClass(ComboKey2Comparator.class);

        job.waitForCompletion(false);
    }
}
