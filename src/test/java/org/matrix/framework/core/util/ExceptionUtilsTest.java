/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.matchers.JUnitMatchers;

public class ExceptionUtilsTest {
    @Test
    public void getStackTrace() {
        RuntimeException exception = new RuntimeException();
        String trace = ExceptionUtils.stackTrace(exception);

        System.err.println(trace);

        Assert.assertThat(trace, JUnitMatchers.containsString(RuntimeException.class.getName()));
    }
}
