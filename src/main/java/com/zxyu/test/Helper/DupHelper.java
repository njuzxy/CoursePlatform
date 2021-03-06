package com.zxyu.test.Helper;


import com.zxyu.test.Entity.DuplicateEntity;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


public class DupHelper {

    private int aid;
    private  String OutPutparentPath;
    private  String InPutparentPath;
    private String pyInPut;
    private String javaInput;
    private String pyOutPut;
    private String javaOutPut;
    private List<DuplicateEntity> result;




     public DupHelper(String OutPutParentPath,String InputParentPath,int aid){
         this.OutPutparentPath=OutPutParentPath;
         this.InPutparentPath=InputParentPath;
         this.aid=aid;
         this.pyInPut=InPutparentPath+aid+"\\txt\\python";
         this.javaInput=InPutparentPath+aid+"\\txt\\java";
         this.pyOutPut=OutPutparentPath+aid+"\\python_dup.txt";
         this.javaOutPut=OutPutparentPath+aid+"\\java_dup.txt";
         result=new ArrayList();
    }

    public List<DuplicateEntity> getResult() {
        return result;
    }

    public void setResult(List<DuplicateEntity> result) {
        this.result = result;
    }

    public  String getOutPutparentPath() {
        return OutPutparentPath;
    }

    public  String getInPutparentPath() {
        return InPutparentPath;
    }

    public String getPyInPut() {
        return pyInPut;
    }

    public void setPyInPut(String pyInPut) {
        this.pyInPut = pyInPut;
    }

    public String getJavaInput() {
        return javaInput;
    }

    public void setJavaInput(String javaInput) {
        this.javaInput = javaInput;
    }


    public String getPyOutPut() {
        return pyOutPut;
    }

    public void setPyOutPut(String pyOutPut) {
        this.pyOutPut = pyOutPut;
    }

    public String getJavaOutPut() {
        return javaOutPut;
    }

    public void setJavaOutPut(String javaOutPut) {
        this.javaOutPut = javaOutPut;
    }




    public int getAid() {
        return aid;
    }

    public void setAid(int aid) {
        this.aid = aid;
    }



    public void calSimilarity(String outPath,String inPath){
        String[]input=new String[2];
        String cmd="sim_text -pa -t 1 -r 1 -o "+outPath+" -R "+inPath;
        //System.out.println(cmd);
        input[0]=cmd;
        input[1]="sim";
        try {
            LocalCmd.main(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cal(){
         calSimilarity(pyOutPut,pyInPut);
         calSimilarity(javaOutPut,javaInput);
    }





    public void getSimilarity(String inPath,String outPath){
         File file=new File(inPath);
         int file_num=0;
         if(file.exists()){
             File[]list=file.listFiles();
             file_num=list.length;
         }
         File out=new File(outPath);
        try {
            InputStreamReader reader=new InputStreamReader(new FileInputStream(out));
            BufferedReader br=new BufferedReader(reader);
            for(int i=0;i<file_num+2;i++)
                br.readLine();
            String line="";
            while((line=br.readLine())!=null){
                //System.out.println(line);
                String[]tmp=line.split(" ");
                String f1=tmp[0];
                String similarity=tmp[3];
                String f2=tmp[6];
                //System.out.println("f1:"+f1+"f2:"+f2+"相似度:"+similarity);
                File F1=new File(f1);
                File F2=new File(f2);
                double sim=Double.parseDouble(similarity)*1.0/100;
                String name1=F1.getName().substring(0,F1.getName().lastIndexOf("."));
                String name2=F2.getName().substring(0,F2.getName().lastIndexOf("."));
                //System.out.println("name1:"+name1+" name2:"+name2+" 相似度:"+sim);
                result.add(new DuplicateEntity(aid,name1,name2,sim));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public void getAllSimiarity(){
         getSimilarity(pyInPut,pyOutPut);
         getSimilarity(javaInput,javaOutPut);
    }

    public static void main(String args[]){
       /* DupHelper helper=new DupHelper(1);
       helper.cal();
       helper.getAllSimiarity();
       System.out.println(helper.getResult().size());*/
    }




}
