package com.chenyh.netdisc.dao;

import com.chenyh.netdisc.pojo.User;
import org.apache.ibatis.annotations.*;


@Mapper
public interface UserDao {
    int addUser(User user);

    @Delete("delete from user where id = #{id}")
    int deleteUser(@Param("id") String id);

    int updateUser(User user);

    int updateMsg(String id, String username, String email);

    @Select("select * from user where id = #{id}")
    User getUserById(@Param("id") String id);

    @Select("select * from user where id = #{id} and password = #{password}")
    User getUserByIdAndPassword(@Param("id") String id, @Param("password") String password);

    @Update("update user set password = #{password} where id = #{id}")
    int resetPassword(@Param("id") String id, @Param("password") String password);
}
