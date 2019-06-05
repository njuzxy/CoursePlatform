package com.zxyu.test.Helper;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

public class PmdHelper {
    private static  final int ruleNum=10;
    private static DocumentBuilderFactory factory=null;
    private static DocumentBuilder db=null;
    private static Document document=null;
    private String command;
    private String srcPath;
    private String outputPath;
    private String rulePath;

    static{
        try{
            factory=DocumentBuilderFactory.newInstance();
            db=factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public PmdHelper(String srcPath,String outputPath,String rulePath) {
        this.srcPath=srcPath;
        this.outputPath=outputPath;
        this.rulePath=rulePath;
        this.command="pmd -d "+srcPath+" -f xml "+"-R "+rulePath+" -reportfile "+outputPath;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getSrcPath() {
        return srcPath;
    }

    public void setSrcPath(String srcPath) {
        this.srcPath = srcPath;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public void execPmd(){
        String[]input=new String[2];
        input[0]=command;
        input[1]="pmd";
        System.out.println("command:"+command);
        try {
            LocalCmd.main(input);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getScore(){
        Set<String> ruleSet=new HashSet<String>() ;
        try {
            //document=db.parse(outputPath);
            BufferedReader br=new BufferedReader(new InputStreamReader(new FileInputStream(outputPath),"utf-8"));
            InputSource is=new InputSource(br);
            document=db.parse(is);
            NodeList violationList=document.getElementsByTagName("violation");
            Node node;
            String rule="";
            for(int i=0;i<violationList.getLength();i++){
                node=violationList.item(i);
                NamedNodeMap map=node.getAttributes();
                rule=map.getNamedItem("rule").getTextContent();
                ruleSet.add(rule);
            }
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ruleNum-ruleSet.size();
    }

    public static void main(String[]args){
        PmdHelper pmdHelper=new PmdHelper("i:\\MyLauncher.java","i:\\output.xml","c:\\Users\\zxyu\\Desktop\\rule.xml");
        pmdHelper.execPmd();
        System.out.println(pmdHelper.getScore());
    }

}
