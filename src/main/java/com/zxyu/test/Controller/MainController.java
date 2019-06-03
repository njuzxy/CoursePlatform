package com.zxyu.test.Controller;

import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.DaoImpl.UserDaoImpl;
import com.zxyu.test.Entity.UserEntity;
import com.zxyu.test.Helper.AssistTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;

@Controller
@EnableAutoConfiguration
public class MainController {

    @Autowired
    private UserDao userDao;

    private AssistTool assistTool = new AssistTool();

    @RequestMapping("/")
    public ModelAndView getIndex(ModelAndView mv) {
        mv.setViewName("/index");
        return mv;
    }


    @RequestMapping("/register")
    public String getRegister() {
        return "register";
    }


    @RequestMapping("/login")
    public String getLogin() {
        return "login";
    }

    @RequestMapping("/info")
    public String getInfo() {
        return "info";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String doLogin(@RequestParam("sid") String sid,
                          @RequestParam("pwd") String pwd,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        UserEntity user = userDao.findUser(sid);

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter out = null;

        //每次登录前检查作业状态是否已截止
        assistTool.updateAssignmentState();

        if (sid.equals("root")) {
            if(pwd.equals("root")){
                HttpSession session = request.getSession();
                session.setAttribute("sid", sid);
                return "redirect:/root/course";
            }else{
                //alert提醒
                try {
                    out = response.getWriter();
                    out.print("<script>alert('Root Password wrong! ');" +
                            "window.location.href='/login';</script>");
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (user == null) {
                //alert提醒
                try {
                    out = response.getWriter();
                    out.print("<script>alert('User not found! ');" +
                            "window.location.href='/login';</script>");
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {

                if (user.getPassword().equals(pwd)) {
                    HttpSession session = request.getSession();
                    session.setAttribute("sid", sid);
                    return "redirect:/student/" + sid + "/course";
                } else {
                    try {
                        out = response.getWriter();
                        out.print("<script>alert('Password wrong! ');" +
                                "window.location.href='/login';</script>");
                        out.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "login";
    }


    @RequestMapping("/logout")
    public void getLogout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.invalidate();
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter out = null;
        try {
            out = response.getWriter();
            out.print("<script> window.location.href='/login';</script>");
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
