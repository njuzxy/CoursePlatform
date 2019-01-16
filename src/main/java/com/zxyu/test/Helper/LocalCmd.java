package com.zxyu.test.Helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LocalCmd {
    public static void main(String args[])throws Exception{
        String cmdStr=args[0];/*"pylint --rcfile=C:\\Users\\zxyu\\pylint.conf i:\\testpy.py";*/
        //String cmdStr="java -version";
        Process process= Runtime.getRuntime().exec("cmd /c "+cmdStr);
        InputStream inputStream = process.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(inputStreamReader);
        String line = null;
        String tmp="";
        while ((line = br.readLine()) != null) {
            System.out.println(line);
            if(!line.equals(""))
                tmp=line;
        }
        System.out.println(tmp);
        inputStream.close();
        inputStreamReader.close();
        process.waitFor();



    }
}
