package com.example.eblog.service;

import com.example.eblog.common.lang.Result;
import com.example.eblog.entity.MUser;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.eblog.shiro.AccountProfile;
import org.apache.catalina.User;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author dew0209
 * @since 2022-03-08
 */
public interface MUserService extends IService<MUser> {

    Result register(MUser user);

    AccountProfile login(String username, String password);
}
