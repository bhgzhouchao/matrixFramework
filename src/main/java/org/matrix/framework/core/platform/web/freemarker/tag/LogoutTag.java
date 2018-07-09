package org.matrix.framework.core.platform.web.freemarker.tag;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.matrix.framework.core.util.I18nUtil;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class LogoutTag extends TagSupport implements TemplateDirectiveModel {

    private final HttpServletRequest request;

    private final I18nUtil i18nUtil;

    public LogoutTag(I18nUtil i18nUtil, HttpServletRequest request) {
        this.i18nUtil = i18nUtil;
        this.request = request;
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        assertNoBody(body);
        String label = i18nUtil.getMessage("nstechs.i18n.logout");
        String basePath = request.getContextPath();
        String link = "<a href='" + basePath + "/logout" + "'> " + label + "</a>";
        Writer output = env.getOut();
        output.write(link);

    }
}
