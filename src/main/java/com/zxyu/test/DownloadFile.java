package com.zxyu.test;


import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IOUtils;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;


public class DownloadFile {
    public static void download(String inputPath,String outPutPath) throws Exception{
        URI uri=new URI("hdfs://172.19.241.248:9000");
        Configuration conf=new Configuration();
        FileSystem fileSystem=FileSystem.get(uri,conf);
        InputStream in=fileSystem.open(new Path(inputPath));
        OutputStream out=new FileOutputStream(outPutPath);
        IOUtils.copyBytes(in, out, 4096, true);
        System.out.println("下载完成");
        fileSystem.close();
    }

    public static void main(String args[]){
        try {
            download("/root/test1/part-00000","i://abc.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
