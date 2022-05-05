package com.chenyh.netdisc.service;

import com.chenyh.netdisc.dao.UserDao;
import com.chenyh.netdisc.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    UserDao userDao;
    public User getUserById(String id){
        User user = userDao.getUserById(id);
        return user;
    }
    public User getUserByIdAndPassword(String id, String password){
        User user = userDao.getUserByIdAndPassword(id, password);
        return user;
    }

    public int addUser(User user){
        int i = userDao.addUser(user);
        return i;
    }

    public int deleteUser(String id){
        int i = userDao.deleteUser(id);
        return i;
    }

    public int updateUser(User user){
        int i = userDao.updateUser(user);
        return i;
    }

    public int resetPassword(String id, String password){
        int i = userDao.resetPassword(id, password);
        return i;
    }
}
