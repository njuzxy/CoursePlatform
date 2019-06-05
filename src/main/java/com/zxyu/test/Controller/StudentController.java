package com.zxyu.test.Controller;

import ch.ethz.ssh2.Session;
import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.Entity.*;
import com.zxyu.test.Helper.AssistTool;
import com.zxyu.test.Helper.StateEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

import static com.zxyu.test.Helper.AssistTool.innerUrl;
import static com.zxyu.test.Helper.AssistTool.tempUrl;

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
//        System.out.println("course[0] = " + courses.get(0).getCtype());

        ModelAndView mv = new ModelAndView("/student/s-course");
        mv.addObject("student", student);
        mv.addObject("sid", sid);
        mv.addObject("courses", courses);
        return mv;
    }

    @RequestMapping(value = "/{sid}/{ctype}/assignment", method = RequestMethod.GET)
    public ModelAndView getAssignment(@PathVariable String sid, @PathVariable String ctype, HttpServletRequest request) {
        List<AssignmentEntity> assignments = userDao.findAllAssignment(ctype);
//        System.out.println("------------" + assignments.get(0).getTitle());
        ModelAndView mv = new ModelAndView("/student/s-assignment");

        HttpSession session = request.getSession();
        session.setAttribute("ctype", ctype);
        System.out.println("TEST----------------------session.ctype : " + session.getAttribute("ctype"));


        mv.addObject("sid", sid);
        mv.addObject("ctype", ctype);
        mv.addObject("assignments", assignments);
        return mv;
    }

    @RequestMapping(value = "/{sid}/{ctype}/{aid}/assignmentInfo", method = RequestMethod.GET)
    public ModelAndView getAssignmentInfo(@PathVariable String sid, @PathVariable String ctype, @PathVariable String aid, HttpServletRequest request) {
        int aidInt = Integer.parseInt(aid);
        ModelAndView mv = new ModelAndView("/student/s-assignmentInfo");
        AssignmentEntity assignment = userDao.findAssignment(aidInt);
        SubmitEntity submit = userDao.findSubmit(sid, aidInt);

        HttpSession session = request.getSession();
        session.setAttribute("aid", aid);
        session.setAttribute("ctype", ctype);
//        System.out.println("TEST----------------------session.aid : " + session.getAttribute("aid"));


        String submitState = "";
        String score = "";
        if (submit == null) {
            submitState = "Not submitted";
            score = "Not available";
        } else if (submit.getState().equals(assistTool.enum2Str(StateEnum.NotSubmitted))) {
            submitState = "Not submitted";
            score = "Not available";
        } else if (submit.getState().equals(assistTool.enum2Str(StateEnum.Submitted))) {
            submitState = "Submitted";
            score = "Waiting";
        } else if (submit.getState().equals(assistTool.enum2Str(StateEnum.Scored))) {
            submitState = "Scored";
            score = "run : " + submit.getRun_score() + "  /  quality : " + submit.getQuality_score();
        } else {
            submitState = "Scoring";
            score = "Waiting";
        }

        boolean ifCanSubmit = false;
        if (assignment.getState().equals(assistTool.enum2Str(StateEnum.Published))) {
            ifCanSubmit = true;
        }

//        System.out.println(submitState + "         " + score);
        mv.addObject("sid", sid);
        mv.addObject("assignment", assignment);
        mv.addObject("submitState", submitState);
        mv.addObject("score", score);
        mv.addObject("ifCanSubmit", ifCanSubmit);
        return mv;
    }

    @RequestMapping(value = "/{sid}/record", method = RequestMethod.GET)
    public ModelAndView getRecord(@PathVariable String sid) {
        List<SubmitEntity> submits = userDao.findSubmitsBySid(sid);
        ModelAndView mv = new ModelAndView("/student/s-record");
        mv.addObject("sid", sid);
        mv.addObject("records", submits);
        return mv;
    }

    @RequestMapping(value = "/{sid}/notice", method = RequestMethod.GET)
    public ModelAndView getNotice(@PathVariable String sid) {
        List<NoticeEntity> notices = userDao.findNoticesBySid(sid);
        ModelAndView mv = new ModelAndView("/student/s-notice");
        mv.addObject("sid", sid);
        mv.addObject("notices", notices);
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

        HttpSession session = request.getSession();
        String courseType = String.valueOf(session.getAttribute("ctype"));
        int aid = Integer.parseInt(String.valueOf(session.getAttribute("aid")));
        String sid = String.valueOf(session.getAttribute("sid"));
        if(sid.equals(null)){
            try {
                out = response.getWriter();
                out.print("<script>alert('Please login first! ');</script>");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //test
//        System.out.println(sid);

        String assignmentUrl = innerUrl + courseType + "/" + aid + "/";
        String txtUrl = assignmentUrl + "txt/";
        String submitUrl = assignmentUrl + "submit/";
        String txtFileNameS = sid + ".txt";
        String submitFileNameS = "";

        if (fileType.equals("python")) {
            txtUrl += "python/";
            submitFileNameS = sid + ".py";
        } else if (fileType.equals("java")) {
            txtUrl += "java/";
            submitFileNameS = sid + ".jar";
        }
        boolean submitResult1 = assistTool.saveFile(txtUrl, txtFile, txtFileNameS);
        boolean submitResult2 = assistTool.saveFile(submitUrl, submitFile, submitFileNameS);

        //submit失败提醒
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
        String txtFileUrl = txtUrl + txtFileNameS;
        String submitFileUrl = submitUrl + submitFileNameS;
        Date date = new Date();
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        /*
        submit多了type属性
         */
        SubmitEntity submit = new SubmitEntity(sid, aid, fileType, assistTool.enum2Str(StateEnum.Submitted), submitFileUrl, txtFileUrl, "", 0.0, 0.0, date);
        if (userDao.findSubmit(sid, aid) == null) {
            userDao.addSubmit(submit);
        } else {
            userDao.updateSubmit(submit);
        }
        System.out.println("---------------------------------submit");

        try {
            out = response.getWriter();
            out.print("<script>alert('Submit success! ');window.location.href='/student/" + sid + "/" + courseType + "/" + aid + "/assignmentInfo';</script>");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @ResponseBody
    @RequestMapping(value = "/downloadDetailedFile", method = RequestMethod.POST)
    public void downloadDetailFile(@RequestParam("urlDown") String urlDown, HttpServletRequest request, HttpServletResponse response) {
        System.out.println("*****************downloadFile");
        String fileName = "test.txt";
        String path = tempUrl + innerUrl;
//   String path = request.getSession().getServletContext().getRealPath("/");
        //文件在项目的webapp 下面
//   String fileName="guest_template.xls";   ///sstf-manager/src/main/webapp/guest_template.xls
        //设置响应头和客户端保存文件名
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        response.setHeader("Content-Disposition", "attachment;fileName=" + fileName);
        InputStream inputStream = null;
        try {
            //打开本地文件流
            inputStream = new FileInputStream(path + fileName);
            //激活下载操作
            OutputStream os = response.getOutputStream();
            //循环写入输出流
            byte[] b = new byte[2048];
            int length;
            while ((length = inputStream.read(b)) > 0) {
                os.write(b, 0, length);
            }
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
