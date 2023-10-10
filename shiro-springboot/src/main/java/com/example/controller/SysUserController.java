package com.example.controller;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.domian.SysUser;
import com.example.service.SysUserService;
import org.apache.shiro.crypto.SecureRandomNumberGenerator;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 刘苏义
 * @since 2023年10月10日
 */
@RestController
@RequestMapping("/sysUser")
public class SysUserController {
    @Resource
    SysUserService sysUserService;
    @RequestMapping("/addUser")
    public String addUser(SysUser sysUser) {
        //盐
        String Salt = new SecureRandomNumberGenerator().nextBytes().toHex();
        //加密
        SimpleHash simpleHash = new SimpleHash("md5",sysUser.getPassword(),Salt,1);
        String NewPassword = simpleHash.toString();
        sysUser.setPassword(NewPassword);
        sysUser.setSalt(Salt);
        boolean save = sysUserService.save(sysUser);
        return "插入成功";
    }
    @RequestMapping("/getUserDetail")
    public SysUser getUserDetail(SysUser sysUser) {
        Wrapper<SysUser> queryWrapper=new QueryWrapper<>(sysUser);
        return sysUserService.getOne(queryWrapper);
    }
}
