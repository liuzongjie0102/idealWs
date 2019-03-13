package com.hadoop.hdfs.hive;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class WCountApp {
    public static class WMapper extends Mapper<LongWritable, Text, Text, IntWritable> {
        @Override
        protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
            String line = value.toString();
            String[] data = line.split(" ");

            for (String s:data) {
                context.write(new Text(s), new IntWritable(1));
            }
        }
    }

    public static class WReducer extends Reducer<Text, IntWritable, Text, IntWritable> {
        @Override
        protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
            int count = 0;
            for (IntWritable value:values) {
                count++;
            }
            context.write(key,new IntWritable(count));
        }
    }

    public static void main(String[] args) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","file:///");
        try {
            String localPath = "g:/mr/temp.txt";
            String outputPath = "g:/mr/out";
            FileSystem.get(conf).delete(new Path(outputPath),true);

            Job job = Job.getInstance(conf);
            //设置job的各种属性
            job.setJobName("WCountApp");    //作业类名称
            job.setJarByClass(WCountApp.class);     //搜索类
            job.setInputFormatClass(TextInputFormat.class);     //设置输入格式

            FileInputFormat.addInputPath(job, new Path(localPath));
            FileOutputFormat.setOutputPath(job, new Path(outputPath));

            job.setMapperClass(WMapper.class);
            job.setReducerClass(WReducer.class);

            //job.setPartitionerClass(MyPartition.class);
            //job.setCombinerClass(WCReducer.class);
            //job.setNumReduceTasks(1);

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
