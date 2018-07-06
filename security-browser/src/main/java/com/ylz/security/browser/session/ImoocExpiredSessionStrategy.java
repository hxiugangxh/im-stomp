package com.ylz.security.browser.session;

import com.ylz.security.core.properties.SecurityProperties;
import org.springframework.security.web.session.SessionInformationExpiredEvent;
import org.springframework.security.web.session.SessionInformationExpiredStrategy;

import javax.servlet.ServletException;
import java.io.IOException;

/**
 * 并发登录导致session失效时，默认的处理策略
 */
public class ImoocExpiredSessionStrategy extends AbstractSessionStrategy implements
		SessionInformationExpiredStrategy {

    public ImoocExpiredSessionStrategy(SecurityProperties securityProperties) {
        super(securityProperties);
    }

    /* (non-Javadoc)
     * @see org.springframework.security.web.session
     * .SessionInformationExpiredStrategy#onExpiredSessionDetected(org.springframework.security
     * .web.session.SessionInformationExpiredEvent)
     */
    @Override
    public void onExpiredSessionDetected(SessionInformationExpiredEvent event) throws
			IOException, ServletException {
        onSessionInvalid(event.getRequest(), event.getResponse());
    }

    /* (non-Javadoc)
     * @see com.imooc.security.browser.session.AbstractSessionStrategy#isConcurrency()
     */
    @Override
    protected boolean isConcurrency() {
        return true;
    }

}
