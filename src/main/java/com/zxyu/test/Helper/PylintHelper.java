package com.zxyu.test.Helper;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class PylintHelper {
    private String srcPath;
    private String command;
    private String confPath;
    private String outPath;
    private static final int NUM=10;

    public PylintHelper(String srcPath, String confPath,String outPath) {
        this.srcPath = srcPath;
        this.confPath = confPath;
        this.outPath=outPath;
        this.command="pylint --rcfile="+confPath+" "+srcPath;
    }

    public String getOutPath() {
        return outPath;
    }

    public void setOutPath(String outPath) {
        this.outPath = outPath;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getConfPath() {
        return confPath;
    }

    public void setConfPath(String confPath) {
        this.confPath = confPath;
    }

    public void execPylint(){
        String[]input=new String[3];
        input[0]=command;
        input[1]="pylint";
        input[2]=outPath;
        try {
            LocalCmd.main(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getScore() {
        File file = new File(outPath);
        int result = 0;
        Set<String> set=new HashSet<String>();
        if (file.exists()) {
            try {
                InputStreamReader reader = new InputStreamReader(new FileInputStream(file));
                BufferedReader br = new BufferedReader(reader);
                String line = null;

                while ((line = br.readLine()) != null) {
                    //System.out.println(line);
                    if(line.equals(""))
                        continue;
                    char[]chars=line.toCharArray();
                    if(chars[chars.length-1]==')'){
                        for(int i=chars.length-2;i>=0;i--){
                            if(chars[i]=='('){
                                set.add(line.substring(i+1,chars.length-1));
                                break;
                            }

                        }
                    }

                }
                reader.close();
                br.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(set.size()<=NUM)
            result=NUM+1-set.size();

        return result;
    }



    public static void main(String args[]){
        PylintHelper helper=new PylintHelper("i:\\testpy.py","C:\\Users\\zxyu\\pylint.conf","i:\\pyout.txt");
        helper.execPylint();
        System.out.println(helper.getScore());
    }
}
