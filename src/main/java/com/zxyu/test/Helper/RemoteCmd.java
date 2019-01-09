package com.zxyu.test.Helper;

import ch.ethz.ssh2.ChannelCondition;
import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.io.*;

public class RemoteCmd {
    private Connection connection;
    private static final Logger log = Logger.getLogger(RemoteCmd.class);
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
            if(result){
                log.info("认证成功");
            }else{
                log.info("认证失败");
            }
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
                //session.execCommand(cmd);
                session.requestPTY("bash");
                session.startShell();
                PrintWriter out = new PrintWriter(session.getStdin());
                out.println(cmd);
                out.println("exit");
                out.close();
                session.waitForCondition(ChannelCondition.CLOSED | ChannelCondition.EOF | ChannelCondition.EXIT_STATUS , 30000);
                result=ProcessStdOut(session.getStdout(),charset);
                if(StringUtils.isBlank(result)){
                    log.info("得到标准输出为空"+",执行的命令："+cmd);
                    result=ProcessStdOut(session.getStderr(),charset);
                }else{
                    log.info("执行命令成功"+",执行的命令："+cmd);
                }
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
    StringBuilder sb=new StringBuilder();
    sb.append("spark-submit --master local ");
    if(!args[0].equals("python"))
        sb.append("--class main ");
    sb.append(args[1]);
    //System.out.println(remote.exec("spark-submit --master local /home/spark/2DZ1832003.py"));
       System.out.println(remote.exec(sb.toString()));
   }
}
