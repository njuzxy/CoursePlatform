package com.zxyu.test.Helper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PyQualityHelper {
    private String dir;
    private static final String confPath = System.getProperty("user.dir")+"\\src\\main\\java\\com\\zxyu\\test\\Helper\\pylint.conf";
    private List<Object[]> list;

    public PyQualityHelper(String python_dir) {
        this.dir = python_dir;
        list = new ArrayList<>();
    }

    public void calPyQuality() {
        File file = new File(dir);
        File outDir = new File(file.getParent(),"pythonquality");
        int aid = Integer.parseInt(file.getParentFile().getParentFile().getName());
        File[] pythonList = file.listFiles();
        String fileName;
        String sid;
        for (int i = 0; i < pythonList.length; i++) {
            if (pythonList[i].isDirectory())
                continue;
            fileName = pythonList[i].getName();
            sid = fileName.substring(0, fileName.lastIndexOf("."));
            PylintHelper helper = new PylintHelper(dir + "\\" + fileName, confPath, outDir.getAbsolutePath() + "\\" + sid + ".txt");
            helper.execPylint();
            Object[] pythonQ = new Object[3];
            pythonQ[0] = aid;
            pythonQ[1] = sid;
            pythonQ[2] = helper.getScore();
            list.add(pythonQ);
        }

    }

    public List<Object[]> getList() {
        return list;
    }

    public static void main(String args[]){
        PyQualityHelper helper=new PyQualityHelper(System.getProperty("user.dir")+"\\src\\main\\java\\com\\zxyu\\test\\jars\\云计算\\0\\txt\\python");
        helper.calPyQuality();
        System.out.println(helper.getList().size());
        System.out.println(helper.getList().get(0)[2]);
    }

}
