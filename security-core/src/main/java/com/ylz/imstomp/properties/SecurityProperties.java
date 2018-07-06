package com.ylz.imstomp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "imooc.security")
@Data
public class SecurityProperties {
	
	/**
	 * 浏览器环境配置
	 */
	private BrowserProperties browser = new BrowserProperties();
	/**
	 * 验证码配置
	 */
//	private ValidateCodeProperties code = new ValidateCodeProperties();
	/**
	 * OAuth2认证服务器配置
	 */
//	private OAuth2Properties oauth2 = new OAuth2Properties();

}

