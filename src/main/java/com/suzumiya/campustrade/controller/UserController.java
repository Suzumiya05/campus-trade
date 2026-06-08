package com.suzumiya.campustrade.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.suzumiya.campustrade.entity.R;
import com.suzumiya.campustrade.entity.User;
import com.suzumiya.campustrade.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    //按id搜索
    @GetMapping("/{id}")
    public R<User> getById(@PathVariable long id){
        return R.ok(userService.getById(id));
    }
    //按昵称搜索
    @GetMapping(params = "nickname")
    public R<List<User>> getByNickname(@RequestParam String nickname){
        return R.ok(userService.getByNickname(nickname));
    }
    //分页查全部
    @GetMapping(params = {"current", "size"})
    public R<Page<User>> getByCurrentPage(@RequestParam int current, @RequestParam int size){
        return R.ok(userService.getByPage(current, size));
    }
    //新增用户
    @PostMapping
    public R<Void> save(@RequestBody User user){
        userService.createUser(user);
        return R.ok();
    }
    //删除用户
    @DeleteMapping("/{id}")
    public R<Void> delete(@PathVariable long id){
        userService.deleteUser(id);
        return R.ok();
    }
    //修改用户
    @PutMapping("/{id}")
    public R<Void> update(@PathVariable long id, @RequestBody User user){
        user.setId(id);
        userService.updateUser(user);
        return R.ok();
    }
    //注册
    @PostMapping("/register")
    public R<User> register(@RequestBody User user){
        return userService.register(user);
    }
    //登录
    @PostMapping("/login")
    public R<User> login(@RequestBody User user, HttpServletRequest request){
        HttpSession session = request.getSession();
        return userService.login(user.getUsername(), user.getPassword(), session);
    }
    //测试获取uid
//    @GetMapping("/testGetId")
//    public R<Long> getUserId(HttpServletRequest request){
//        Long userId = (Long) request.getAttribute("userId");
//        if (userId == null) {
//            return R.error("用户未登录，请先登录");
//        }
//        return R.ok(userId);
//    }
    @GetMapping("/me")  // 完整路径：/users/me
    public R<User> getCurrentUser(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) {
            return R.error("用户未登录");
        }
        // 根据 userId 查用户基本信息（用户名、昵称、头像等）
        User user = userService.getById(userId);
        user.setPassword(null);
        return R.ok(user);
    }
}
