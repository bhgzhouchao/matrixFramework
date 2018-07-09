/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.freemarker.tag;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

import javax.servlet.http.HttpServletRequest;

import org.matrix.framework.core.platform.web.site.URLBuilder;
import org.matrix.framework.core.settings.DeploymentSettings;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

/**
 * freemarker url directive, used as "@url" in ftl, supports override context in site config in case proxy to different context
 *
 */
public class URLTag extends TagSupport implements TemplateDirectiveModel {
    private final HttpServletRequest request;
    private final DeploymentSettings deploymentSettings;

    public URLTag(HttpServletRequest request, DeploymentSettings deploymentSettings) {
        this.request = request;
        this.deploymentSettings = deploymentSettings;
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        assertNoBody(body);
        String url = getRequiredStringParam(params, "value");
        String scheme = getStringParam(params, "scheme");
        String completeURL = constructURL(url, scheme);
        Writer output = env.getOut();
        output.write(completeURL);
    }

    private String constructURL(String url, String scheme) {
        URLBuilder builder = new URLBuilder();
        builder.setContext(request.getContextPath(), deploymentSettings.getDeploymentContext());
        String completeURL;
        if (scheme != null) {
            builder.setServerInfo(request.getServerName(), deploymentSettings.getHTTPPort(), deploymentSettings.getHTTPSPort());
            completeURL = builder.constructAbsoluteURL(scheme, url);
        } else {
            completeURL = builder.constructRelativeURL(url);
        }
        return completeURL;
    }
}
