package com.ylz.imstomp.config;

import com.ylz.imstomp.authorize.AuthorizeConfigProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Order(Integer.MIN_VALUE)
public class ImStompProvider implements AuthorizeConfigProvider {

    @Value("${im-stomp.eva.kisso}")
    private String kissoFlag;

    /* (non-Javadoc)
     * @see com.imooc.security.core.authorize.AuthorizeConfigProvider#config(org.springframework.security.config
     * .annotation.web.configurers.ExpressionUrlAuthorizationConfigurer.ExpressionInterceptUrlRegistry)
     */
    @Override
    public boolean config(ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry config) {
        log.info("kissoFlag = " + kissoFlag);
        if ("true".equals(kissoFlag)) {
            config.antMatchers("/loginIm").permitAll();
        }
        return false;
    }

}