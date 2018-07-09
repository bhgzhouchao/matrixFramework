/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform;

import java.util.Properties;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.web.context.WebApplicationContext;
import org.matrix.framework.core.platform.exception.handler.ErrorPageModelBuilder;
import org.matrix.framework.core.platform.exception.handler.ExceptionInterceptor;
import org.matrix.framework.core.platform.exception.handler.SiteExceptionInterceptor;
import org.matrix.framework.core.platform.web.cookie.CookieContext;
import org.matrix.framework.core.platform.web.cookie.CookieInterceptor;
import org.matrix.framework.core.platform.web.freemarker.view.DefaultFreeMarkerConfigurer;
import org.matrix.framework.core.platform.web.freemarker.view.DefaultFreemarkerView;
import org.matrix.framework.core.platform.web.freemarker.view.DefaultFreemarkerViewResolver;
import org.matrix.framework.core.platform.web.freemarker.view.DefaultRedirectViewResolver;
import org.matrix.framework.core.platform.web.session.SecureSessionContext;
import org.matrix.framework.core.platform.web.session.SessionContext;
import org.matrix.framework.core.platform.web.session.SessionInterceptor;
import org.matrix.framework.core.platform.web.site.scheme.HTTPSchemeEnforceInterceptor;
import org.matrix.framework.core.settings.CDNSettings;
import org.matrix.framework.core.settings.DefaultCDNSettings;
import org.matrix.framework.core.settings.DeploymentSettings;
import org.matrix.framework.core.settings.SiteSettings;
import org.matrix.framework.core.settings.SsoSettings;
import org.matrix.framework.core.settings.ThumbnailSettings;
import freemarker.template.Configuration;

public class MatrixSiteWebConfig extends DefaultSiteWebConfig {

    //1.framemarker config
    @Bean
    public DefaultFreeMarkerConfigurer freeMarkerConfigurer() {
        DefaultFreeMarkerConfigurer config = new DefaultFreeMarkerConfigurer();
        config.setTemplateLoaderPath("/");
        Properties settings = new Properties();
        setFreeMarkerSettings(settings);
        config.setFreemarkerSettings(settings);
        return config;
    }

    protected void setFreeMarkerSettings(final Properties settings) {
        settings.setProperty(Configuration.DEFAULT_ENCODING_KEY, "UTF-8");
        settings.setProperty(Configuration.URL_ESCAPING_CHARSET_KEY, "UTF-8");
        settings.setProperty(Configuration.NUMBER_FORMAT_KEY, "0.##");
    }

    @Bean
    public DefaultFreemarkerViewResolver freeMarkerViewResolver() {
        DefaultFreemarkerViewResolver resolver = new DefaultFreemarkerViewResolver();
        resolver.setPrefix("/WEB-INF/templates/");
        resolver.setSuffix(".ftl");
        resolver.setContentType("text/html; charset=UTF-8");
        resolver.setViewClass(DefaultFreemarkerView.class);
        resolver.setExposeSpringMacroHelpers(true);
        resolver.setExposeRequestAttributes(true);
        resolver.setAllowRequestOverride(true);
        return resolver;
    }

    @Bean
    public DefaultRedirectViewResolver redirectViewResolver() {
        return new DefaultRedirectViewResolver();
    }

    //3.config warpper
    @Bean
    public DeploymentSettings deploymentSettings() {
        return new DeploymentSettings();
    }

    @Bean
    public SiteSettings siteSettings() {
        return new SiteSettings();
    }

    @Bean
    public SsoSettings ssoSettings() {
        return new SsoSettings();
    }

    @Bean
    public CDNSettings cdnSettings() {
        return new DefaultCDNSettings();  
    }
    
    @Bean
    public ThumbnailSettings thumbnailSettings() {
        return new ThumbnailSettings();
    }

    @Bean
    public ErrorPageModelBuilder errorPageModelBuilder() {
        return new ErrorPageModelBuilder();
    }

    //4.http context warpper
    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public CookieContext cookieContext() {
        return new CookieContext();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SessionContext sessionContext() {
        return new SessionContext();
    }

    @Bean
    @Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
    public SecureSessionContext secureSessionContext() {
        return new SecureSessionContext();
    }

    //5.inteceptor config
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public CookieInterceptor cookieInterceptor() {
        return new CookieInterceptor();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public SessionInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public HTTPSchemeEnforceInterceptor httpSchemeEnforceInterceptor() {
        return new HTTPSchemeEnforceInterceptor();
    }
    
    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Override
    public ExceptionInterceptor exceptionInterceptor() {
        return new SiteExceptionInterceptor();
    }
    
}