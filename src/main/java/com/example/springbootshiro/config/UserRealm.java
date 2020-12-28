package com.example.springbootshiro.config;

import com.example.springbootshiro.pojo.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.context.annotation.Configuration;

//自定义UserRealm
public class UserRealm extends AuthorizingRealm {
    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行了=>授权doGetAuthorizationInfo");
        //给用户授权
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //info.addStringPermission("user:add");
        //拿到当前登录的这个对象
        Subject subject = SecurityUtils.getSubject();
        User currentUser = (User) subject.getPrincipal();
        System.out.println(currentUser);
        //设置当前用户的权限
        info.addStringPermission(currentUser.getPerms());
        return info;
    }
    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行了=>认证doGetAuthenticationInfo");
        //用户名、密码，数据中取
        User user = new User("root", "3333", "user:add");

        UsernamePasswordToken userToken = (UsernamePasswordToken) authenticationToken;

        if (!userToken.getUsername().equals(user.getUsername())){
            return null;    //抛出异常  UnknownAccountException
        }
        Subject currentSubject = SecurityUtils.getSubject();
        Session session = currentSubject.getSession();
        session.setAttribute("loginUser",user);
        //密码认证，shiro自带功能
        //可以加密，md5 md5盐值加密
        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }
}
