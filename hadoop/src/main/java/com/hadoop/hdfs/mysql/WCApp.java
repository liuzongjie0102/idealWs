package com.hadoop.hdfs.mysql;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.db.DBConfiguration;
import org.apache.hadoop.mapreduce.lib.db.DBInputFormat;
import org.apache.hadoop.mapreduce.lib.db.DBOutputFormat;

public class WCApp {
    public static void main(String[] args){
        try {
            Configuration conf = new Configuration();

            Job job = Job.getInstance(conf);

            job.setJobName("MysqlJob");
            job.setJarByClass(WCApp.class);

            String url = "jdbc:mysql://192.168.230.1:3306/mydb";
            DBConfiguration.configureDB(job.getConfiguration(),"com.mysql.jdbc.Driver", url, "root", "123456");
            DBInputFormat.setInput(job, MyWritable.class, "select id,name,txt from words", "select count(*) from words");

            DBOutputFormat.setOutput(job,"status", "words", "num");
            job.setMapperClass(WCMapper.class);
            job.setReducerClass(WCReducer.class);

            job.setNumReduceTasks(3);

            job.setMapOutputKeyClass(Text.class);           //
            job.setMapOutputValueClass(IntWritable.class);  //

            job.setOutputKeyClass(Text.class);
            job.setOutputValueClass(IntWritable.class);     //


            job.waitForCompletion(true);



        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
