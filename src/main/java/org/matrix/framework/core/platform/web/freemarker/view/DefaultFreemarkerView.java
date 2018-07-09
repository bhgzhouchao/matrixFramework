/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.freemarker.view;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.matrix.framework.core.platform.web.cookie.CookieContext;
import org.matrix.framework.core.platform.web.freemarker.tag.CDNTag;
import org.matrix.framework.core.platform.web.freemarker.tag.CSSTag;
import org.matrix.framework.core.platform.web.freemarker.tag.FstaticTag;
import org.matrix.framework.core.platform.web.freemarker.tag.JSTag;
import org.matrix.framework.core.platform.web.freemarker.tag.LogoutTag;
import org.matrix.framework.core.platform.web.freemarker.tag.MasterTag;
import org.matrix.framework.core.platform.web.freemarker.tag.MasterTemplateLoader;
import org.matrix.framework.core.platform.web.freemarker.tag.PageTag;
import org.matrix.framework.core.platform.web.freemarker.tag.StaticTag;
import org.matrix.framework.core.platform.web.freemarker.tag.TagNames;
import org.matrix.framework.core.platform.web.freemarker.tag.TokenTag;
import org.matrix.framework.core.platform.web.freemarker.tag.URLTag;
import org.matrix.framework.core.platform.web.request.RequestContext;
import org.matrix.framework.core.platform.web.session.SessionContext;
import org.matrix.framework.core.settings.CDNSettings;
import org.matrix.framework.core.settings.DeploymentSettings;
import org.matrix.framework.core.settings.SiteSettings;
import org.matrix.framework.core.util.I18nUtil;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import freemarker.template.Template;
import freemarker.template.TemplateModelException;

public class DefaultFreemarkerView extends FreeMarkerView {
    protected SiteSettings siteSettings;
    protected DeploymentSettings deploymentSettings;
    protected CDNSettings cdnSettings;
    protected DefaultFreemarkerViewResolver viewResolver;
    protected RequestContext requestContext;
    protected SessionContext sessionContext;
    protected CookieContext cookieContext;
    protected I18nUtil i18nUtil;

    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        siteSettings = getBeanFromApplicationContext(SiteSettings.class);
        deploymentSettings = getBeanFromApplicationContext(DeploymentSettings.class);
        viewResolver = getBeanFromApplicationContext(DefaultFreemarkerViewResolver.class);
        cdnSettings = getBeanFromApplicationContext(CDNSettings.class);
        requestContext = getBeanFromApplicationContext(RequestContext.class);
        sessionContext = getApplicationContext().getBean("sessionContext", SessionContext.class);
        cookieContext = getBeanFromApplicationContext(CookieContext.class);
        i18nUtil = getBeanFromApplicationContext(I18nUtil.class);
    }

    protected <T> T getBeanFromApplicationContext(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    @Override
    protected void exposeHelpers(Map<String, Object> model, HttpServletRequest request) throws Exception {
        registerURLTag(model, request);
        registerCDNTag(model, request);
        registerMasterTag(model, request);
        registerJSTag(model, request);
        registerCSSTag(model, request);
        registerTOKENTag(model, request);
        registerPageTag(model, request);
        registerStatiTag(model, request);
        registerFstaticTag(model, request);
        registerLogoutTag(model, request);
        exposeRequestContext(model);
    }

    private void exposeRequestContext(Map<String, Object> model) throws TemplateModelException {
        Object previousValue = model.put("requestContext", requestContext);
        if (previousValue != null)
            throw new TemplateModelException("requestContext is reserved name in model, please use different name in model");
    }

    protected void assertTagNameIsAvailable(Object previousValue, String tagName) throws TemplateModelException {
        if (previousValue != null)
            throw new TemplateModelException(String.format("%1$s is reserved name in model as @%1$s, please use different name in model", tagName));
    }

    private void registerURLTag(Map<String, Object> model, HttpServletRequest request) throws TemplateModelException {
        Object previousValue = model.put(TagNames.TAG_URL, new URLTag(request, deploymentSettings));
        assertTagNameIsAvailable(previousValue, TagNames.TAG_URL);
    }

    private void registerJSTag(Map<String, Object> model, HttpServletRequest request) throws TemplateModelException {
        Object previousValue = model.put(TagNames.TAG_JS, new JSTag(request, siteSettings, deploymentSettings, cdnSettings));
        assertTagNameIsAvailable(previousValue, TagNames.TAG_JS);
    }

    private void registerCSSTag(Map<String, Object> model, HttpServletRequest request) throws TemplateModelException {
        Object previousValue = model.put(TagNames.TAG_CSS, new CSSTag(request, siteSettings, deploymentSettings, cdnSettings));
        assertTagNameIsAvailable(previousValue, TagNames.TAG_CSS);
    }

    private void registerTOKENTag(Map<String, Object> model, HttpServletRequest request) throws TemplateModelException {
        Object previousValue = model.put(TagNames.TAG_TOKEN, new TokenTag(request, siteSettings, deploymentSettings, cdnSettings));
        assertTagNameIsAvailable(previousValue, TagNames.TAG_TOKEN);
    }

    private void registerCDNTag(Map<String, Object> model, HttpServletRequest request) throws TemplateModelException {
        Object previousValue = model.put(TagNames.TAG_CDN, new CDNTag(request, siteSettings, deploymentSettings, cdnSettings));
        assertTagNameIsAvailable(previousValue, TagNames.TAG_CDN);
    }

    private void registerMasterTag(Map<String, Object> model, HttpServletRequest request) throws TemplateModelException {
        Locale locale = RequestContextUtils.getLocale(request);
        MasterTemplateLoader templateLoader = new MasterTemplateLoader(viewResolver, this, locale);
        Object previousValue = model.put(TagNames.TAG_MASTER, new MasterTag(model, templateLoader));
        assertTagNameIsAvailable(previousValue, TagNames.TAG_MASTER);
    }

    private void registerPageTag(Map<String, Object> model, HttpServletRequest request) throws TemplateModelException {
        Object previousValue = model.put(TagNames.TAG_PAGE, new PageTag(request, deploymentSettings));
        assertTagNameIsAvailable(previousValue, TagNames.TAG_PAGE);
    }

    private void registerStatiTag(Map<String, Object> model, HttpServletRequest request) throws TemplateModelException {
        Object previousValue = model.put(TagNames.TAG_STATIC, new StaticTag(request, siteSettings, deploymentSettings, cdnSettings));
        assertTagNameIsAvailable(previousValue, TagNames.TAG_STATIC);
    }

    private void registerFstaticTag(Map<String, Object> model, HttpServletRequest request) throws TemplateModelException {
        Object previousValue = model.put(TagNames.TAG_FSTATIC, new FstaticTag(request, siteSettings, deploymentSettings, cdnSettings));
        assertTagNameIsAvailable(previousValue, TagNames.TAG_FSTATIC);
    }

    private void registerLogoutTag(Map<String, Object> model, HttpServletRequest request) throws TemplateModelException {
        Object previousValue = model.put(TagNames.TAG_LOGOUT, new LogoutTag(i18nUtil, request));
        assertTagNameIsAvailable(previousValue, TagNames.TAG_LOGOUT);
    }

    public Template loadTemplate(String fullTemplatePath, Locale locale) throws IOException {
        return getTemplate(fullTemplatePath, locale);
    }

}