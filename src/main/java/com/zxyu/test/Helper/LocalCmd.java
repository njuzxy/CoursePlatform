package com.zxyu.test.Helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LocalCmd {
    public static void main(String args[])throws Exception{
        String cmdStr="python i:/hello.py";

        Process process= Runtime.getRuntime().exec("cmd /k "+cmdStr);
        InputStream inputStream = process.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader br = new BufferedReader(inputStreamReader);
        String line = null;
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

    }
}
