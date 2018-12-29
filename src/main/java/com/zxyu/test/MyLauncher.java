package com.zxyu.test;


import org.apache.spark.launcher.SparkAppHandle;
import org.apache.spark.launcher.SparkLauncher;




public class MyLauncher {
    public static void main(String args[]) throws Exception{
        String path=args[1];//System.getProperty("user.dir")+"\\src\\main\\java\\com\\zxyu\\test\\jars\\example.jar";
        System.out.println(path);
        SparkLauncher launcher=new SparkLauncher();
               launcher.setAppResource(path);
               if(!args[0].equals("python"))
                launcher.setMainClass("JavaSparkDemo");
               launcher.setMaster("spark://172.19.241.248:7077");
        SparkAppHandle handle=launcher.startApplication(new SparkAppHandle.Listener() {
                   @Override
                   public void stateChanged(SparkAppHandle sparkAppHandle) {
                       System.out.println("state changed");
                   }

                   @Override
                   public void infoChanged(SparkAppHandle sparkAppHandle) {
                        System.out.println("info changed");
                   }
               });
        while(!"FINISHED".equalsIgnoreCase(handle.getState().toString()) && !"FAILED".equalsIgnoreCase(handle.getState().toString())){
            System.out.println("id    "+handle.getAppId());
            System.out.println("state "+handle.getState());

            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
