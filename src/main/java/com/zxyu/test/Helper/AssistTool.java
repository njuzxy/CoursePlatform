package com.zxyu.test.Helper;

import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.Entity.SubmitEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AssistTool {
    public static String tempUrl = System.getProperty("user.dir");//当前用户系统目录
    public static String innerUrl = "/src/main/java/com/zxyu/test/jars/";//当前项目固定存文件目录
    @Autowired
    private UserDao userDao;

    public boolean saveFile(String url, MultipartFile file, String fileName) {
        if (file.isEmpty()) {
            return false;
        }
        int size = (int) file.getSize();
        System.out.println(fileName + "-->" + size);

        String path = tempUrl + url;

        File dest = new File(path + "/" + fileName);
        if (!dest.getParentFile().exists()) { //判断文件父目录是否存在
            dest.getParentFile().mkdirs();
            System.out.println("*test* " + dest.getParentFile());

        }
        try {
            file.transferTo(dest); //保存文件
            return true;
        } catch (IllegalStateException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean createDir(String destDirName) {
        File dir = new File(tempUrl + destDirName);
        if (dir.exists()) {
            System.out.println("创建目录" + destDirName + "失败，目标目录已经存在");
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            System.out.println("创建目录" + destDirName + "成功！");
            return true;
        } else {
            System.out.println("创建目录" + destDirName + "失败！");
            return false;
        }
    }

    public ArrayList<String> readUserFile(String pathname) {
        ArrayList<String> users = new ArrayList<>();
        //防止文件建立或读取失败，用catch捕捉错误并打印，也可以throw;
        //不关闭文件会导致资源的泄露，读写文件都同理
        //Java7的try-with-resources可以优雅关闭文件，异常时自动关闭文件；详细解读https://stackoverflow.com/a/12665271
        try (FileReader reader = new FileReader(pathname);
             BufferedReader br = new BufferedReader(reader) // 建立一个对象，它把文件内容转成计算机能读懂的语言
        ) {
            String line;
            //网友推荐更加简洁的写法
            while ((line = br.readLine()) != null) {
                // 一次读入一行数据
                users.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return users;
    }

    public String formatDate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public String enum2Str(StateEnum state) {
        return String.valueOf(state);
    }

    public void updateScore(List<Object[]> scores, int aidNow,StateEnum scoreType) {

        for (int j = 0; j < scores.size(); j++) {
            String sidNow = String.valueOf(scores.get(j)[1]);
            String scoreNow = String.valueOf(scores.get(j)[2]);
            SubmitEntity submitNow = userDao.findSubmit(sidNow, aidNow);
            //什么时候算scored？质量分要不要考虑
            submitNow.setState(enum2Str(StateEnum.Scored));
            if(scoreType.equals(StateEnum.Run)){
            submitNow.setRun_score(scoreNow);
            }else if(scoreType.equals(StateEnum.Quality)){
                submitNow.setQuality_score(scoreNow);
            }
            userDao.updateSubmit(submitNow);
        }
    }
}
