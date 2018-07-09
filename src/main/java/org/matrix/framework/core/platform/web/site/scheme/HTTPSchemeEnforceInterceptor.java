/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.site.scheme;

import java.lang.annotation.Annotation;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.matrix.framework.core.http.HTTPConstants;
import org.matrix.framework.core.platform.web.filter.RequestUtils;
import org.matrix.framework.core.platform.web.site.URLBuilder;
import org.matrix.framework.core.settings.DeploymentSettings;

public class HTTPSchemeEnforceInterceptor extends HandlerInterceptorAdapter {

    private DeploymentSettings deploymentSettings;
    

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (handler instanceof HandlerMethod) {
            HTTPOnly httpOnly = findAnnotation((HandlerMethod) handler, HTTPOnly.class);
            if (httpOnly != null && !HTTPConstants.SCHEME_HTTP.equals(request.getScheme())) {
                enforceScheme(request, response, HTTPConstants.SCHEME_HTTP);
                return false;
            }
        
            HTTPSOnly httpsOnly = findAnnotation((HandlerMethod) handler, HTTPSOnly.class);
            if (httpsOnly != null && !HTTPConstants.SCHEME_HTTPS.equals(request.getScheme())) {
                enforceScheme(request, response, HTTPConstants.SCHEME_HTTPS);
                return false;
            }
        }
        return true;
    }

    private <T extends Annotation> T findAnnotation(HandlerMethod handler, Class<T> annotationType) {
        T annotation = handler.getBeanType().getAnnotation(annotationType);
        if (annotation != null) return annotation;
        return handler.getMethodAnnotation(annotationType);
    }

    private void enforceScheme(HttpServletRequest request, HttpServletResponse response, String scheme) {
        URLBuilder builder = new URLBuilder();
        builder.setContext(request.getContextPath(), deploymentSettings.getDeploymentContext());
        builder.setServerInfo(request.getServerName(), deploymentSettings.getHTTPPort(), deploymentSettings.getHTTPSPort());
        //builder.setServerInfo(request.getServerName(), deploymentSettings.getHTTPPort(), deploymentSettings.getHTTPSPort());
        response.setStatus(HTTPConstants.SC_MOVED_PERMANENTLY);
        response.setHeader("Location", builder.constructAbsoluteURL(scheme, RequestUtils.getClientRelativeRequestURLWithQueryString(request)));
    }

    @Inject
    public void setDeploymentSettings(DeploymentSettings deploymentSettings) {
        this.deploymentSettings = deploymentSettings;
    }
 
}