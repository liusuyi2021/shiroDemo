package com.example.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @ClassName: MyController
 * @Author: 刘苏义
 * @Date: 2023年10月10日11:43:09
 **/
@Controller
public class MyController {

    @RequestMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("msg", "hello,shiro");
        return "index";
    }

    @RequestMapping("/user/add")
    public String add() {
        return "user/add";
    }

    @RequestMapping("/user/update")
    public String update() {
        return "user/update";
    }

    @RequestMapping("/toLogin")
    public String toLogin() {
        return "login";
    }

    @RequestMapping("/login")
    public String login(String username, String password, Model model) {
        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        //封装用户的的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, password);
        //执行登录方法
        try {
            subject.login(token);
            model.addAttribute("msg", "登陆成功");
            return "index";
        } catch (UnknownAccountException e) {
            model.addAttribute("msg", "用户名错误");
            return "login";
        } catch (IncorrectCredentialsException e) {
            model.addAttribute("msg", "密码错误");
            return "login";
        }

    }

    @RequestMapping("/unAuthor")
    public @ResponseBody
    String unAuthor() {
        return "未授权add";
    }

    @RequestMapping("/logout")
    public String logout() {
        //获取当前的用户
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            subject.logout();
        }
        return "redirect:/toLogin";
    }
}
