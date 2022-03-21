package com.example.eblog.config;

import cn.hutool.core.map.MapUtil;
import com.example.eblog.shiro.AccountRealm;
import com.example.eblog.shiro.AuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;


@Slf4j
@Configuration
public class ShiroConfig {

    @Bean
    public SecurityManager securityManager(AccountRealm accountRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(accountRealm);
        log.info("注入成功");
        return securityManager;
    }
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(SecurityManager securityManager){
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        filterFactoryBean.setSecurityManager(securityManager);
        // 配置登录的url和登录成功的url
        filterFactoryBean.setLoginUrl("/login");
        filterFactoryBean.setSuccessUrl("/user/center");
        // 配置未授权跳转页面
        filterFactoryBean.setUnauthorizedUrl("/error/403");

        Map<String, String> hashMap = new LinkedHashMap<>();

        hashMap.put("/login", "anon");
        hashMap.put("/user/home", "authc");
        hashMap.put("/user/set", "authc");
        hashMap.put("/user/upload", "authc");
        filterFactoryBean.setFilters(MapUtil.of("auth",authFilter()));
        hashMap.put("/collection/find", "auth");
        hashMap.put("/collection/add", "auth");
        hashMap.put("/collection/remove", "auth");

        filterFactoryBean.setFilterChainDefinitionMap(hashMap);

        return filterFactoryBean;
    }
    @Bean
    public AuthFilter authFilter(){
        return new AuthFilter();
    }
}
