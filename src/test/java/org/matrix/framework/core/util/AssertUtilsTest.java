/**********************************************************************
 * Copyright (c):    2014-2049 chengdu nstechs company, All rights reserved.
 * Technical Support:Chengdu nstechs company
 * Contact:          allen.zhou@nstechs.com,18782989997
 **********************************************************************/
package org.matrix.framework.core.util;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class AssertUtilsTest {
    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void assertFalse() {
        exception.expect(AssertUtils.AssertionException.class);
        exception.expectMessage("someErrorMessage");

        AssertUtils.assertFalse(true, "someErrorMessage");
    }

    @Test
    public void assertHasText() {
        exception.expect(AssertUtils.AssertionException.class);
        exception.expectMessage("someErrorMessage");

        AssertUtils.assertHasText(" ", "someErrorMessage");
    }
}
