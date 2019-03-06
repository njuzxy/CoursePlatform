package com.zxyu.test.Helper;



import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class JavaQualityHelper {
    private String dir;
    private static final String rulePath="c:\\Users\\zxyu\\Desktop\\rule.xml";
    private List<Object[]> list;

    public JavaQualityHelper(String java_dir){
        this.dir=java_dir;
        list=new ArrayList<>();
    }

    private void calJavaQuality(){
        File file=new File(dir);
        File outDir=file.getParentFile().listFiles()[2];
        //int aid=file.getParentFile();
        System.out.println(outDir.getAbsolutePath());

        int aid=Integer .parseInt(file.getParentFile().getParentFile().listFiles()[0].getName());
        System.out.println(aid);
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
            PmdHelper helper=new PmdHelper(dir+"\\"+fileName,outDir.getAbsolutePath()+"\\"+sid+".xml",rulePath);
            helper.execPmd();
            Object[] javaQ=new Object[3];
            javaQ[0]=aid;
            javaQ[1]=sid;
            javaQ[2]=helper.getScore();
            list.add(javaQ);
        }
    }

    public List<Object[]> getList() {
        return list;
    }

    public static void main(String args[]){
        JavaQualityHelper helper=new JavaQualityHelper("I:\\src_text\\1\\java");
        helper.calJavaQuality();
        System.out.println(helper.getList().size());
        System.out.println(helper.getList().get(0)[0]);
    }
}
