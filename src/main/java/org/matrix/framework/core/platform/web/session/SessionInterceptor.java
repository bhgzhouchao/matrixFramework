/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.session;

import java.lang.annotation.Annotation;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.matrix.framework.core.log.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.matrix.framework.core.collection.Key;
import org.matrix.framework.core.database.memcached.MatrixMemcachedGroup;
import org.matrix.framework.core.platform.SpringObjectFactory;
import org.matrix.framework.core.platform.web.cookie.CookieContext;
import org.matrix.framework.core.platform.web.cookie.CookieSpec;
import org.matrix.framework.core.platform.web.session.annotation.Site;
import org.matrix.framework.core.platform.web.session.provider.MemcachedSessionProvider;
import org.matrix.framework.core.platform.web.session.provider.SessionProvider;
import org.matrix.framework.core.platform.web.site.SiteHelper;

import org.matrix.framework.core.settings.SiteSettings;

import org.matrix.framework.core.settings.SsoSettings;

public class SessionInterceptor extends HandlerInterceptorAdapter {

    private final Logger logger = LoggerFactory.getLogger(SessionInterceptor.class);

    private  Key<String> cookieSessionId = Key.stringKey("SessionId");
    
    private  Key<String> cookieSecureSessionId = Key.stringKey("SecureSessionId");
    
    private static final String ATTRIBUTE_CONTEXT_INITIALIZED = SessionInterceptor.class.getName() + ".CONTEXT_INITIALIZED";
    
    private static final String BEAN_NAME_SESSION_PROVIDER = "sessionProvider";

    private CookieContext cookieContext;
    
    private SessionContext sessionContext;
    
    private SecureSessionContext secureSessionContext;
    
    private SiteSettings siteSettings;
    
    private SessionProvider sessionProvider;
    
    private SpringObjectFactory springObjectFactory;
    
    private SsoSettings ssoSettings;

    private void generatedSessionKey() {
        final String sessionId = ssoSettings.getClientKey();
        final String secureSessionId = "Secure" + sessionId;
        cookieSessionId = Key.stringKey(sessionId);
        cookieSecureSessionId = Key.stringKey(secureSessionId);
    }
    
    @PostConstruct
    public void initialize() {
        generatedSessionKey();
        SessionProviderType type = siteSettings.getSessionProviderType();
        if (SessionProviderType.MEMCACHED.equals(type)) {
            springObjectFactory.registerSingletonBean(BEAN_NAME_SESSION_PROVIDER, MemcachedSessionProvider.class);
        } else {
            throw new IllegalStateException("unsupported session provider type, type=" + type);
        }
        sessionProvider = springObjectFactory.getBean(SessionProvider.class);
    }
    
