/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform;

import org.matrix.framework.core.SpringTest;
import org.matrix.framework.core.http.HTTPClient;
import org.matrix.framework.core.platform.web.freemarker.template.FreemarkerTemplate;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

public class DefaultAppConfigTest extends SpringTest {
    @Inject
    HTTPClient httpClient1;

    @Inject
    HTTPClient httpClient2;

    @Test
    public void httpClientShouldBeRegisteredAsPrototype() {
        Assert.assertNotNull(httpClient1);
        Assert.assertNotNull(httpClient2);
        Assert.assertNotSame("httpClient should be prototype", httpClient1, httpClient2);
    }

    @Inject
    FreemarkerTemplate freemarkerTemplate1;

    @Inject
    FreemarkerTemplate freemarkerTemplate2;

    @Test
    public void freemarkerTemplateShouldBeRegisteredAsPrototype() {
        Assert.assertNotNull(freemarkerTemplate1);
        Assert.assertNotNull(freemarkerTemplate2);
        Assert.assertNotSame("freemarkerTemplate should be prototype", freemarkerTemplate1, freemarkerTemplate2);
    }
}