/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.site;

import org.matrix.framework.core.http.HTTPConstants;
import org.matrix.framework.core.settings.SiteSettings;
import org.matrix.framework.core.util.StringUtils;
import org.slf4j.Logger;
import org.matrix.framework.core.log.LoggerFactory;

public class URLBuilder {
    private  final Logger logger = LoggerFactory.getLogger(URLBuilder.class);

    String contextPath;
    String deploymentContext;
    String serverName;
    String logicalURL;
    int httpsPort;
    int httpPort;
    SiteSettings siteSettings;
    
    public String constructRelativeURL(String relativeURL) {
        StringBuilder builder = new StringBuilder();
        buildRelativeURL(builder, relativeURL);
        return builder.toString();
    }

    public String constructAbsoluteURL(String scheme, String relativeURL) {
        if (!relativeURL.startsWith("/")) {
            logger.error("relative url must start with '/' to construct absolute url");
            throw new IllegalArgumentException("relative url must start with '/' to construct absolute url");
        }
        StringBuilder builder = new StringBuilder();
        buildURLPrefix(builder, scheme);
        buildRelativeURL(builder, relativeURL);
        return builder.toString();
    }
    
    public String buildRelativeURL() {
        StringBuilder builder = new StringBuilder();
        buildRelativeURL(builder, logicalURL);
        return builder.toString();
    }

    void buildRelativeURL(StringBuilder builder, String relativeURL) {
        if (relativeURL.startsWith("/")) {
            String context = getAbsoluteContext(contextPath, deploymentContext);
            builder.append(context).append(relativeURL);
        } else {
            builder.append(relativeURL);
        }
        if (null != siteSettings) {
            String url = builder.toString();
            char connector = url.indexOf('?') < 0 ? '?' : '&';
            builder.append(connector).append("version=").append(siteSettings.getVersion());
        }
    }

    private String getAbsoluteContext(String servletContextPath, String deploymentContext) {
        if (StringUtils.hasText(deploymentContext)) {
            if ("/".equals(deploymentContext)) return ""; // because the url value contains '/' already
            return deploymentContext;
        } else {
            if ("/".equals(servletContextPath)) return "";
            return servletContextPath;
        }
    }

    private void buildURLPrefix(StringBuilder builder, String scheme) {
        String schemaInLowerCase = scheme.toLowerCase();
        builder.append(schemaInLowerCase)
                .append("://")
                .append(serverName);

        if (HTTPConstants.SCHEME_HTTP.equals(schemaInLowerCase) && httpPort != 80) {
            builder.append(":").append(httpPort);
        } else if (HTTPConstants.SCHEME_HTTPS.equals(schemaInLowerCase) && httpsPort != 443) {
            builder.append(":").append(httpsPort);
        }
    }

    public void setContext(String contextPath, String deploymentContext) {
        this.contextPath = contextPath;
        this.deploymentContext = deploymentContext;
    }

    public void setServerInfo(String serverName, int httpPort, int httpsPort) {
        this.serverName = serverName;
        this.httpPort = httpPort;
        this.httpsPort = httpsPort;
    }

    public void setLogicalURL(String logicalURL) {
        this.logicalURL = logicalURL;
    }

    public void setSiteSettings(SiteSettings siteSettings) {
        this.siteSettings = siteSettings;
    }

}