/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.http;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class FormPostTest {
    @Test
    public void buildPostEntity() throws IOException {
        FormPost post = new FormPost("http://url");
        post.addParameter("key1", "value1");
        post.addParameter("key2", "value2");

        HttpPost request = (HttpPost) post.createRequest();

        HttpEntity entity = request.getEntity();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        entity.writeTo(outputStream);

        assertEquals("application/x-www-form-urlencoded; charset=UTF-8", entity.getContentType().getValue());
        assertEquals("key1=value1&key2=value2", new String(outputStream.toByteArray()));
    }

    @Test
    public void addParameter() {
        FormPost post = new FormPost("http://url");
        post.addParameter("key1", "value1");
        post.addParameter("key1", "value2");

        assertEquals(2, post.parameters.size());
        assertEquals("value1", post.parameters.get(0).getValue());
        assertEquals("value2", post.parameters.get(1).getValue());
    }

    @Test
    public void setParameterShouldRemoveAllPriorValues() {
        FormPost post = new FormPost("http://url");
        post.addParameter("key1", "priorValue1");
        post.addParameter("key1", "priorValue2");
        post.setParameter("key1", "correctValue");

        assertEquals(1, post.parameters.size());
        assertEquals("correctValue", post.parameters.get(0).getValue());
    }
}
