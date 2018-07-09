package org.matrix.framework.core.platform.web.freemarker.tag;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.matrix.framework.core.platform.web.site.URLBuilder;
import org.matrix.framework.core.settings.DeploymentSettings;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;

public class PageTag extends TagSupport implements TemplateDirectiveModel {

    private static final int STEP = 3;
    private final HttpServletRequest request;
    private final DeploymentSettings deploymentSettings;

    public PageTag(HttpServletRequest request, DeploymentSettings deploymentSettings) {
        this.request = request;
        this.deploymentSettings = deploymentSettings;
    }

    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        assertNoBody(body);

        String url = getRequiredStringParam(params, "url");
        Integer pageNo = Integer.valueOf(getRequiredStringParam(params, "pageNo"));
        Integer pageSize = Integer.valueOf(getStringParam(params, "pageSize"));
        Integer totalRecords = Integer.valueOf(getRequiredStringParam(params, "totalRecords"));

        Writer output = env.getOut();
        output.write(createPageNav(request.getContextPath() + url, pageNo, pageSize, totalRecords));
    }

    private String createOption(Integer pageSize) {
        int currentPagesize = null == pageSize || pageSize == 0 ? 10 : pageSize;
        int[] chooses = { 10, 20, 30, 50, 80, 100 };
        StringBuilder options = new StringBuilder();
        options.append("<select id=\"syncPageSize\" name=\"syncPageSize\">");
        for (int choose : chooses) {
            if (choose == currentPagesize) {
                options.append("<option selected=\"selected\" value=\"" + choose + "\">" + choose + "</option>");
            } else {
                options.append("<option  value=\"" + choose + "\">" + choose + "</option>");
            }
        }
        options.append("</select>");
        return options.toString();
    }

    private String createLink(String url, Integer pageNo, Integer pageSize, Integer totalRecords) {
        Integer totalPages = getTotalPages(pageSize, totalRecords);
        StringBuilder html = new StringBuilder("<p>");

        Integer begin = pageNo - STEP;
        Integer pageFrom = begin > 0 ? begin : 1;

        Integer end = pageNo + STEP;
        Integer pageTo = end <= totalPages ? end : totalPages;

        if (pageFrom != 1) {
            html.append("<a href=\"" + createUrl(url, 1, pageSize) + "\">1</a>");
            html.append((pageFrom == 2) ? "" : "<span>...</span>");
        }
        for (int i = pageFrom; i <= pageTo; i++) {
            String pageurl = createUrl(url, i, pageSize);
            html.append(i == pageNo ? "<strong data-page=\"" + i + "\">" + i + "</strong>" : "<a href=\"" + pageurl + "\">" + i + "</a>");
        }
        if (!pageTo.equals(totalPages)) {
            html.append(pageTo + 1 == totalPages ? "" : "<span>...</span>");
            html.append("<a href=\"" + createUrl(url, totalPages, pageSize) + "\">" + totalPages + "</a>");
        }
        html.append("</p>");
        return html.toString();
    }

    protected String createPageNav(String url, int pageNo, int pageSize, int totalRecords) {
        StringBuilder div = new StringBuilder();
        div.append("<div class=\"pageNav\" id=\"syncPageNav\" data-remote=\"" + url + "\">");
        div.append("Total Records : <span id=\"recordsLen\">" + totalRecords + "</span>");
        div.append(createOption(pageSize));
        div.append(createLink(url, pageNo, pageSize, totalRecords));
        div.append("</div>");
        return div.toString();
    }

    private Integer getTotalPages(Integer pageSize, Integer totalRecords) {
        return (totalRecords + pageSize - 1) / pageSize;
    }

    private String createUrl(String url, Integer pageNo, Integer pageSize) {
        URLBuilder builder = new URLBuilder();
        builder.setContext(request.getContextPath(), deploymentSettings.getDeploymentContext());
        return builder.constructRelativeURL(url + "/" + pageNo + "/" + pageSize);
    }

}
