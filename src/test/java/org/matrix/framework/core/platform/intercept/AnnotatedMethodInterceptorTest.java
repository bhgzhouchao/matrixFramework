/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.intercept;

import org.matrix.framework.core.SpringTest;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;

/**
 * @see com.nstechs.core.TestAppConfig
 */
public class AnnotatedMethodInterceptorTest extends SpringTest {
    @Inject
    InterceptorTestService testService;

    // interceptor registered on TestAppConfig
    @Test
    public void interceptAnnotatedMethod() {
        testService.execute();

        Assert.assertTrue(testService.isIntercepted());
    }
}
