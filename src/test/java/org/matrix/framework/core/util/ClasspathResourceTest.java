/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ClasspathResourceTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void shouldThrowExceptionIfResourceNotFound() {
        exception.expect(IllegalArgumentException.class);
        new ClasspathResource("not-existed-resource");
    }
}
