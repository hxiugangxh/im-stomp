package com.us.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 表单登录配置
 * 
 */
@Component
public class FormAuthenticationConfig {

	@Autowired
	protected AuthenticationSuccessHandler imoocAuthenticationSuccessHandler;
	

	public void configure(HttpSecurity http) throws Exception {
		http.formLogin()
			.successHandler(imoocAuthenticationSuccessHandler);
	}
	
}
