/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.freemarker.tag;


import freemarker.template.SimpleScalar;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateModelException;

import java.util.Map;


public class TagSupport {
    protected void assertNoBody(TemplateDirectiveBody body) throws TemplateModelException {
        if (body != null)
            throw new TemplateModelException(String.format("%s directive does not allow body", getClass().getSimpleName()));
    }

    protected void assertHasBody(TemplateDirectiveBody body) throws TemplateModelException {
        if (body == null)
            throw new TemplateModelException(String.format("%s directive should have body", getClass().getSimpleName()));
    }

    protected String getRequiredStringParam(Map params, String key) throws TemplateModelException {
        Object value = params.get(key);
        if (!(value instanceof SimpleScalar))
            throw new TemplateModelException(String.format("%s param is required by %s, and must be string", key, getClass().getSimpleName()));
        return ((SimpleScalar) value).getAsString();
    }

    protected String getStringParam(Map params, String key) throws TemplateModelException {
        Object value = params.get(key);
        if (value == null) return null;
        if (!(value instanceof SimpleScalar))
            throw new TemplateModelException(String.format("%s param must be string in %s", key, getClass().getSimpleName()));
        return ((SimpleScalar) value).getAsString();
    }
}
