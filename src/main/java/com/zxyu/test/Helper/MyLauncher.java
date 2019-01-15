package com.zxyu.test.Helper;


import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;



import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class MyLauncher {
    public static void main(String args[]) throws Exception{
        String path=args[1];//System.getProperty("user.dir")+"\\src\\main\\java\\com\\zxyu\\test\\jars\\example.jar";
        System.out.println(path);
        CountDownLatch countDownLatch = new CountDownLatch(1);
        //HashMap env=new HashMap();
        /*env.put("JAVA_HOME","/usr/local/java/jdk1.8.0_144");
        env.put("HADOOP_CONF_DIR","/usr/local/hadoop-2.7.4/etc/hadoop");*/
        SparkLauncher launcher=new SparkLauncher();
               launcher.setAppResource(path);
               //launcher.setDeployMode("cluster");
               if(!args[0].equals("python"))
                launcher.setMainClass("JavaSparkDemo");
               launcher.setMaster("spark://172.19.241.248:7077");
               launcher.setVerbose(true);
        SparkAppHandle handle=launcher.startApplication(new SparkAppHandle.Listener() {
                   @Override
                   public void stateChanged(SparkAppHandle sparkAppHandle) {
                       if (sparkAppHandle.getState().isFinal()) {
                           countDownLatch.countDown();
                       }
                       System.out.println("state:" + sparkAppHandle.getState().toString());
                   }

                   @Override
                   public void infoChanged(SparkAppHandle sparkAppHandle) {
                       System.out.println("Info:" + sparkAppHandle.getState().toString());
                   }
               });
        /*while(!"FINISHED".equalsIgnoreCase(handle.getState().toString()) && !"FAILED".equalsIgnoreCase(handle.getState().toString())){
            System.out.println("id    "+handle.getAppId());
            System.out.println("state "+handle.getState());

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        System.out.println("The task "+path+" is executing, please wait ....");
        //线程等待任务结束
        System.out.println("current state:"+handle.getState().toString());

        boolean s=countDownLatch.await(1, TimeUnit.MINUTES);
        if(!s)
            System.out.println("任务执行超时");
        else
            System.out.println("The task is finished!");


    }
}
