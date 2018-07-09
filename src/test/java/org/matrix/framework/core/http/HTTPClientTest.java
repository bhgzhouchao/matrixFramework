/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.http;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.rules.ExpectedException;

public class HTTPClientTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    HTTPClient client;

    @Before
    public void createHTTPClient() {
        client = new HTTPClient();
    }

    @Test
    public void receiveRedirectStatusCode() {
        client.validateStatusCode(new HTTPStatusCode(HTTPConstants.SC_MOVED_PERMANENTLY));
    }

    @Test
    public void receiveSuccessStatusCode() {
        client.validateStatusCode(new HTTPStatusCode(HTTPConstants.SC_ACCEPTED));
    }

    @Test
    public void receiveClientErrorStatusCode() {
        exception.expect(HTTPException.class);
        exception.expectMessage(JUnitMatchers.containsString(String.valueOf(HttpStatus.SC_FORBIDDEN)));
        client.setValidateStatusCode(true);
        client.validateStatusCode(new HTTPStatusCode(HTTPConstants.SC_FORBIDDEN));
    }

}
