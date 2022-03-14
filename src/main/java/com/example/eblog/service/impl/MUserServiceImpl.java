package com.example.eblog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.eblog.common.lang.Result;
import com.example.eblog.entity.MUser;
import com.example.eblog.mapper.MUserMapper;
import com.example.eblog.service.MUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.eblog.shiro.AccountProfile;
import org.apache.catalina.User;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dew0209
 * @since 2022-03-08
 */
@Service
public class MUserServiceImpl extends ServiceImpl<MUserMapper, MUser> implements MUserService {

    @Override
    public Result register(MUser user) {
        int count = this.count(new QueryWrapper<MUser>().eq("email", user.getEmail())
                .or()
                .eq("username", user.getUsername()));
        if(count > 0)return Result.fail("用户名或者邮箱已经被用");
        MUser temp = new MUser();
        temp.setUsername(user.getUsername());
        temp.setPassword(SecureUtil.md5(user.getPassword()));
        temp.setEmail(user.getEmail());
        temp.setCreated((new Date()));
        temp.setPoint(0);
        temp.setAvatar("/res/images/avatar/default.png");
        temp.setCommentCount(0);
        temp.setBirthday(new Date());
        temp.setGender("0");
        temp.setSign("这个人很懒，什么也没写");
        this.save(temp);
        return Result.success();
    }

    @Override
    public AccountProfile login(String email, String password) {
        MUser user = this.getOne(new QueryWrapper<MUser>().eq("email", email));
        if (user == null){
            throw new UnknownAccountException();
        }
        if (!user.getPassword().equalsIgnoreCase(password)){
            throw new IncorrectCredentialsException();
        }
        user.setLasted(new Date());
        this.updateById(user);

        AccountProfile profile = new AccountProfile();
        BeanUtil.copyProperties(user,profile);

        return profile;
    }

}
