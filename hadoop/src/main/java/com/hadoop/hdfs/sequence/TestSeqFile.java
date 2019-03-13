package com.hadoop.hdfs.sequence;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

import java.io.IOException;

public class TestSeqFile {
    public static void main(String[] args) throws Exception {
        write();
        read();
    }

    public static void write() throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","file:///");
        FileSystem fs = FileSystem.get(conf);
        Path path = new Path("G:/mr/1.seq");
        SequenceFile.Writer writer = SequenceFile.createWriter(fs,  conf, path, IntWritable.class, Text.class);
        for (int i = 0; i < 100; i++){
            writer.append(new IntWritable(i), new Text("tom" + i));
        }
        writer.close();
    }

    public static void read() throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","file:///");
        FileSystem fs = FileSystem.get(conf);
        Path path = new Path("G:/mr/1.seq");
        SequenceFile.Reader reader = new SequenceFile.Reader(fs, path, conf);

        IntWritable key = new IntWritable();
        Text value = new Text();

        while (reader.next(key, value)){
            System.out.println(key.get() + " " + value.toString() + " " + reader.getPosition());
        }


        reader.close();
    }
}
