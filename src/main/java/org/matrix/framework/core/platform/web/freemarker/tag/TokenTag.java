/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.freemarker.tag;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.UUID;
import javax.servlet.http.HttpServletRequest;

import org.matrix.framework.core.platform.web.site.URLBuilder;
import org.matrix.framework.core.settings.CDNSettings;
import org.matrix.framework.core.settings.DeploymentSettings;
import org.matrix.framework.core.settings.SiteSettings;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

public class TokenTag extends CDNTagSupport implements TemplateDirectiveModel {

    public static final String SYNCH_TOKEN_INPUT_FIELD_NAME = "SYNCH_TOKEN_INPUT_FIELD_NAME";

    public TokenTag(HttpServletRequest request, SiteSettings siteSettings, DeploymentSettings deploymentSettings, CDNSettings cdnSettings) {
        super(request, siteSettings, deploymentSettings, cdnSettings);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        assertNoBody(body);
        String tags = buildTOKENTags(params);
        
        Writer output = env.getOut();
        output.write(tags);
    }

    String buildTOKENTags(Map<String, Object> params) throws IOException, TemplateModelException {
        String action = getRequiredStringParam(params, "action");
        String scheme = getStringParam(params, "scheme");
        char connector = action.indexOf('?') < 0 ? '?' : '&';
        final String TEMPLATE_TOKEN_TAG = "%s" + connector + "%s=%s";
        String url = String.format(TEMPLATE_TOKEN_TAG, action, SYNCH_TOKEN_INPUT_FIELD_NAME, UUID.randomUUID());
        return constructURL(url, scheme);
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