package com.hadoop.hdfs.compress;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IOUtils;
import org.apache.hadoop.io.compress.*;
import org.apache.hadoop.util.ReflectionUtils;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class TestCompress {
    public static void main(String[] args){
        Class[] zipClass = {
                DeflateCodec.class,
                GzipCodec.class,
                BZip2Codec.class,
                Lz4Codec.class,
                SnappyCodec.class
        };
        for (Class clazz: zipClass
             ) {
            try {
                zip(clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static void zip(Class codecClass) throws Exception{

        long start = System.currentTimeMillis();
        CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, new Configuration());
        FileOutputStream fos = new FileOutputStream("/home/centos/zip/b" + codec.getDefaultExtension());
        CompressionOutputStream zipOut = codec.createOutputStream(fos);
        IOUtils.copyBytes(new FileInputStream("/home/centos/zip/a.txt"), zipOut, 1024);
        zipOut.close();
        System.out.println(codecClass.getName() + ":" + (System.currentTimeMillis()-start));
    }


    public static void unZip(Class codecClass) throws Exception{

        long start = System.currentTimeMillis();
        CompressionCodec codec = (CompressionCodec) ReflectionUtils.newInstance(codecClass, new Configuration());
        FileInputStream fis = new FileInputStream("/home/centos/zip/b" + codec.getDefaultExtension());
        CompressionInputStream zipIn = codec.createInputStream(fis);
        IOUtils.copyBytes(zipIn, new FileOutputStream("/home/centos/zip/b.txt"), 1024);
        zipIn.close();
        System.out.println(codecClass.getName() + ":" + (System.currentTimeMillis()-start));
    }
}
