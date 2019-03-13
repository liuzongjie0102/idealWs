package com.hadoop.hdfs.hive;

import com.hadoop.hdfs.mr.MyPartition;
import com.hadoop.hdfs.mr.WCApp;
import com.hadoop.hdfs.mr.WCMapper;
import com.hadoop.hdfs.mr.WCReducer;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.orc.mapred.OrcStruct;
import org.apache.orc.mapreduce.OrcInputFormat;

import java.io.IOException;

public class HiveApp {

    public static class OrcMap extends Mapper<NullWritable,OrcStruct,Text,Text> {

        // Assume the ORC file has type: struct<s:string,i:int>
        public void map(NullWritable key, OrcStruct value,
                        Context output) throws IOException, InterruptedException {
            // take the first field as the key and the second field as the value
            output.write((Text) value.getFieldValue(0),
                    (Text) value.getFieldValue(1));
        }
    }

    public static void main(String[] args) {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","file:///");
        try {
            String localPath = "g:/mr/000000_0";
            String outputPath = "g:/mr/out";
            FileSystem.get(conf).delete(new Path(outputPath),true);

            Job job = Job.getInstance(conf);
            //设置job的各种属性
            job.setJobName("HiveApp");    //作业类名称
            job.setJarByClass(HiveApp.class);     //搜索类
            job.setInputFormatClass(OrcInputFormat.class);     //设置输入格式

            FileInputFormat.addInputPath(job, new Path(localPath));
            FileOutputFormat.setOutputPath(job, new Path(outputPath));

            job.setMapperClass(OrcMap.class);
            //job.setReducerClass(HReducer.class);

            //job.setPartitionerClass(MyPartition.class);
            //job.setCombinerClass(WCReducer.class);
            //job.setNumReduceTasks(1);

            //job.setMapOutputKeyClass(Text.class);
            //job.setMapOutputValueClass(IntWritable.class);

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(Text.class);
            //job.setOutputFormatClass(TextOutputFormat.class);

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
