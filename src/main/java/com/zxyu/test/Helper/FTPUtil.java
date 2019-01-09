package com.zxyu.test.Helper;

import com.jcraft.jsch.*;
;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Vector;

public class FTPUtil {
    private static final Logger LOG = LoggerFactory.getLogger(FTPUtil.class);
    private Session session;
    private Channel channel;
    private ChannelSftp sftp;

    public FTPUtil(String host,int port,String username,String password){
        session=getSession(host,port,username,password);
        channel=getChannel(session);
        sftp=(ChannelSftp)channel;
    }

    public Channel getChannel(Session session){
                    Channel channel = null;
                     try {
                             channel = session.openChannel("sftp");
                             channel.connect();
                             LOG.info("get Channel success!");
                         } catch (JSchException e) {
                             LOG.info("get Channel fail!", e);
                         }
                     return channel;
    }
    public Session getSession(String host, int port, String username,
              final String password) {
                 Session session = null;
                 try {
                         JSch jsch = new JSch();
                         jsch.getSession(username, host, port);
                         session = jsch.getSession(username, host, port);
                         session.setPassword(password);
                         Properties sshConfig = new Properties();
                         sshConfig.put("StrictHostKeyChecking", "no");
                         session.setConfig(sshConfig);
                         session.connect();
                         LOG.info("Session connected!");
                    } catch (JSchException e) {
                         LOG.info("get Channel failed!", e);
                     }
                 return session;
    }

    public void mkdir( String dir) {
                 try {
                     Vector vector=sftp.ls(dir);
                     if(vector==null){
                         sftp.mkdir(dir);
                         System.out.println("创建文件夹成功！");
                     }else
                         System.out.println("目录已存在");

                     } catch (SftpException e) {
                         System.out.println("创建文件夹失败！");
                         e.printStackTrace();
                     }
    }

    public String uploadFile( String dir, File file) {
        String result = "";
        try {
            sftp.cd(dir);
            // File file = new File("D://34.txt"); //要上传的本地文件
            if (file != null) {
                sftp.put(new FileInputStream(file), file.getName());
                result = "上传成功！";
            } else {
                result = "文件为空！不能上传！";
            }
        } catch (Exception e) {
            LOG.info("上传失败！", e);
            result = "上传失败！";
        }
        System.out.println(result);
        return result;
    }

    private void closeChannel() {
        if (channel != null) {
            if (channel.isConnected()) {
                channel.disconnect();
            }
        }
    }

    private void closeSession() {
        if (session != null) {
            if (session.isConnected()) {
                session.disconnect();
            }
        }
    }

    public void closeAll() {
        try {
            closeChannel();
            closeChannel();
            closeSession();
        } catch (Exception e) {
            LOG.info("closeAll", e);
        }
    }

    public Session getSession() {
        return session;
    }

    public Channel getChannel() {
        return channel;
    }

    public ChannelSftp getSftp() {
        return sftp;
    }

    public static void main(String args[]){
        FTPUtil ftpUtil=new FTPUtil("172.19.241.248",22,"root","pnjueducn");
        ftpUtil.mkdir("/home/spark/file");
        ftpUtil.uploadFile("/home/spark/file",new File("i://example.py"));
        ftpUtil.closeAll();
    }



}
