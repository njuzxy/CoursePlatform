package com.zxyu.test.Controller;

import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.Entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@EnableAutoConfiguration
@RequestMapping("/root")
public class RootController {

    @Autowired
    private UserDao userDao;

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


    @ResponseBody
    @RequestMapping(value = "/publishAssignment")
    public String publishAssignment(HttpServletRequest request) {

        return "";
    }

}
