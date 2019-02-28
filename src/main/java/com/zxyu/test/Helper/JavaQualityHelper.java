package com.zxyu.test.Helper;

import java.io.File;

public class JavaQualityHelper {
    private String dir;
    private static final String rulePath="";

    public JavaQualityHelper(String java_dir){
        this.dir=java_dir;
    }

    private void calJavaQuality(){
        File file=new File(dir);
        File outDir=file.getParentFile().getParentFile().listFiles()[1].listFiles()[0];
        System.out.println(outDir.getName());
        File[]javaList=file.listFiles();
        String s=file.getName();
        System.out.println(s);
        String fileName;
        String sid;
        for(int i=0;i<javaList.length;i++){
            if(javaList[i].isDirectory())
                continue;
            fileName=javaList[i].getName();
            sid=fileName.substring(0,fileName.lastIndexOf("."));
            System.out.println(sid);

        }
    }

    public static void main(String args[]){
        JavaQualityHelper helper=new JavaQualityHelper("I:\\src_text\\1\\java");
        helper.calJavaQuality();
    }
}
