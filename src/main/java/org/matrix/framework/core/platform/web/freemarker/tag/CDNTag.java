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

import org.matrix.framework.core.settings.CDNSettings;
import org.matrix.framework.core.settings.DeploymentSettings;
import org.matrix.framework.core.settings.SiteSettings;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class CDNTag extends CDNTagSupport implements TemplateDirectiveModel {
    public CDNTag(HttpServletRequest request, SiteSettings siteSettings, DeploymentSettings deploymentSettings, CDNSettings cdnSettings) {
        super(request, siteSettings, deploymentSettings, cdnSettings);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        assertNoBody(body);
        String url = getRequiredStringParam(params, "value");

        String completeURL = constructCDNURL(url);
        Writer output = env.getOut();
        output.write(completeURL);
    }
}
