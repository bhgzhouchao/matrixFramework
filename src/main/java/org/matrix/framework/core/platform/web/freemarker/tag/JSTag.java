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
import freemarker.template.TemplateModelException;

import javax.servlet.http.HttpServletRequest;

import org.matrix.framework.core.settings.CDNSettings;
import org.matrix.framework.core.settings.DeploymentSettings;
import org.matrix.framework.core.settings.SiteSettings;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class JSTag extends CDNTagSupport implements TemplateDirectiveModel {
    public static final String TEMPLATE_JS_TAG = "<script type=\"text/javascript\" src=\"%s\"%s></script>";

    public JSTag(HttpServletRequest request, SiteSettings siteSettings, DeploymentSettings deploymentSettings, CDNSettings cdnSettings) {
        super(request, siteSettings, deploymentSettings, cdnSettings);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        assertNoBody(body);
        String tags = buildJSTags(params);
        Writer output = env.getOut();
        output.write(tags);
    }

    String buildJSTags(Map<String, Object> params) throws IOException, TemplateModelException {
        return buildMultipleResourceTags("src", siteSettings.getJsDir(), TEMPLATE_JS_TAG, params);
    }
}
