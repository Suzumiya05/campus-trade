package com.suzumiya.campustrade.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suzumiya.campustrade.entity.R;
import com.suzumiya.campustrade.entity.User;
import com.suzumiya.campustrade.exception.BusinessException;
import com.suzumiya.campustrade.mapper.UserMapper;
import com.suzumiya.campustrade.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    //创建用户
    @Override
    public void createUser(User user) {
        userMapper.insert(user);
    }

    //删除用户
    @Override
    public void deleteUser(Long id) {
        userMapper.deleteById(id);
    }

    //更新用户信息
    @Override
    public void updateUser(User user) {
        User tmpUser = getById(user.getId());//数据库原始用户信息
        //要更新昵称
        if (user.getNickname() != null) {
            tmpUser.setNickname(user.getNickname());
        }
        //要更新电话
        if (user.getPhone() != null) {
            tmpUser.setPhone(user.getPhone());
        }
        userMapper.updateById(tmpUser);
    }

    //按id查询
    @Override
    public User getById(Long id) {
        return userMapper.selectById(id);
    }

    //按昵称查询
    @Override
    public List<User> getByNickname(String nickname) {
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("nickname",nickname);
        return userMapper.selectList(qw);
    }

    //分页查所有用户
    @Override
    public Page<User> getByPage(Integer current, Integer size) {
        Page<User> page = new Page<>(current, size);
        userMapper.selectPage(page,null);
        return page;
    }

    //用户注册
    @Override
    public R<Void> register(User user) {
        //校验已由 Controller 通过 @Valid 完成，此处只做业务查重

        //用户名查重
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username",user.getUsername());
        Long count = userMapper.selectCount(qw);
        if (count > 0) {
            //return R.error("用户名已存在");
            throw new BusinessException("用户名已存在");
        }
        //用户有效，可注册
        createUser(user);
        user.setPassword(null);//密码置空，安全
        return R.ok();
    }

    //登录
    @Override
    public R<User> login(String username, String password, HttpSession session) {
        //匹配用户名
        QueryWrapper<User> qw = new QueryWrapper<>();
        qw.eq("username",username);
        User user = userMapper.selectOne(qw);
        if (user == null) return R.error("用户名或密码错误");
        //比对密码
        if (user.getPassword().equals(password)) {
            session.setAttribute("userId",user.getId());
            session.setAttribute("username",user.getUsername());
            user.setPassword(null);
            return R.ok(user);
        }
        else return R.error("用户名或密码错误");
    }
}
