package com.chenyh.netdisc;

import com.chenyh.netdisc.dao.UserDao;
import com.chenyh.netdisc.pojo.User;
import com.chenyh.netdisc.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NetdiscApplicationTests {

    @Autowired
    UserService userService;
    @Test
    void contextLoads() {
        userService.addUser(new User("101", "2", "123", 1, true, "123"));
        userService.resetPassword("101", "111");
        User user = userService.getUserById("100");
        System.out.println(user);
        userService.deleteUser("100");
    }

}
