package com.hadoop.hdfs.mr;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WCApp {
    public static void main(String[] args){
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","file:///");
        try {
            FileSystem.get(conf).delete(new Path(args[1]),true);

            Job job = Job.getInstance(conf);
            //设置job的各种属性
            job.setJobName("WCApp");    //作业类名称
            job.setJarByClass(WCApp.class);     //搜索类
            job.setInputFormatClass(TextInputFormat.class);     //设置输入格式

            FileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            job.setMapperClass(WCMapper.class);
            job.setReducerClass(WCReducer.class);

            job.setPartitionerClass(MyPartition.class);
            job.setCombinerClass(WCReducer.class);
            job.setNumReduceTasks(1);

            job.setMapOutputKeyClass(Text.class);
            job.setMapOutputValueClass(IntWritable.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);

            job.waitForCompletion(false);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
