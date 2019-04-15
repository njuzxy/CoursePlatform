package com.zxyu.test.Controller;

import com.zxyu.test.Dao.FileDao;
import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.Entity.*;
import com.zxyu.test.Helper.*;
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

import static com.zxyu.test.Helper.AssistTool.innerUrl;
import static com.zxyu.test.Helper.AssistTool.tempUrl;

@Controller
@EnableAutoConfiguration
@RequestMapping("/root")
public class RootController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private FileDao fileDao;

    private AssistTool assistTool = new AssistTool();

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
        if (assignments.size() > 0) {
            System.out.println("------------" + assignments.get(0).getTitle());
        }
        ModelAndView mv = new ModelAndView("/root/r-assignment");
        mv.addObject("sid", "root");
        mv.addObject("ctype", ctype);
        mv.addObject("assignments", assignments);
        return mv;
    }

    @RequestMapping(value = "/{ctype}/{aid}/assignmentInfo", method = RequestMethod.GET)
    public ModelAndView getAssignmentInfo(@PathVariable String ctype, @PathVariable String aid) {
        int aidInt = Integer.parseInt(aid);
        ModelAndView mv = new ModelAndView("/root/r-assignmentInfo");
        mv.addObject("sid", "root");
        AssignmentEntity assignment = userDao.findAssignment(aidInt);


        mv.addObject("assignment", assignment);

        return mv;
    }

    @RequestMapping(value = "/publishAssignment", method = RequestMethod.GET)
    public ModelAndView getPublishAssignment() {
        List<CourseEntity> courses = userDao.findAllCourse();
        ModelAndView mv = new ModelAndView("/root/publishAssignment");
        mv.addObject("sid", "root");
        mv.addObject("courses", courses);
        return mv;
    }

    @RequestMapping(value = "/figureAssignments", method = RequestMethod.GET)
    public ModelAndView getFigureAssignments() {
        List<CourseEntity> courses = userDao.findAllCourse();
        ModelAndView mv = new ModelAndView("/root/figureAssignment");
        mv.addObject("sid", "root");
        mv.addObject("courses", courses);

        return mv;
    }

    @RequestMapping(value = "/figureAssignment", method = RequestMethod.GET)
    public ModelAndView getFigureAssignment(@RequestParam("ctype") String ctype, HttpServletRequest req, HttpServletResponse resp) {
//        String state = req.getParameter("assignmentState");

        List<CourseEntity> courses = userDao.findAllCourse();
        List<AssignmentEntity> assignments = new ArrayList<>();
        if (ctype == null) {
            System.out.println("ctype now is : " + ctype);
            ctype = courses.get(0).getCtype();
            assignments = userDao.findAllAssignment(ctype);
        } else {
            System.out.println("ctype now is : " + ctype);
            assignments = userDao.findAllAssignment(ctype);
        }
        List<AssignmentEntity> assignmentsPublished = new ArrayList<>();
        List<AssignmentEntity> assignmentsEnd = new ArrayList<>();
        List<AssignmentEntity> assignmentsFinished = new ArrayList<>();
        List<AssignmentEntity> assignmentsScoring = new ArrayList<>();
        List<AssignmentEntity> assignmentsScored = new ArrayList<>();
        for (int i = 0; i < assignments.size(); i++) {
            switch (assignments.get(i).getState()) {
                case "Published":
                    assignmentsPublished.add(assignments.get(i));
                    break;
                case "End":
                    assignmentsEnd.add(assignments.get(i));
                    break;
                case "Finished":
                    assignmentsFinished.add(assignments.get(i));
                    break;
                case "Scoring":
                    assignmentsScoring.add(assignments.get(i));
                    break;
                case "Scored":
                    assignmentsScored.add(assignments.get(i));
                    break;
            }
        }

        ModelAndView mv = new ModelAndView("/root/figureAssignment");
        mv.addObject("sid", "root");
        mv.addObject("courses", courses);
        mv.addObject("ctype", ctype);
        mv.addObject("assignments", assignments);
        mv.addObject("assignmentsPublished", assignmentsPublished);
        mv.addObject("assignmentsEnd", assignmentsEnd);
        mv.addObject("assignmentsFinished", assignmentsFinished);
        mv.addObject("assignmentsScoring", assignmentsScoring);
        mv.addObject("assignmentsScored", assignmentsScored);


        System.out.println("TEST*******************");

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
                                  @RequestParam("outputUrl") String outputUrl,
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

        int aid = userDao.findCourseNextAid();

        String deadlineD = deadline + ";" + lateTime + ";" + latePercent;


        String url = innerUrl + courseType + "/" + aid;
        //创建submit和txt质量/查重输入输出目录
        assistTool.createDir(url);
        assistTool.createDir(url + "/submit");
        assistTool.createDir(url + "/txt/java");
        assistTool.createDir(url + "/txt/python");
        assistTool.createDir(url + "/txt/javaquality");
        assistTool.createDir(url + "/txt/pythonquality");

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
        AssignmentEntity assignment = new AssignmentEntity(aid, title, courseType, description, deadlineD, detailedFileUrl, testFileUrl, assistTool.enum2Str(StateEnum.Published), outputUrl);
        userDao.addAssignment(assignment);

        try {
            out = response.getWriter();
            out.print("<script> window.location.href='/root/course';</script>");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
//        return "redirect:/root/course";
    }

    @ResponseBody
    @RequestMapping(value = "/figureAssignmentsAct", method = RequestMethod.POST)
    public void figureAssignments(HttpServletRequest request,
                                  HttpServletResponse response) throws Exception {
        String[] figureAidsStr = request.getParameterValues("figureAid");
        int[] figureAids = new int[figureAidsStr.length];
        for (int i = 0; i < figureAidsStr.length; i++) {
            figureAids[i] = Integer.parseInt(figureAidsStr[i]);
            System.out.println("figureAids[" + i + "] = " + figureAids[i]);
        }
        for (int i = 0; i < figureAids.length; i++) {
            int aidNow = figureAids[i];
            AssignmentEntity assignment = userDao.findAssignment(aidNow);
            //更新作业状态为scoring
            assignment.setState(assistTool.enum2Str(StateEnum.Scoring));
            userDao.updateAssignment(assignment);

            //run_score
            RunScoreHelper runScoreHelper = new RunScoreHelper(assignment.getOutpath(), assignment.getTestfile(), aidNow);
            //private 计算方法无法调用
            List<Object[]> run_scores = runScoreHelper.getList();//aid,sid,score
            assistTool.updateScore(run_scores,aidNow,StateEnum.Run);

            //quality_score java
            String java_dirNow = tempUrl + innerUrl + "/"+assignment.getCtype()+"/"+aidNow+"/txt/java";
            JavaQualityHelper javaQualityHelper = new JavaQualityHelper(java_dirNow);
            List<Object[]> java_scores=javaQualityHelper.getList();
            assistTool.updateScore(java_scores,aidNow,StateEnum.Quality);

            //quality_score python
            String py_dirNow = tempUrl + innerUrl + "/"+assignment.getCtype()+"/"+aidNow+"/txt/python";
            PyQualityHelper pyQualityHelper = new PyQualityHelper(py_dirNow);
            List<Object[]> py_scores=pyQualityHelper.getList();
            assistTool.updateScore(py_scores,aidNow,StateEnum.Quality);

            //更新作业状态为scored
            assignment.setState(assistTool.enum2Str(StateEnum.Scored));
            userDao.updateAssignment(assignment);

            //作业出分同时发送通知
            List<NoticeEntity> notices=new ArrayList<>();
            List<UserEntity> students=userDao.findAllUser();
            for(int j=0;j<students.size();j++){
                String info=assignment.getCtype()+" "+assignment.getTitle()+" "+aidNow+" has been scored.";
                NoticeEntity notice = new NoticeEntity(aidNow,students.get(j).getSid(),new Date(),info,assistTool.enum2Str(StateEnum.Unread));
                notices.add(notice);
            }
            userDao.addNotices(notices);
        }

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter out = null;
        out = response.getWriter();
        out.print("<script>alert('Figure assignments success!');" +
                "window.location.href='/root/figureAssignments';</script>");
        out.flush();

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

        String url = innerUrl + "addUserFile/";
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

        String userFileUrl = tempUrl + url + "/" + fileNameU;
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
