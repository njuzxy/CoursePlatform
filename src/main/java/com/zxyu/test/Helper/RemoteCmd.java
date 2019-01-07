package com.zxyu.test.Helper;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import java.io.*;

public class RemoteCmd {
    private Connection connection;
    private String ip;
    private String  username;
    private String password;
    private String charset="UTF-8";

    public RemoteCmd(String ip, String username, String password) {
        this.ip = ip;
        this.username = username;
        this.password = password;
    }

    private boolean login(){
        connection=new Connection(ip);
        boolean result=false;
        try {
            connection.connect();
            result=connection.authenticateWithPassword(username,password);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

   public String exec(String cmd){
        String result="";
        if(login()){
            try {
                Session session=connection.openSession();
                session.execCommand(cmd);
                result=ProcessStdOut(session.getStdout(),charset);
                connection.close();
                session.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
   }

   public String ProcessStdOut(InputStream in,String charset){
   InputStream stdout=new StreamGobbler(in);
   StringBuilder builder=new StringBuilder();
       try {
           BufferedReader br=new BufferedReader(new InputStreamReader(stdout,charset));
           String line=null;
           while((line=br.readLine())!=null){
                builder.append(line+"\n");
           }
       } catch (UnsupportedEncodingException e) {
           e.printStackTrace();
       }catch(IOException e){
           e.printStackTrace();
       }
       return builder.toString();
   }

   public static void main(String args[]){
    RemoteCmd remote=new RemoteCmd("172.19.241.248","root","pnjueducn");
    System.out.println(remote.exec("pwd"));
   }
}
