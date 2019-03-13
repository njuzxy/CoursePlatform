package com.zxyu.test.Controller;

import ch.ethz.ssh2.Session;
import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.Entity.*;
import com.zxyu.test.Helper.AssistTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

@Controller
@EnableAutoConfiguration
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private UserDao userDao;

    private AssistTool assistTool = new AssistTool();

    @RequestMapping(value = "/{sid}/course", method = RequestMethod.GET)
    public ModelAndView getCourse(@PathVariable String sid) {
        UserEntity student = userDao.findUser(sid);
        List<CourseEntity> courses = userDao.findAllCourse();

//        List<Map<String,Object>> coursesList= new ArrayList<>();
//        for(CourseEntity c:courses){
//            Map<String,Object> course = new HashMap<>();
//            course.put("ctype", c.getCtype());
//            coursesList.add(course);
//        }
        System.out.println("course[0] = " + courses.get(0).getCtype());

        ModelAndView mv = new ModelAndView("/student/s-course");
        mv.addObject("student", student);
        mv.addObject("sid", sid);
        mv.addObject("courses", courses);
        return mv;
    }

    @RequestMapping(value = "/{sid}/{ctype}/assignment", method = RequestMethod.GET)
    public ModelAndView getAssignment(@PathVariable String sid, @PathVariable String ctype) {
        List<AssignmentEntity> assignments = userDao.findAllAssignment(ctype);
        System.out.println("------------" + assignments.get(0).getTitle());
        ModelAndView mv = new ModelAndView("/student/s-assignment");
        mv.addObject("sid", sid);
        mv.addObject("ctype", ctype);
        mv.addObject("assignments", assignments);
        return mv;
    }

    @RequestMapping(value = "/{sid}/{ctype}/{aid}/assignmentInfo", method = RequestMethod.GET)
    public ModelAndView getAssignmentInfo(@PathVariable String sid, @PathVariable String ctype, @PathVariable String aid) {
        int aidInt = Integer.parseInt(aid);
        ModelAndView mv = new ModelAndView("/student/s-assignmentInfo");
        AssignmentEntity assignment = userDao.findAssignment(aidInt);
        SubmitEntity submit = userDao.findSubmit(sid, aidInt);

        String submitState = "";
        String score = "";
        if (submit == null) {
            submitState = "Not submitted";
            score = "Not available";
        } else if (submit.getState().equals("not_submitted")) {
            submitState = "Not submitted";
            score = "Not available";
        } else if (submit.getState().equals("submitted")) {
            submitState = "Submitted";
            score = "Waiting";
        } else if (submit.getState().equals("scored")) {
            submitState = "Scored";
            score = "run : " + submit.getRun_score() + "  /  quality : " + submit.getQuality_score();
        }
        System.out.println(submitState+"         "+score);
        mv.addObject("assignment", assignment);
        mv.addObject("submitState", submitState);
        mv.addObject("score", score);
        return mv;
    }

    @RequestMapping(value = "/{sid}/record", method = RequestMethod.GET)
    public ModelAndView getRecord(@PathVariable String sid) {
//        List<SubmitEntity> submits=userDao.findSubmitBySid(sid);
        ModelAndView mv = new ModelAndView("/student/s-record");
//        mv.addObject("records",submits);
        return mv;
    }

    @RequestMapping(value = "/{sid}/notice", method = RequestMethod.GET)
    public ModelAndView getNotice(@PathVariable String sid) {
//        List<NoticeEntity> notices=userDao.findNoticeBySid(sid);
        ModelAndView mv = new ModelAndView("/student/s-notice");
//        mv.addObject("notices",notices);
        return mv;
    }

    @ResponseBody
    @RequestMapping(value = "/submitAssignment", method = RequestMethod.POST)
    public void submitAssignment(@RequestParam("fileType") String fileType,
                                 @RequestParam("txtFile") MultipartFile txtFile,
                                 @RequestParam("submitFile") MultipartFile submitFile,
                                 HttpServletRequest request,
                                 HttpServletResponse response) {

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter out = null;

        System.out.println("---------------------------------fileType : " + fileType);

        String courseType = "Cloud";
        String title = "assignment01";
        String sid = "151250199";

        String assignmentUrl = "/src/main/java/com/zxyu/test/jars/course/" + courseType + "/" + title + "/";
        String txtUrl;
        String submitUrl = assignmentUrl + "submit/" + sid;
        String txtFileNameS = courseType + "_" + title + "_txt_" + sid + ".txt";
        String submitFileNameS = "";

        if (fileType.equals("python")) {
            txtUrl = assignmentUrl + "txt/python/" + sid;
            submitFileNameS = courseType + "_" + title + "_submit_" + sid + ".py";
        } else if (fileType.equals("java")) {
            txtUrl = assignmentUrl + "txt/java/" + sid;
            submitFileNameS = courseType + "_" + title + "_submit_" + sid + ".jar";

        } else {
            txtUrl = assignmentUrl + "txt/scala/" + sid;
            submitFileNameS = courseType + "_" + title + "_submit_" + sid + ".jar";

        }
        boolean submitResult1 = assistTool.saveFile(txtUrl, txtFile, txtFileNameS);
        boolean submitResult2 = assistTool.saveFile(submitUrl, submitFile, submitFileNameS);

        if (!submitResult1 || !submitResult2) {
            //alert提醒
            try {
                out = response.getWriter();
                out.print("<script>alert('Submit or TXT file upload fails! ');" +
                        "window.location.href='#';</script>");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        String txtFileUrl = txtUrl + "/" + txtFileNameS;
        String submitFileUrl = submitUrl + "/" + submitFileNameS;
        Date date = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SubmitEntity submit = new SubmitEntity("151250199", 0, fileType, "submitted", submitFileUrl, "", "", "", "", date);
        if(userDao.findSubmit(sid,0)==null){
        userDao.addSubmit(submit);
        }else{
            userDao.updateSubmit(submit);
        }
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

}
