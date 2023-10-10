package com.example.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @Description: shiro配置类
 * @ClassName: ShiroConfig
 * @Author: 刘苏义
 * @Date: 2023年10月10日11:55:45
 **/
@Configuration
public class ShiroConfig {
    //ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(@Qualifier("securityManager") DefaultWebSecurityManager defaultWebSecurityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        //设置安全管理器
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);
        //添加shiro的内置过滤器
        /*
        anon:无需认证就可以访问
        authc:必须认证才能访问
        user：必须拥有记住我功能才能访问
        perms：拥有某个资源权限才能访问
        role：拥有某个角色才能访问
        * */
        //拦截
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        //授权
        //filterChainDefinitionMap.put("/user/add","authc");
        //filterChainDefinitionMap.put("/user/update","authc");
        filterChainDefinitionMap.put("/user/add", "perms[user:add]");//授权
        filterChainDefinitionMap.put("/user/update", "perms[user:update]");//授权
        filterChainDefinitionMap.put("/user/*", "authc");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        shiroFilterFactoryBean.setLoginUrl("/toLogin");//拦截跳转到登录页面
        shiroFilterFactoryBean.setUnauthorizedUrl("/unAuthor");//拦截跳转到未授权页面
        return shiroFilterFactoryBean;
    }

    //DefaultWebSecurityManager：2
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(@Qualifier("userRealm") UserRealm userRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        //关联UserRealm
        securityManager.setRealm(userRealm);
        return securityManager;
    }

    //创建realm对象，需要自定义类:1
    @Bean(name = "userRealm")
    public UserRealm userRealm() {
        UserRealm userRealm = new UserRealm();
        //设置加密算法
        userRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return userRealm;
    }

    //加密算法-哈希凭据匹配器
    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        return hashedCredentialsMatcher;
    }

    //为了在thymeleaf里使用shiro的标签bean
    @Bean
    public ShiroDialect getShiroDialect()
    {
        return new ShiroDialect();
    }
}
