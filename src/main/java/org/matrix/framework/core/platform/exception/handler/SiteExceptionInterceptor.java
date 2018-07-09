/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.exception.handler;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.DispatcherType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.matrix.framework.core.platform.web.freemarker.view.DefaultFreemarkerViewResolver;
import org.matrix.framework.core.settings.SiteSettings;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.servlet.View;

public class SiteExceptionInterceptor extends ExceptionInterceptor {

    private DefaultFreemarkerViewResolver defaultFreemarkerViewResolver;

    private SiteSettings siteSettings;

    private ErrorPageModelBuilder errorPageModelBuilder;

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
        super.afterCompletion(request, response, handler, exception);
        // only render error page if for request dispatch, to avoid duplicated
        // process with forward dispatching
        if (exception != null && DispatcherType.REQUEST.equals(request.getDispatcherType())) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            View view = defaultFreemarkerViewResolver.resolveViewName(siteSettings.getErrorPage(), LocaleContextHolder.getLocale());
            Map<String, Object> model = errorPageModelBuilder.buildErrorPageModel(exception);
            view.render(model, request, response);
        }
    }

    @Inject
    public void setSiteSettings(SiteSettings siteSettings) {
        this.siteSettings = siteSettings;
    }

    @Inject
    public void setDefaultFreemarkerViewResolver(DefaultFreemarkerViewResolver defaultFreemarkerViewResolver) {
        this.defaultFreemarkerViewResolver = defaultFreemarkerViewResolver;
    }

    @Inject
    public void setErrorPageModelBuilder(ErrorPageModelBuilder errorPageModelBuilder) {
        this.errorPageModelBuilder = errorPageModelBuilder;
    }

}
