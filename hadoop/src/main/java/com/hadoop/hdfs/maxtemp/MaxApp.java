package com.hadoop.hdfs.maxtemp;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.partition.InputSampler;
import org.apache.hadoop.mapreduce.lib.partition.TotalOrderPartitioner;

public class MaxApp {
    public static void main(String[] args){
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","file:///");
        try {
            FileSystem.get(conf).delete(new Path(args[1]),true);

            Job job = Job.getInstance(conf);
            //设置job的各种属性
            job.setJobName("MaxApp");    //作业类名称
            job.setJarByClass(MaxApp.class);     //搜索类
            job.setInputFormatClass(SequenceFileInputFormat.class);     //设置输入格式

            SequenceFileInputFormat.addInputPath(job, new Path(args[0]));
            FileOutputFormat.setOutputPath(job, new Path(args[1]));

            job.setMapperClass(MaxMaper.class);
            job.setReducerClass(MaxReducer.class);

         //   job.setPartitionerClass(MyPartition.class);
         //   job.setCombinerClass(WCReducer.class);
            job.setNumReduceTasks(3);

            job.setMapOutputKeyClass(IntWritable.class);
            job.setMapOutputValueClass(IntWritable.class);

            job.setOutputKeyClass(IntWritable.class);
            job.setOutputValueClass(IntWritable.class);

            //设置全排序分区类
            job.setPartitionerClass(TotalOrderPartitioner.class);
            TotalOrderPartitioner.setPartitionFile(job.getConfiguration(), new Path("file:///g:/mr/par.lst"));

            //创建随机采样器对象
            //freq:每个key被选中的概率
            //numSapmple:抽取样本的总数
            //maxSplitSampled:最大采样切片数
            InputSampler.Sampler<IntWritable, IntWritable> sampler
                    = new InputSampler.RandomSampler<IntWritable, IntWritable>(1,5000,3);
            //将sample数据写入分区文件.
            InputSampler.writePartitionFile(job, sampler);

            job.waitForCompletion(false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
