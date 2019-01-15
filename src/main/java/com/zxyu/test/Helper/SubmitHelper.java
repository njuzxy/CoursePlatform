package com.zxyu.test.Helper;

import java.io.File;

public class SubmitHelper {
    private String dir;

    public SubmitHelper(String dir) {
        this.dir = dir;
    }

    public void submit(){
        File file=new File(dir);
        File[]tmplist=file.listFiles();
        System.out.println(tmplist.length);
        String fileName;
        String suffix;
        String[]input=new String[2];
        for(int i=0;i<tmplist.length;i++){
            if(tmplist[i].isDirectory())
                continue;
            fileName=tmplist[i].getName();
            suffix=fileName.substring(fileName.lastIndexOf(".")+1);
            input[1]=tmplist[i].getAbsolutePath();
            if(suffix.equals("py")){
                input[0]="python";
            }else
                input[0]="notpython";
            try {
                MyLauncher.main(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String args[]){
        SubmitHelper sub=new SubmitHelper(System.getProperty("user.dir")+"\\src\\main\\java\\com\\zxyu\\test\\jars");
        sub.submit();
    }
}
