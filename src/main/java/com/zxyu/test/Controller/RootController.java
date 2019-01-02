package com.zxyu.test.Controller;

import com.zxyu.test.Dao.FileDao;
import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.Entity.AssignmentEntity;
import com.zxyu.test.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Controller
@EnableAutoConfiguration
@RequestMapping("/root")
public class RootController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private FileDao fileDao;

    @RequestMapping(value = "/course", method = RequestMethod.GET)
    public ModelAndView getCourse() {
        ModelAndView mv = new ModelAndView("/root/r-course");
        mv.addObject("sid", "root");
        return mv;
    }


    @RequestMapping(value = "/publishAssignment", method = RequestMethod.GET)
    public ModelAndView getPublishAssignment() {
        ModelAndView mv = new ModelAndView("/root/publishAssignment");
        mv.addObject("sid", "root");
        return mv;
    }


    @RequestMapping(value = "/manageUser", method = RequestMethod.GET)
    public ModelAndView getManageUser() {
        ModelAndView mv = new ModelAndView("/root/manageUser");
        mv.addObject("sid", "root");
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/publishAssignment", method = RequestMethod.POST)
    public void publishAssignment(@RequestParam("title") String title,
                                    @RequestParam("courseType") String courseType,
                                    @RequestParam("description") String description,
                                    @RequestParam("deadline") String deadline,
                                    @RequestParam("lateTime") String lateTime,
                                    @RequestParam("latePercent") String latePercent,
                                    @RequestParam("detailedFile") MultipartFile detailedFile,
                                    @RequestParam("testFile") MultipartFile testFile,
                                    HttpServletRequest request,
                                    HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter out = null;

        if(title.equals("")||description.equals("")||deadline.equals("")){
            try {
                out = response.getWriter();
                out.print("<script>alert('Please complete the form! ');" +
                        "window.location.href='#';</script>");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String deadlineD = deadline + ";" + lateTime + ";" + latePercent;


        String url = "/src/main/java/com/zxyu/test/jars/" + courseType + "/" + title;
        boolean detailedResult = saveFile(url, detailedFile);
        boolean testResult = saveFile(url, testFile);

        if (!detailedResult) {
            //alert提醒
            try {
                out = response.getWriter();
                out.print("<script>alert('Detailed file upload fails! ');" +
                        "window.location.href='#';</script>");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!testResult) {
            try {
                out = response.getWriter();
                out.print("<script>alert('Test file upload fails! ');" +
                        "window.location.href='#';</script>");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String detailedFileUrl = url + "/" + detailedFile.getOriginalFilename();
        String testFileUrl = url + "/" + testFile.getOriginalFilename();

        AssignmentEntity assignment = new AssignmentEntity(title, courseType, description, deadlineD, detailedFileUrl, testFileUrl);
        userDao.addAssignment(assignment);

        try {
            out = response.getWriter();
            out.print("<script> window.location.href='#';</script>");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return "redirect:/root/course";
    }

    public boolean saveFile(String url, MultipartFile file) {
        String tempUrl = System.getProperty("user.dir");

        if (file.isEmpty()) {
            return false;
        }
        String fileName = file.getOriginalFilename();
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


}
