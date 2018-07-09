/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.xml;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.matrix.framework.core.xml.DOMUtils.setText;
import static org.matrix.framework.core.xml.DOMUtils.text;
import static org.junit.Assert.assertEquals;

public class DOMUtilsSetTextTest {
    @Test
    public void shouldAppendNewTextNodeForEmptyElement() {
        Document document = new XMLParser().parse("<xml/>");
        Element element = document.getDocumentElement();
        setText(element, "value");
        assertEquals("<xml>value</xml>", text(element).trim());
    }

    @Test
    public void shouldChangeInnerText() {
        Document document = new XMLParser().parse("<xml>old</xml>");
        Element element = document.getDocumentElement();
        setText(element, "new");
        assertEquals("<xml>new</xml>", text(element).trim());
    }

    @Test(expected = XMLException.class)
    public void shouldThrowExceptionIfElementHasNonTextChild() {
        Document document = new XMLParser().parse("<xml><child/></xml>");
        setText(document.getDocumentElement(), "value");
    }

    @Test(expected = XMLException.class)
    public void shouldThrowExceptionIfElementHasMultipleChild() {
        Document document = new XMLParser().parse("<xml><child1/><child2/></xml>");
        setText(document.getDocumentElement(), "value");
    }
}
