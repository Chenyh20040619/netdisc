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
    public boolean register(@RequestBody User registerInfo){
        int res = userService.addUser(registerInfo);
        if (res == 0){
            return false;
        }else {
            return true;
        }
    }
    /*
    不推荐使用@RequestParam接收application/json
    后端@RequestBody注解对应的类在将HTTP的输入流(含请求体)装配到目标类(即：@RequestBody后面的类)时，会根据json字符串中的key来匹配对应实体类的属性
     */
    @PostMapping("/deleteUser")
    public int deleteUser(@RequestBody User user){
        int state = userService.deleteUser(user.getId());
        return state;
    }

    @PostMapping("/resetPassword")
    public int resetPassword(@RequestBody User user){
        int state = userService.resetPassword(user.getId(), user.getPassword());
        return state;
    }

    @PostMapping("/updateUser")
    public int updateUser(@RequestBody User user){
        System.out.println(user);
        String id = user.getId();
        String username = user.getUsername();
        String email = user.getEmail();
        int state = userService.updateMsg(id, username, email);
        return state;
    }
}
