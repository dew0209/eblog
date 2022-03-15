package com.example.eblog.common.exception;

import cn.hutool.json.JSONUtil;
import com.example.eblog.common.lang.Result;
import org.apache.shiro.SecurityUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class)
    public ModelAndView handel(HttpServletRequest req, HttpServletResponse resp,Exception e) throws IOException {
        HttpServletRequest httpServletRequest = req;

        // ajax 弹窗显示未登录
        String header = httpServletRequest.getHeader("X-Requested-With");
        if(header != null  && "XMLHttpRequest".equals(header)) {
            resp.setContentType("application/json;charset=UTF-8");
            resp.getWriter().print(JSONUtil.toJsonStr(Result.fail("请先登录！")));
            return null;
        }
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message",e.getMessage());
        return modelAndView;
    }
}
