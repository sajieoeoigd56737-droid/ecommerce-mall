package com.example.ecommerce.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.ecommerce.common.Result;
import com.example.ecommerce.dto.LoginDTO;
import com.example.ecommerce.dto.RegisterDTO;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.mapper.UserMapper;
import com.example.ecommerce.vo.UserInfoVO;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserMapper userMapper;

    @PostMapping("/register")
    public Result<Void> register(@RequestBody RegisterDTO request) {
        if (request == null || !StringUtils.hasText(request.getUsername())) {
            return Result.error("用户名不能为空");
        }
        if (!StringUtils.hasText(request.getPassword())) {
            return Result.error("密码不能为空");
        }

        String username = request.getUsername().trim();
        User existingUser = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
        if (existingUser != null) {
            return Result.error("用户名已存在");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(request.getPassword());
        user.setNickname(request.getNickname());
        userMapper.insert(user);
        return Result.success();
    }

    @PostMapping("/login")
    public Result<UserInfoVO> login(@RequestBody LoginDTO request) {
        if (request == null || !StringUtils.hasText(request.getUsername())) {
            return Result.error("用户名不能为空");
        }
        if (!StringUtils.hasText(request.getPassword())) {
            return Result.error("密码不能为空");
        }

        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, request.getUsername().trim()));
        if (user == null) {
            return Result.error("用户不存在");
        }
        if (!user.getPassword().equals(request.getPassword())) {
            return Result.error("密码错误");
        }
        return Result.success(toUserInfo(user));
    }

    @GetMapping("/info/{id}")
    public Result<UserInfoVO> info(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        if (user == null) {
            return Result.error("用户不存在");
        }
        return Result.success(toUserInfo(user));
    }

    private UserInfoVO toUserInfo(User user) {
        UserInfoVO userInfo = new UserInfoVO();
        userInfo.setId(user.getId());
        userInfo.setUsername(user.getUsername());
        userInfo.setNickname(user.getNickname());
        return userInfo;
    }
}
