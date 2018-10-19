package com.hello.utils;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;

public class ShiroUtil {

	public static Subject login(String configFile, String username, String password) {

		// 读取配置文件 初始化SecurityManager工厂
		Factory<SecurityManager> factory = new IniSecurityManagerFactory(configFile);

		// 获取 SecurityManager 实例
		SecurityManager securityManager = factory.getInstance();

		// SecurityManager 实例 绑定到 securityUtils
		SecurityUtils.setSecurityManager(securityManager);

		// 获取当前的执行用户
		Subject subject = SecurityUtils.getSubject();

		// 创建 token
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);

		try {
			// 身份认证
			subject.login(token);
			System.out.println("登录成功");
		} catch (AuthenticationException e) {
			e.printStackTrace();
			System.out.println("登录失败");
		}

		// 退出
		return subject;
	}

}