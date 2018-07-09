package org.matrix.framework.core.platform.web.freemarker.tag;

import java.io.IOException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.matrix.framework.core.settings.CDNSettings;
import org.matrix.framework.core.settings.DeploymentSettings;
import org.matrix.framework.core.settings.SiteSettings;
import org.springframework.util.StringUtils;
import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class StaticTag extends CDNTagSupport implements TemplateDirectiveModel {

    public StaticTag(HttpServletRequest request, SiteSettings siteSettings, DeploymentSettings deploymentSettings, CDNSettings cdnSettings) {
        super(request, siteSettings, deploymentSettings, cdnSettings);
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        assertNoBody(body);
        String value = getStringParam(params, "value");
        String staticDir = siteSettings.getStaticDir();
        env.getOut().write(StringUtils.hasText(value) ? constructCDNURL(staticDir + value) : constructCDNPrefix(staticDir));
    }

}