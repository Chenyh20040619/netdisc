package com.chenyh.netdisc.controller;

import com.chenyh.netdisc.pojo.User;
import com.chenyh.netdisc.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/toLogin")
    @ResponseBody //返回对象为字符串
    public String login(String id, String password){
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(id, password);
        try {
            subject.login(token);
            User user = (User) subject.getPrincipal();
            return "success";
        }catch (UnknownAccountException e){
            System.out.println("账号不对");
            return "账号不对";
        }catch (IncorrectCredentialsException e){
            System.out.println("密码不对");
            return "密码不对";
        }
    }

    @DeleteMapping("/deleteUser")
    public int deleteUser(@RequestParam("id") String id){
        int state = userService.deleteUser(id);
        return state;
    }
}
