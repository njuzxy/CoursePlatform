package com.zxyu.test.Controller;

import com.zxyu.test.Dao.UserDao;
import com.zxyu.test.DaoImpl.UserDaoImpl;
import com.zxyu.test.Entity.UserEntity;
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

    @RequestMapping("/")
    public ModelAndView getIndex(ModelAndView mv) {
        mv.setViewName("/index");
        mv.addObject("title","欢迎使用Thymeleaf!");
        return mv;    }


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
    public String doLogin(@RequestParam("email") String email,
                          @RequestParam("pwd") String pwd,
                          HttpServletRequest request,
                          HttpServletResponse response) {
        UserEntity user = userDao.findUser(email);

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter out = null;
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
                session.setAttribute("user", userDao.findUser(email));
                return "redirect:info";
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


        return "";

    }


    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public void doRegist(@RequestParam("email") String email,
                         @RequestParam("pwd1") String pwd1,
                         @RequestParam("pwd2") String pwd2,
                         @RequestParam("nickname") String nickname,
                         ModelMap model,
                         HttpServletResponse response) {
        System.out.println("email: "+email);
        String returnMessage = "";
        boolean result = false;
        response.setHeader("Content-type", "text/html;charset=UTF-8");
        PrintWriter out = null;

        UserEntity user=new UserEntity();
        //user.setEmail(email);
        user.setPassword(pwd1);
        user.setSid("");
        user.setUsername(nickname);
        if (pwd1.equals(pwd2)) {

            userDao.addUser(user);
                returnMessage = "Success! Please Check the email.";
                try {
                    out = response.getWriter();
                    out.print("<script>alert('" + returnMessage + "');" +
                            "window.location.href='/login';</script>");
                    out.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }

        } else {
            returnMessage = "Password not match!";
            try {
                out = response.getWriter();
                out.print("<script>alert('" + returnMessage + "');" +
                        "window.location.href='/register';</script>");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
