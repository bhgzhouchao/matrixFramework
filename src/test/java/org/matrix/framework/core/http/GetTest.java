/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.http;

import org.apache.http.client.methods.HttpRequestBase;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class GetTest {
    @Test
    public void buildURLWithoutParameters() {
        Get get = new Get("http://www.google.com/search");
        assertEquals("http://www.google.com/search", get.createURL());
    }

    @Test
    public void buildURLWithParameters() {
        Get get = new Get("http://www.google.com/search");
        get.addParameter("q", "value1 value2");
        assertEquals("http://www.google.com/search?q=value1+value2", get.createURL());
    }

    @Test
    public void buildURLWithRootURLHasParameters() {
        Get get = new Get("http://www.google.com/search?output=csv");
        get.addParameter("q", "value1 value2");
        assertEquals("http://www.google.com/search?output=csv&q=value1+value2", get.createURL());
    }

    @Test
    public void createRequestWithoutParams() {
        Get get = new Get("http://www.google.com/search");

        HttpRequestBase request = get.createRequest();
        assertEquals("http://www.google.com/search", request.getURI().toString());
    }
}
