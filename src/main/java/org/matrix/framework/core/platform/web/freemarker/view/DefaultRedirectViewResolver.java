/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.freemarker.view;


import org.matrix.framework.core.platform.web.request.RequestContext;
import org.matrix.framework.core.platform.web.site.URLBuilder;
import org.matrix.framework.core.settings.DeploymentSettings;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractView;
import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import javax.inject.Inject;

import java.util.Locale;

public class DefaultRedirectViewResolver extends UrlBasedViewResolver {
    private DeploymentSettings deploymentSettings;
    private RequestContext requestContext;

    public DefaultRedirectViewResolver() {
        setOrder(Ordered.HIGHEST_PRECEDENCE);
        setRedirectHttp10Compatible(false);
        setRedirectContextRelative(false);
        setViewClass(RedirectView.class);
    }

    // modified according to parent method
    @Override
    protected View createView(String viewName, Locale locale) throws Exception {
        if (viewName.startsWith(REDIRECT_URL_PREFIX)) {
            String redirectURL = viewName.substring(REDIRECT_URL_PREFIX.length());
            // always use contextRelative = false, and handle url according to SiteSettings
            URLBuilder builder = new URLBuilder();
            builder.setContext(requestContext.getContextPath(), deploymentSettings.getDeploymentContext());
            String targetURL = builder.constructRelativeURL(redirectURL);
            RedirectView view = new RedirectView(targetURL, isRedirectContextRelative(), isRedirectHttp10Compatible());
            return applyLifecycleMethods(viewName, view);
        }

        return null;
    }

    private View applyLifecycleMethods(String viewName, AbstractView view) {
        return (View) getApplicationContext().getAutowireCapableBeanFactory().initializeBean(view, viewName);
    }

    boolean useHTTP10Redirect() {
        return isRedirectHttp10Compatible();
    }

    boolean useSpringRelativeRedirect() {
        return isRedirectContextRelative();
    }

    @Inject
    public void setDeploymentSettings(DeploymentSettings deploymentSettings) {
        this.deploymentSettings = deploymentSettings;
    }

    @Inject
    public void setRequestContext(RequestContext requestContext) {
        this.requestContext = requestContext;
    }
}
