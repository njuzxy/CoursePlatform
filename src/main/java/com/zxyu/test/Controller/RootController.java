package com.zxyu.test.Controller;

import com.zxyu.test.Dao.FileDao;
import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.Entity.AssignmentEntity;
import com.zxyu.test.Entity.CourseEntity;
import com.zxyu.test.Entity.SubmitEntity;
import com.zxyu.test.Entity.UserEntity;
import com.zxyu.test.Helper.AssistTool;
import com.zxyu.test.Helper.MyLauncher;
import com.zxyu.test.Helper.StateEnum;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@EnableAutoConfiguration
@RequestMapping("/root")
public class RootController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private FileDao fileDao;

    private AssistTool assistTool = new AssistTool();

    String localpath = System.getProperty("user.dir");

    @RequestMapping(value = "/course", method = RequestMethod.GET)
    public ModelAndView getCourse() {
        List<CourseEntity> courses = userDao.findAllCourse();
        ModelAndView mv = new ModelAndView("/root/r-course");
        mv.addObject("sid", "root");
        mv.addObject("courses", courses);
        return mv;
    }

    @RequestMapping(value = "/{ctype}/assignment", method = RequestMethod.GET)
    public ModelAndView getAssignment(@PathVariable String ctype) {
        List<AssignmentEntity> assignments = userDao.findAllAssignment(ctype);
        System.out.println("------------" + assignments.get(0).getTitle());
        ModelAndView mv = new ModelAndView("/root/r-assignment");
        mv.addObject("ctype", ctype);
        mv.addObject("assignments", assignments);
        return mv;
    }

    @RequestMapping(value = "/{ctype}/{aid}/assignmentInfo", method = RequestMethod.GET)
    public ModelAndView getAssignmentInfo(@PathVariable String ctype, @PathVariable String aid) {
        int aidInt = Integer.parseInt(aid);
        ModelAndView mv = new ModelAndView("/root/r-assignmentInfo");
        AssignmentEntity assignment = userDao.findAssignment(aidInt);


        mv.addObject("assignment", assignment);

        return mv;
    }


    @RequestMapping(value = "/publishAssignment", method = RequestMethod.GET)
    public ModelAndView getPublishAssignment() {
        ModelAndView mv = new ModelAndView("/root/publishAssignment");
        mv.addObject("sid", "root");
        return mv;
    }


    @RequestMapping(value = "/figureAssignment", method = RequestMethod.GET)
    public ModelAndView getFigureAssignment() {
        ModelAndView mv = new ModelAndView("/root/figureAssignment");
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
    @RequestMapping(value = "/addNewCourse", method = RequestMethod.POST)
    public void addNewCourse(@RequestParam("ctype") String ctype,
                             HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter out = null;
        CourseEntity course = new CourseEntity(ctype);
        userDao.addCourseEntity(course);
        try {
            out = response.getWriter();
            out.print("<script>alert('Add new course success!');" +
                    "window.location.href='/root/course';</script>");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        if (title.equals("") || description.equals("") || deadline.equals("")) {
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
        String fileNameD = detailedFile.getOriginalFilename();
        String fileNameT = testFile.getOriginalFilename();
        boolean detailedResult = assistTool.saveFile(url, detailedFile, fileNameD);
        boolean testResult = assistTool.saveFile(url, testFile, fileNameT);

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

        String detailedFileUrl = url + "/" + fileNameD;
        String testFileUrl = url + "/" + fileNameT;
        int aid = userDao.findCourseNextAid();
        AssignmentEntity assignment = new AssignmentEntity(aid, title, courseType, description, deadlineD, detailedFileUrl, testFileUrl, assistTool.enum2Str(StateEnum.Published));
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

    @ResponseBody
    @RequestMapping(value = "/figureAssignments", method = RequestMethod.POST)
    public void figureAssignments(HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        String[] figureAidsStr = request.getParameterValues("figureAid");
        int[] figureAids = new int[figureAidsStr.length];
        for (int i = 0; i < figureAidsStr.length; i++) {
            figureAids[i] = Integer.parseInt(figureAidsStr[i]);
            System.out.println("figureAids["+i+"] = "+figureAids[i]);
        }
        for(int i =0;i<figureAids.length;i++){
            List<SubmitEntity> submits=userDao.findAllSubmit(figureAids[i]);
            for(int j=0;j<submits.size();j++){
                String args[]=new String[2];
                args[0]=submits.get(j).getLanguage();
                args[1]=localpath+"/"+submits.get(j).getFile();
                MyLauncher.main(args);
            }
        }

    }

    @ResponseBody
    @RequestMapping(value = "/addSingleBySid", method = RequestMethod.POST)
    public void addSingleBySid(@RequestParam("sid") String sid,
                               HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter out = null;

        if (userDao.findUser(sid) != null) {
            try {
                out = response.getWriter();
                out.print("<script>alert('User already exists!');" +
                        "window.location.href='/root/manageUser';</script>");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            UserEntity user = new UserEntity(sid, "", "123");
            userDao.addUser(user);
            try {
                out = response.getWriter();
                out.print("<script>alert('Add single user success!');" +
                        "window.location.href='/root/manageUser';</script>");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/addMultipleByFile", method = RequestMethod.POST)
    public void addMultipleByFile(@RequestParam("userFile") MultipartFile userFile,
                                  HttpServletResponse response) {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter out = null;

        String url = "/src/main/java/com/zxyu/test/jars/addUserFile/";
        String fileNameU = "users_" + assistTool.formatDate(new Date(), "YYYYMMDDHHmmss") + ".txt";
        boolean addUserResult = assistTool.saveFile(url, userFile, fileNameU);
        if (!addUserResult) {
            //alert提醒
            try {
                out = response.getWriter();
                out.print("<script>alert('User file upload fails! ');" +
                        "window.location.href='#';</script>");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String userFileUrl = localpath + url + "/" + fileNameU;
        ArrayList<String> users = assistTool.readUserFile(userFileUrl);
        for (int i = 0; i < users.size(); i++) {
            if (userDao.findUser(users.get(i)) == null) {
                UserEntity user = new UserEntity(users.get(i), "", "123");
                userDao.addUser(user);
            }
        }
        try {
            out = response.getWriter();
            out.print("<script>alert('Add user file success!');" +
                    "window.location.href='/root/manageUser';</script>");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
