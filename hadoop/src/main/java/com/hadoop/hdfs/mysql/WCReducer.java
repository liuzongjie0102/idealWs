package com.hadoop.hdfs.mysql;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;


public class WCReducer extends Reducer<Text, IntWritable, MyWritable, NullWritable>{
    @Override
    protected void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        MyWritable keyOut = new MyWritable();
        keyOut.setWords(key.toString());
        int count = 0;
        for (IntWritable i : values
             ) {
            count += i.get();
        }
        keyOut.setNum(count);
        context.write(keyOut, NullWritable.get());
    }
}
