package com.suzumiya.campustrade.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suzumiya.campustrade.entity.R;
import com.suzumiya.campustrade.entity.User;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public interface UserService {
    //增：创建新用户
    void createUser(User user);
    //删：删除用户（注销）
    void deleteUser(Long id);
    //改：修改用户信息（昵称、头像、电话）
    void updateUser(User user);
    /* 查 */
    //通过id查询
    User getById(Long id);
    //通过昵称查询
    List<User> getByNickname(String nickname);
    //分页查询
    Page<User> getByPage(Integer current, Integer size);
    //用户注册
    R<Void> register(User user);
    //登录
    R<User> login(String username, String password, HttpSession session);
}
