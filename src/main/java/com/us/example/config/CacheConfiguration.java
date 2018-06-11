package com.us.example.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

/**
 * ehcache的配置类.

 ----------------------
 1/Factory --->EhCacheManagerFactoryBean 
 2/Manager --->EhCacheCacheManager
 
 */
@Configuration //这是配置类.
@EnableCaching //启动缓存.
public class CacheConfiguration {
	
	@Bean
	public EhCacheManagerFactoryBean ehCacheManagerFactoryBean(){
		EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
		ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("conf/ehcache.xml"));
		return ehCacheManagerFactoryBean;
	}
	
	@Bean
	public EhCacheCacheManager ehCacheCacheManager(EhCacheManagerFactoryBean factoryBean){
		EhCacheCacheManager cacheCacheManager = new EhCacheCacheManager(factoryBean.getObject());
		return cacheCacheManager;
	}
	
	
}
