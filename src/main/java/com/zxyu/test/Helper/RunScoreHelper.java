package com.zxyu.test.Helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class RunScoreHelper {
    private String outpath;
    private String testfile;
    private int aid;
    private List<Object[]> list;

    public RunScoreHelper(String outpath, String testfile,int aid) {
        this.outpath = outpath;
        this.testfile = testfile;
        this.aid=aid;
        list=new ArrayList<>();
    }

    private void calRunScore(){
        List<String> result=new ArrayList<>();
        File test_file=new File(testfile);
        try {
            BufferedReader br=new BufferedReader(new FileReader(test_file));
            String str=null;
            while((str=br.readLine())!=null){
                result.add(str);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        File dir=new File(outpath);
        File[]files=dir.listFiles();
        String fileName;
        String sid;
        BufferedReader reader;
        for(int i=0;i<files.length;i++){
            fileName=files[i].getName();
            sid=fileName.substring(0,fileName.lastIndexOf("."));
            try {
                reader=new BufferedReader(new FileReader(files[i]));
                String s=null;
                int j=0;
                boolean tag=true;
                while((s=reader.readLine())!=null){
                    if(j>=result.size()||!s.equals(result.get(j))){
                        tag=false;
                        break;
                    }
                    j++;
                }
                if(j!=result.size())
                    tag=false;
                reader.close();
                Object[]score=new Object[3];
                score[0]=aid;
                score[1]=sid;
                if(tag)
                    score[2]=10;
                else
                    score[2]=0;
                list.add(score);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<Object[]> getList() {
        return list;
    }

    public static void main(String args[]){
        RunScoreHelper helper=new RunScoreHelper("g:\\test","g:\\answer.txt",1);
        helper.calRunScore();
        for(Object[] o:helper.getList())
            System.out.println(o[2]);
    }
}
