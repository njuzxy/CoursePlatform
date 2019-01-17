package com.zxyu.test.Helper;

import java.io.*;

public class LocalCmd {
    public static void main(String args[])throws Exception{
        String cmdStr=args[0];/*"pylint --rcfile=C:\\Users\\zxyu\\pylint.conf i:\\testpy.py";*/
        //String cmdStr="java -version";
        Process process= Runtime.getRuntime().exec("cmd /c "+cmdStr);
        String type=args[1];
        if(type.equals("pylint")){
            String outPath=args[2];
            File output=new File(outPath);
            if(!output.getParentFile().exists())
                output.getParentFile().mkdirs();
            FileWriter writer=new FileWriter(output);
            InputStream inputStream = process.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader br = new BufferedReader(inputStreamReader);
            String line = null;
            while ((line = br.readLine()) != null) {
                writer.write(line+"\n");
            }
            writer.close();
            inputStream.close();
            inputStreamReader.close();
        }

        process.waitFor();



    }
}
