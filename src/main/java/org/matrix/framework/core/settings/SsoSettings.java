/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.settings;

import java.util.HashMap;
import java.util.Map;
import org.matrix.framework.core.collection.Key;

public class SsoSettings {
    
    private String clientKey;
    
    private String secondclientKey;
    
    private String ssoConstantsClassName;

    private String globalDns;

    private String cookieDomain;

    private String cookiePath;

    private final Map<Key<SsoSite>, SsoSite> ssoSites = new HashMap<Key<SsoSite>, SsoSite>();

    public static class SsoSite {

        private final String dns;

        private final String deployContext;

        private String accessPoint;

        public SsoSite(String dns, String deployContext, String accessPoint) {
            this.dns = dns;
            this.deployContext = deployContext;
            this.accessPoint = accessPoint;
        }

        public String getDns() {
            return dns;
        }

        public String getDeployContext() {
            return deployContext;
        }

        public String getAccessPoint() {
            return accessPoint;
        }

        public void setAccessPoint(String accessPoint) {
            this.accessPoint = accessPoint;
        }
    }
    
    

    public String getClientKey() {
        return clientKey;
    }

    public void setClientKey(String clientKey) {
        this.clientKey = clientKey;
    }

    public Map<Key<SsoSite>, SsoSite> getSsoSites() {
        return ssoSites;
    }

    public String getGlobalDns() {
        return globalDns;
    }

    public void setGlobalDns(String globalDns) {
        this.globalDns = globalDns;
    }

    public String getCookieDomain() {
        return cookieDomain;
    }

    public void setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
    }

    public String getCookiePath() {
        return cookiePath;
    }

    public void setCookiePath(String cookiePath) {
        this.cookiePath = cookiePath;
    }

    public String getSsoConstantsClassName() {
        return ssoConstantsClassName;
    }

    public void setSsoConstantsClassName(String ssoConstantsClassName) {
        this.ssoConstantsClassName = ssoConstantsClassName;
    }

    public String getSecondclientKey() {
        return secondclientKey;
    }

    public void setSecondclientKey(String secondclientKey) {
        this.secondclientKey = secondclientKey;
    }

}