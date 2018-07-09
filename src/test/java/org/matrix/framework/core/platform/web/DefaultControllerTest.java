/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.web;

import org.matrix.framework.core.SpringTest;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

public class DefaultControllerTest extends SpringTest {
    @Inject
    TestController testController;

    @Test
    public void getMessage() {
        String message = testController.getMessage("test.message1");
        Assert.assertEquals("testMessage", message);
    }

    @Test
    public void getMessageWithParams() {
        String message = testController.getMessage("test.message2", "Value");
        Assert.assertEquals("testMessageValue", message);
    }
}
