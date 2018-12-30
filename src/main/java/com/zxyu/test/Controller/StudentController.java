package com.zxyu.test.Controller;

import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
}
