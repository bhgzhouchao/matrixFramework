/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.freemarker.view;

import freemarker.cache.TemplateLoader;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

public class DefaultFreeMarkerConfigurer extends FreeMarkerConfigurer {
    // replace default template loader with html escaping implementation, to prevent XSS by default.
    @Override
    protected TemplateLoader getTemplateLoaderForPath(String templateLoaderPath) {
        TemplateLoader loader = super.getTemplateLoaderForPath(templateLoaderPath);
        return new HTMLEscapeTemplateLoader(loader);
    }
}
