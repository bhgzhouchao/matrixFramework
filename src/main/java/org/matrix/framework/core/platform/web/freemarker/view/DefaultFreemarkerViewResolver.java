/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.freemarker.view;

import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

public class DefaultFreemarkerViewResolver extends FreeMarkerViewResolver {
    public String buildFullTemplatePath(String template) {
        return String.format("%s%s%s", getPrefix(), template, getSuffix());
    }
}
