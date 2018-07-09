/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.settings;

public class DeploymentSettings {
    private String host;
    private String deploymentContext;
    private int httpPort = 80;
    private int httpsPort = 443;

    public String getDeploymentContext() {
        return deploymentContext;
    }

    public void setDeploymentContext(String deploymentContext) {
        this.deploymentContext = deploymentContext;
    }

    public int getHTTPPort() {
        return httpPort;
    }

    public void setHTTPPort(int httpPort) {
        this.httpPort = httpPort;
    }

    public int getHTTPSPort() {
        return httpsPort;
    }

    public void setHTTPSPort(int httpsPort) {
        this.httpsPort = httpsPort;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}