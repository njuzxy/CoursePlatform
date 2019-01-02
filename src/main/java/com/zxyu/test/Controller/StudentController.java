package com.zxyu.test.Controller;

import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.Entity.AssignmentEntity;
import com.zxyu.test.Entity.SubmitEntity;
import com.zxyu.test.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@EnableAutoConfiguration
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private UserDao userDao;

    @RequestMapping(value = "/{sid}/course", method = RequestMethod.GET)
    public ModelAndView getCourse(@PathVariable String sid) {
        UserEntity student = userDao.findUser(sid);
        ModelAndView mv = new ModelAndView("/student/s-course");
        mv.addObject("student", student);
        mv.addObject("sid", sid);

        return mv;
    }

    @RequestMapping(value = "/{sid}/assignment", method = RequestMethod.GET)
    public ModelAndView getAssignment(@PathVariable String sid) {
        ModelAndView mv = new ModelAndView("/student/courseInfo");

        return mv;
    }

    @RequestMapping(value = "/{sid}/assignmentInfo", method = RequestMethod.GET)
    public ModelAndView getAssignmentInfo(@PathVariable String sid) {
        ModelAndView mv = new ModelAndView("/student/s-assignmentInfo");
        return mv;
    }


    @ResponseBody
    @RequestMapping(value = "/submitAssignment", method = RequestMethod.POST)
    public void submitAssignment(@RequestParam("fileType") String fileType,
                                 @RequestParam("submitFile") MultipartFile submitFile,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter out = null;

        System.out.println("---------------------------------fileType : " + fileType);

        String courseType = "Cloud";
        String title = "assignment01";

        String url = "/src/main/java/com/zxyu/test/jars/" + courseType + "/" + title + "/submit/151250199";
        boolean submitResult = saveFile(url, submitFile);

        if (!submitResult) {
            //alert提醒
            try {
                out = response.getWriter();
                out.print("<script>alert('Submit file upload fails! ');" +
                        "window.location.href='#';</script>");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String submitFileUrl = url + "/" + submitFile.getOriginalFilename();
        Date date = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SubmitEntity submit = new SubmitEntity("151250199", 0, fileType, "submitted", submitFileUrl, "", "", "", date);
        userDao.addSubmit(submit);
        System.out.println("---------------------------------submit");

        try {
            out = response.getWriter();
            out.print("<script> window.location.href='/student/151250199/assignment';</script>");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        return "redirect:/student/151250199/assignment";
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