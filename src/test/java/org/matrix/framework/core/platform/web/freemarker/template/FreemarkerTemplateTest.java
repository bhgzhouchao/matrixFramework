/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web.freemarker.template;

import org.matrix.framework.core.TestResource;
import org.matrix.framework.core.xml.XMLParser;

import freemarker.ext.dom.NodeModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;
import org.junit.rules.ExpectedException;

import java.util.HashMap;
import java.util.Map;

public class FreemarkerTemplateTest {
    @Rule 
    public ExpectedException exception = ExpectedException.none();

    FreemarkerTemplate freemarkerTemplate;

    @Before
    public void createFreemarkerTemplate() {
        freemarkerTemplate = new FreemarkerTemplate();
    }

    public static class Bean {
        private String name;

        public String method() {
            return "someMessage";
        }

        public String methodWithParam(String param) {
            return "return-" + param;
        }

        public String getName() {
            return name;
        }
    }

    @Test
    public void useObjectAsParameters() {
        
        freemarkerTemplate.setTemplateSource(TestResource.text("/template/freemarker-object-params-test.ftl"));

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("name", "someName");

        Bean bean = new Bean();
        bean.name = "someBeanName";
        context.put("bean", bean);

        String result = freemarkerTemplate.transform(context);
        System.err.println(result);

        assertContainsString(result, "<name>someName</name>");
        assertContainsString(result, "<bean name=\"someBeanName\">");
        assertContainsString(result, "<method>someMessage</method>");
        assertContainsString(result, "<method-with-param>return-param</method-with-param>");
        assertContainsString(result, "<substring>so</substring>");
    }

    @Test
    public void useXMLAsInput() {
        freemarkerTemplate.setTemplateSource(TestResource.text("/template/freemarker-xml-params-test.ftl"));

        String inputXML = "<request>"
                + "<name>someName</name>"
                + "</request>";
        Map<String, Object> context = new HashMap<String, Object>();
        context.put("xml", NodeModel.wrap(new XMLParser().parse(inputXML)));

        String result = freemarkerTemplate.transform(context);
        System.err.println(result);

        assertContainsString(result, "<name>someName</name>");
    }

    private void assertContainsString(String actual, String subString) {
        Assert.assertThat(actual, JUnitMatchers.containsString(subString));
    }
}
