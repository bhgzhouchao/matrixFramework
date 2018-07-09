/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.platform.intercept;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

public class TestInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        InterceptorTestAnnotation annotation = invocation.getMethod().getAnnotation(InterceptorTestAnnotation.class);
        if (annotation != null) {
            InterceptorTestService service = (InterceptorTestService) invocation.getThis();
            service.setIntercepted(true);
        }
        return invocation.proceed();
    }
}
