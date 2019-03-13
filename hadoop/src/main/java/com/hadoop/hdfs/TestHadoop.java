package com.hadoop.hdfs;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import javax.xml.ws.soap.AddressingFeature;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class TestHadoop {
    public static void main(String[] args){

       try {
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(conf);
            //InputStream in = fs.open(new Path("/user/centos/hadoop/hello.txt"));
            fs.copyFromLocalFile(true,new Path("G:\\mr\\1.seq"), new Path("/user/test0/1.seq"));
            //fs.mkdirs(new Path("/user/test"));
            //byte[] buf = new byte[in.available()];
            //in.read(buf);
            //in.close();
            //System.out.println(new String(buf));
        }catch (Exception e){
            e.printStackTrace();
        }

/*         try {
            Configuration conf = new Configuration();
            FileSystem fs = FileSystem.get(conf);
            OutputStream out = fs.create(new Path("hdfs://s110/user/centos/hello1.txt"),
                    true,1024,(short) 2,1024*1024);
            InputStream inputStream = new ByteArrayInputStream("hello world".getBytes());
            IOUtils.copyBytes(inputStream,out,1024);
            inputStream.close();
            out.close();
        }catch (Exception e){
            e.printStackTrace();
        }*/
    }
}
