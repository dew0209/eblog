package com.example.eblog.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.eblog.common.lang.Result;
import com.example.eblog.entity.MUser;
import com.example.eblog.util.ValidationUtil;
import com.google.code.kaptcha.Producer;
import org.apache.catalina.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;

@Controller
public class AuthController extends BaseController{
    private static final String KAPTCHA_SESSION_KEY = "KAPTCHA_SESSION_KEY";
    @Autowired
    Producer producer;
    @GetMapping("/kapthca.jpg")
    public void kaptcha(HttpServletResponse resp) throws IOException {
        String text = producer.createText();
        req.getSession().setAttribute(KAPTCHA_SESSION_KEY,text);
        BufferedImage image = producer.createImage(text);
        ServletOutputStream outputStream = resp.getOutputStream();
        resp.setHeader("Cache-Control","no-store,no-cache");
        resp.setContentType("image/jpeg");
        ImageIO.write(image,"jpg",outputStream);

    }
    @GetMapping("/login")
    public String login(){
        return "/auth/login";
    }
    @GetMapping("/register")
    public String register(){
        return "/auth/reg";
    }

    @ResponseBody
    @PostMapping("register")
    public Result doRegister(MUser user, String repass, String vercode){
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(user);
        if (validResult.hasErrors()){
            return Result.fail(validResult.getErrors());
        }
        if(!user.getPassword().equals(repass)){
            return Result.fail("两次输入密码不相同");
        }

        String kapthca = (String)req.getSession().getAttribute(KAPTCHA_SESSION_KEY);
        System.out.println(kapthca);
        if(vercode == null || !vercode.equalsIgnoreCase(kapthca)){
            return Result.fail("验证码不正确");
        }
        Result result = userService.register(user);
        return result.success().action("/login");
    }
    @ResponseBody
    @PostMapping("/login")
    public Result doLogin(String email,String password){
        if(StrUtil.isEmpty(email) || StrUtil.isEmpty(password))return Result.fail("邮箱和密码不能为空");
        UsernamePasswordToken token = new UsernamePasswordToken(email, SecureUtil.md5(password));
        try{
            SecurityUtils.getSubject().login(token);
        }catch (AuthenticationException e) {
            if (e instanceof UnknownAccountException) {
                return Result.fail("用户不存在");
            } else if (e instanceof LockedAccountException) {
                return Result.fail("用户被禁用");
            } else if (e instanceof IncorrectCredentialsException) {
                return Result.fail("密码错误");
            } else {
                return Result.fail("用户认证失败");
            }
        }

        return Result.success().action("/");
    }

    @RequestMapping("/user/logout")
    public String logout(){
        SecurityUtils.getSubject().logout();
        return "/";
    }
}
