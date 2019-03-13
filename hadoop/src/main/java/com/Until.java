package com;

import org.apache.commons.math3.analysis.function.Sqrt;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.SequenceFile;
import org.apache.hadoop.io.Text;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class Until {
    public static void main(String[] args){
        Queue<Integer> queue = new PriorityQueue<Integer>();
        queue.offer(9);
        queue.offer(5);
        queue.offer(8);
        queue.offer(7);
        queue.offer(1);
        queue.offer(4);
        for (int i = 0 ; i < 10; i++){
            System.out.println(queue.poll());
        }


    }

    public static void makeText() throws IOException {
        OutputStream out = new FileOutputStream("g:/mr/temp.txt");
        int count = 0;
        while (count < 500){
            String temp = (int)(Math.random()*100 + 1900) + " " + (int)(Math.random() * 100) + "\r\n";
            out.write(temp.getBytes());
            count ++;
        }
        out.close();
    }

    public static void makeSeq() throws Exception {
        Configuration conf = new Configuration();
        conf.set("fs.defaultFS","file:///");
        FileSystem fs = FileSystem.get(conf);
        Path path = new Path("G:/mr/temp.seq");
        SequenceFile.Writer writer = SequenceFile.createWriter(fs,  conf, path, IntWritable.class, IntWritable.class);
        int count = 0;
        Random random = new Random();
        while (count < 5000){
            String temp = (int)(Math.random()*100 + 1900) + " " + (int)(Math.random() * 100) + "\r\n";
            IntWritable key = new IntWritable(random.nextInt(100) + 1970);
            IntWritable value = new IntWritable(random.nextInt(100) - 30);
            writer.append(key, value);
            count ++;
        }
        writer.close();
    }
}
