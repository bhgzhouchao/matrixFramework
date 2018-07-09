/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.http;

import org.junit.Assert;
import org.junit.Test;

public class HTTPStatusCodeTest {
    @Test
    public void isRedirectStatusCode() {
        HTTPStatusCode statusCode = new HTTPStatusCode(HTTPConstants.SC_MOVED_PERMANENTLY);

        Assert.assertTrue(statusCode.isRedirect());
    }

    @Test
    public void isSuccessStatusCode() {
        HTTPStatusCode statusCode = new HTTPStatusCode(HTTPConstants.SC_OK);

        Assert.assertTrue(statusCode.isSuccess());
    }
}
