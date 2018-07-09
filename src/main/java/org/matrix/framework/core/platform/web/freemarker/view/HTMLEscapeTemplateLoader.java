/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.freemarker.view;

import freemarker.cache.TemplateLoader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import org.matrix.framework.core.util.IOUtils;

public class HTMLEscapeTemplateLoader implements TemplateLoader {
    private final TemplateLoader templateLoader;

    public HTMLEscapeTemplateLoader(TemplateLoader templateLoader) {
        this.templateLoader = templateLoader;
    }

    @Override
    public Object findTemplateSource(String name) throws IOException {
        return templateLoader.findTemplateSource(name);
    }

    @Override
    public long getLastModified(Object templateSource) {
        return templateLoader.getLastModified(templateSource);
    }

    @Override
    public Reader getReader(Object templateSource, String encoding) throws IOException {
        Reader reader = templateLoader.getReader(templateSource, encoding);
        StringBuilder builder = new StringBuilder("<#escape x as x?html>");
        builder.append(IOUtils.text(reader));
        builder.append("</#escape>");
        return new StringReader(builder.toString());
    }

    @Override
    public void closeTemplateSource(Object templateSource) throws IOException {
        templateLoader.closeTemplateSource(templateSource);
    }
}
