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


@CrossOrigin
@RestController
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/toLogin")
    public User login(@RequestBody User passUser){
        String id = passUser.getId();
        String password = passUser.getPassword();
        System.out.println(id+" "+password);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken token = new UsernamePasswordToken(id, password);
        try {
            subject.login(token);
            User user = (User) subject.getPrincipal();
            return user;
        }catch (UnknownAccountException e){
            System.out.println("账号不对");
            return null;
        }catch (IncorrectCredentialsException e){
            return null;
        }
    }

    @PostMapping("/register")
    public String register(@RequestBody User registerInfo){
        int res = userService.addUser(registerInfo);
        if (res == 0){
            return null;
        }else {
            return "Ok";
        }
    }

    @DeleteMapping("/deleteUser")
    public int deleteUser(@RequestParam("id") String id){
        int state = userService.deleteUser(id);
        return state;
    }
}
