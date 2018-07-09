/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.settings;

import org.matrix.framework.core.platform.web.RuntimeEnvironment;
import org.matrix.framework.core.platform.web.session.SessionProviderType;
import org.matrix.framework.core.util.TimeLength;

public class SiteSettings {

    // 1.page setting
    private String errorPage;

    private String resourceNotFoundPage;

    private String sessionTimeOutPage;

    // 2.static dir setting
    private String staticDir;

    private String nfsDir;

    private String jsDir;

    private String cssDir;

    // 3.about session setting
    private SessionProviderType sessionProviderType = SessionProviderType.LOCAL;

    private TimeLength sessionTimeOut = TimeLength.minutes(15);

    // 4.login url and original url setting.
    private String loginUrl;

    private String originalUrl;

    private RuntimeEnvironment environment = RuntimeEnvironment.DEV;

    private String version;

    public String getErrorPage() {
        return errorPage;
    }

    public void setErrorPage(String errorPage) {
        this.errorPage = errorPage;
    }

    public String getResourceNotFoundPage() {
        return resourceNotFoundPage;
    }

    public void setResourceNotFoundPage(String resourceNotFoundPage) {
        this.resourceNotFoundPage = resourceNotFoundPage;
    }

    public String getSessionTimeOutPage() {
        return sessionTimeOutPage;
    }

    public void setSessionTimeOutPage(String sessionTimeOutPage) {
        this.sessionTimeOutPage = sessionTimeOutPage;
    }

    public TimeLength getSessionTimeOut() {
        return sessionTimeOut;
    }

    public void setSessionTimeOut(TimeLength sessionTimeOut) {
        this.sessionTimeOut = sessionTimeOut;
    }

    public SessionProviderType getSessionProviderType() {
        return sessionProviderType;
    }

    public void setSessionProviderType(SessionProviderType sessionProviderType) {
        this.sessionProviderType = sessionProviderType;
    }

    public String getJsDir() {
        return jsDir;
    }

    public void setJsDir(String jsDir) {
        this.jsDir = jsDir;
    }

    public String getCssDir() {
        return cssDir;
    }

    public void setCssDir(String cssDir) {
        this.cssDir = cssDir;
    }

    public String getNfsDir() {
        return nfsDir;
    }

    public void setNfsDir(String nfsDir) {
        this.nfsDir = nfsDir;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public void setOriginalUrl(String originalUrl) {
        this.originalUrl = originalUrl;
    }

    public String getStaticDir() {
        return staticDir;
    }

    public void setStaticDir(String staticDir) {
        this.staticDir = staticDir;
    }

    public RuntimeEnvironment getEnvironment() {
        return environment;
    }

    public void setEnvironment(RuntimeEnvironment environment) {
        this.environment = environment;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}