    /**
     * 1.如果我是一个老用户那么就刷新session，获取当前用户Data
     * 2.如果我是一个新用户那么就告诉context我是一个新用户
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // only site controller may need session
        if (!SiteHelper.isSiteController(handler)) return true;
        final Site site = findAnnotation((HandlerMethod) handler, Site.class);
        if (null != site) return true;
        
        Boolean initialized = (Boolean) request.getAttribute(ATTRIBUTE_CONTEXT_INITIALIZED);

        // only process non-forwarded request, to make sure only init once per request
        if (!Boolean.TRUE.equals(initialized)) {
            
            loadSession(MatrixMemcachedGroup.SESSIONGROUP, sessionContext, cookieSessionId);

            //TODO:ThreadLocal support
            SessionHolder.setSession(sessionContext);
            
            if (request.isSecure()) {
                secureSessionContext.underSecureRequest();
                loadSession(MatrixMemcachedGroup.SESSIONGROUP, secureSessionContext, cookieSecureSessionId);
                
                //TODO:ThreadLocal support
                SessionHolder.setSession(secureSessionContext);
            }

            request.setAttribute(ATTRIBUTE_CONTEXT_INITIALIZED, Boolean.TRUE);
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        saveAllSessions(MatrixMemcachedGroup.SESSIONGROUP, request);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // if some interceptor break the preHandle by returning false, all postHandle will be skipped.
        // by this way we want to try to save session on completion if possible
        // due to setCookies only works before view is rendered
        saveAllSessions(MatrixMemcachedGroup.SESSIONGROUP, request);
        
        //Remove session from threadLocal.
        SessionHolder.removeSession();
    }

    private void saveAllSessions(final String groupName, HttpServletRequest request) {
        saveSession(groupName, sessionContext, cookieSessionId, false);
        
        if (request.isSecure()) {
            saveSession(groupName, secureSessionContext, cookieSecureSessionId, true);
        }
    }

    private void loadSession(final String groupName,  SessionContext sessionContext, Key<String> sessionIdCookieKey) {
        
        String sessionId = cookieContext.getCookie(sessionIdCookieKey); //从cookie中跟据key获取sessionid

        if (sessionId != null) {
            String sessionData = sessionProvider.getAndRefreshSession(groupName, sessionId);
            if (sessionData != null) {
                sessionContext.setId(sessionId);
                sessionContext.loadSessionData(sessionData);
            } else {
                logger.debug("can not find session, generate new sessionId to replace old one");
                sessionContext.requireNewSessionId();
            }
        }
    }

    private void saveSession(final String groupName, SessionContext sessionContext, Key<String> sessionIdCookieKey, boolean secure) {
        if (sessionContext.changed()) {
            if (sessionContext.invalidated()) {
                deleteSession(groupName, sessionContext, sessionIdCookieKey);
            } else {
                persistSession(groupName, sessionContext, sessionIdCookieKey, secure);
            }
            sessionContext.saved();
        }
    }

    private void deleteSession(final String groupName, SessionContext sessionContext, Key<String> sessionIdCookieKey) {
        String sessionId = sessionContext.getId();
        if (sessionId == null) {
            // session was not persisted, nothing is required
            return;
        }
        
        sessionProvider.clearSession(groupName, sessionId);
        CookieSpec spec = new CookieSpec();
        String domain = ssoSettings.getCookieDomain();
        if (StringUtils.hasText(domain)) spec.setDomain(domain);
        spec.setPath(ssoSettings.getCookiePath());
        cookieContext.deleteCookie(sessionIdCookieKey, spec);
    }

    private void persistSession(final String groupName, final SessionContext sessionContext, Key<String> sessionIdCookieKey, boolean secure) {
        String sessionId = sessionContext.getId();
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            sessionContext.setId(sessionId);
            CookieSpec spec = new CookieSpec();
            spec.setHttpOnly(true);
            spec.setPath(ssoSettings.getCookiePath());
            spec.setMaxAge(CookieSpec.MAX_AGE_SESSION_SCOPE);
            spec.setSecure(secure);
            String domain = ssoSettings.getCookieDomain();
            if (StringUtils.hasText(domain)) spec.setDomain(domain);
            cookieContext.setCookie(sessionIdCookieKey, sessionId, spec);
        }
        sessionProvider.saveSession(groupName, sessionId, sessionContext.getSessionData());
    }
    
    private <T extends Annotation> T findAnnotation(HandlerMethod handler, Class<T> annotationType) {
        T annotation = handler.getBeanType().getAnnotation(annotationType);
        if (annotation != null) return annotation;
        return handler.getMethodAnnotation(annotationType);
    }

    @Inject
    public void setSessionContext(SessionContext sessionContext) {
        this.sessionContext = sessionContext;
    }

    @Inject
    public void setSecureSessionContext(SecureSessionContext secureSessionContext) {
        this.secureSessionContext = secureSessionContext;
    }

    @Inject
    public void setSiteSettings(SiteSettings siteSettings) {
        this.siteSettings = siteSettings;
    }

    @Inject
    public void setCookieContext(CookieContext cookieContext) {
        this.cookieContext = cookieContext;
    }

    @Inject
    public void setSpringObjectFactory(SpringObjectFactory springObjectFactory) {
        this.springObjectFactory = springObjectFactory;
    }
    
    @Inject
    public void setSsoSettings(SsoSettings ssoSettings) {
        this.ssoSettings = ssoSettings;
    }
 
}