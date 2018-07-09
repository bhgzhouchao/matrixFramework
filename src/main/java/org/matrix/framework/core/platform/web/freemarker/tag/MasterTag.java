/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.freemarker.tag;

import freemarker.core.Environment;
import freemarker.template.Template;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateDirectiveModel;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModel;
import freemarker.template.TemplateModelException;

import java.io.IOException;
import java.util.Map;

import org.slf4j.Logger;
import org.matrix.framework.core.log.LoggerFactory;

public class MasterTag extends TagSupport implements TemplateDirectiveModel {
    
    private final Logger logger = LoggerFactory.getLogger(MasterTag.class);

    private final MasterTemplateLoader templateLoader;
    private final Map<String, Object> model;

    public MasterTag(Map<String, Object> model, MasterTemplateLoader templateLoader) {
        this.model = model;
        this.templateLoader = templateLoader;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
        assertHasBody(body);
        String template = getRequiredStringParam(params, "template");

        model.putAll(params);

        Template masterTemplate = templateLoader.loadTemplate(template);
        Object previousValue = model.put(TagNames.TAG_BODY, new BodyTag(body));
        if (previousValue != null) {
            logger.error("body is reserved name in model as @body, please use different name in model");
            throw new TemplateModelException("body is reserved name in model as @body, please use different name in model");
        }

        masterTemplate.process(model, env.getOut());
    }
}
