/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.exception;

import org.junit.Assert;
import org.junit.Test;

public class InvalidRequestExceptionTest {
    @Test
    public void createExceptionWithMessageOnly() {
        InvalidRequestException exception = new InvalidRequestException("errorMessage");
        Assert.assertEquals("errorMessage", exception.getMessage());
        Assert.assertNull(exception.getField());
    }
}
