package com.example.config;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.domian.SysUser;
import com.example.service.SysUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.DefaultPasswordService;
import org.apache.shiro.authc.credential.PasswordService;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

import javax.annotation.Resource;


/**
 * @Description: 自定义UserRealm
 * @ClassName: UserRealm
 * @Author: 刘苏义
 * @Date: 2023年10月10日11:57:18
 **/
public class UserRealm extends AuthorizingRealm {
    @Resource
    SysUserService sysUserService;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了==》授权");
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        //拿到当前登录的对象
        Subject subject = SecurityUtils.getSubject();
        SysUser sysUser = (SysUser) subject.getPrincipal();
        simpleAuthorizationInfo.addStringPermission(sysUser.getPerm());//添加字符串权限

        return simpleAuthorizationInfo;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行了==》认证");
        UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;
        //用户名密码 数据库中取
        Wrapper<SysUser> queryWrapper=new QueryWrapper<>(new SysUser().setUsername(userToken.getUsername()));
        SysUser user = sysUserService.getOne(queryWrapper);
        if (user == null) {
            return null;
        }
        //用户名不匹配返回null
        if (!user.getUsername().equals(userToken.getUsername())) {
            return null;
        }
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        session.setAttribute("userLogin", user);
        //密码验证
        String Salt = user.getSalt();
        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(Salt), "");
        // return new DefaultPasswordService("",user.getPassword(),"");
    }
}
